package com.aoredon.onevsone.arena;

import java.util.ArrayList;
import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.aoredon.onevsone.Main;
import com.aoredon.onevsone.gui.GuiManager;

/**
 * This class is the arena class which allows for arenas to have custom names,
 * descriptions, icons, locations and player caps. The arena manages almost all
 * queue logic and ensures that the arena GUI's information is up to date.
 * 
 * @author Alex Guest
 */
public class Arena {
	// Allows quick access to the main class instance.
	private Main main = Main.getInstance();
	
	// The name of the arena.
	public String name;
	// The description for the arena.
	private String description;
	// The material for the arena which is used as the icon in GUIs.
	private Material material;
	// The item stack for the arena.
	private ItemStack itemStack;
	// The spawn locations in the arena.
	private ArrayList<Location> locations;
	// The maximum number of players in the arena.
	private int playerCap;
	
	// Whether or not this arena is disabled.
	private boolean disabled;
	
	// The current queue of participants for the arena.
	private ArrayList<Participant> queue = new ArrayList<Participant>();
	
	// The current round for the arena.
	private Round round;
	
	// The number of rounds the arena has.
	private int rounds;
	// The number of rounds the arena has completed.
	private int roundsComplete;
	
	/**
	 * Initialises the properties for the arena.
	 * 
	 * @param name			the name of the arena
	 * @param description		the description for the arena
	 * @param material		the material which is used as the icon for the arena
	 * @param locations		a list of spawn locations in the arena 
	 * @param playerCap		the maximum number of players allowed in the arena
	 * @param rounds		the number of rounds the arena has
	 */
	public Arena(String name, String description, Material material, 
			ArrayList<Location> locations, int playerCap, int rounds) {
		this.name = name;
		this.description = description;
		this.material = material;
		this.itemStack = this.initItemStack(locations.size(),
				playerCap, rounds);
		this.locations = locations;
		this.playerCap = playerCap;
		this.rounds = rounds;
	}
	
	/**
	 * Updates the arena's item stack.
	 */
	public void updateItemStack() {
		// Creates the arena item stack.
		this.itemStack = this.initItemStack(this.locations.size(),
				this.playerCap, this.rounds);
		// Updates the arena GUI.
		this.main.getGuiManger().findGui(GuiManager.ARENA_GUI).updateGui();
	}
	
	/**
	 * Creates the item stack for the arena.
	 * 
	 * @param locations	the number of spawn locations the arena has
	 * @param playerCap	the maximum number of players that can fit in the arena
	 * @param rounds	the number of rounds the arena has
	 * @return		the item stack for the arena
	 */
	public ItemStack initItemStack(final int locations, final int playerCap, final int rounds) {
		// Creates the item stack from the arena's material.
		ItemStack itemStack = new ItemStack(this.material, 1);
		// Gets the item meta from the item stack.
		ItemMeta itemMeta = itemStack.getItemMeta();
		
		// Sets the display name of the item stack to the arena's name.
		itemMeta.setDisplayName(ChatColor.BOLD + "" + Main.COLOUR + this.name);
		
		// Sets the lore of the item stack to extra information about the arena.
		itemMeta.setLore(new ArrayList<String>() {{
			// Adds the arena's description to the lore.
		    this.add(ChatColor.RESET + "" + description);
		    // Adds the players that are queued for the arena to the lore.
		    this.add(ChatColor.GRAY + "Players in Queue: " + queue.size());
		    // Adds the maximum number of supported players to the lore.
		    this.add(ChatColor.GRAY + "Player Cap: " + playerCap);
		    // Adds the number of rounds to the lore.
		    this.add(ChatColor.GRAY + "Rounds: " + rounds);
		    
		    // Checks to ensure the arena has locations.
			if (locations == 0) {
				// Adds the error to the arena lore.
				this.add(ChatColor.RESET + "" +
						ChatColor.RED +
							"Error: This arena has no locations.");
				// Disables the arena.
				disabled = true;
			}
			
			// Checks to ensure the arena has a player cap larger than zero.
			if (playerCap <= 0) {
				// Adds the error to the arena lore.
				this.add(ChatColor.RESET + "" +
						ChatColor.RED +
							"Error: This arena's player cap is too low.");
				// Disables the arena.
				disabled = true;
			}
			
			// Checks to ensure the arena has rounds.
			if (rounds <= 0) {
				// Adds the error to the arena lore.
				this.add(ChatColor.RESET + "" +
						ChatColor.RED +
							"Error: This arena has no rounds.");
				// Disables the arena.
				disabled = true;
			}
		}});
		
		// Sets the item stacks meta to the updated meta.
		itemStack.setItemMeta(itemMeta);
		
		return itemStack;
	}
	
	/**
	 * Starts a new round.
	 */
	public void newRound() {
		// Initialises a new round.
		this.round = new Round(this);
		
		// Declares and initialises an iterator for the participants in queue
		Iterator<Participant> iterator = this.queue.iterator();
		
		// Gets participants from the queue until we reach the player cap.
		for (int i = 0; i < this.playerCap; i++) {
			// Gets the participant from the iterator.
			Participant participant = iterator.next();
			
			// Adds the participant to the round.
			this.round.addParticipant(participant);
			// Removes the participant from the iterator.
			iterator.remove();
		}
		
		// Starts the round.
		this.round.start();
	}
	
	/**
	 * Disposes of the current round.
	 */
	public void disposeRound() {
		this.round = null;
	}
	
	/**
	 * A method which is called when the specified round is finished.
	 * 
	 * @param round	the round that has finished
	 */
	public void roundFinished(Round round) {
		// Disposes of the round.
		this.disposeRound();
		// Increments the number of rounds that the arena has completed.
		this.incrementRoundsComplete();
		
		// Loops through each particiapnt in the round.
		for (Participant participant : round.getParticipants()) {
			// Gives the player the server starter equipment.
			this.main.getEquipmentManager().givePlayerStarter(participant.getPlayer());
		}
		
		// Checks to see if all arena round have been complete or not.
		if (this.roundsComplete < this.rounds) {
			// Creates a new round.
			this.round = new Round(this);
			// Starts the round.
			this.round.start();
			
			// Copies the participants from the previous round to the new round.
			round.clone(this.round);
		} else {
			// Calls the method to indicate the arena has finished.
			this.arenaFinished(round.getParticipants());
		}
		
		// Updates the arena's item stack.
		this.updateItemStack();
	}
	
	/**
	 * A method which is called when the arena has finished.
	 * 
	 * @param participants	the participants in the arena
	 */
	public void arenaFinished(ArrayList<Participant> participants) {
		// Declares and initialises a list of winning participants.
		ArrayList<Participant> winningParticipants = new ArrayList<Participant>();
		// Declares and initialises the string of winning participants.
		String winningParticipantsConcatenated = "";
		
		// Creates a dummy participant which acts as participant that
		// the scores get compared against.
		Participant dummy = new Participant(null);
		dummy.setScore(-1);
		winningParticipants.add(dummy);
		
		// Loops through each participant in the list of participants.
		for (Participant participant : participants) {
			// Gets the first winning participant in the list.
			Participant winningParticipant = winningParticipants.get(0);
			
			// Checks to see if the participants score is larger than the
			// winning participant's score. If it is the same, then it will
			// add the participant to the list without clearing it, indicating
			// a draw.
			if (participant.getScore() > winningParticipant.getScore()) {
				// Clears the list of winning participants.
				winningParticipants.clear();
				// Adds the participant as a winning participant.
				winningParticipants.add(participant);
			} else if (participant.getScore() == winningParticipant.getScore()) {
				// Adds the participant as a winning participant.
				winningParticipants.add(participant);
			}
		}
		
		// A boolean which lets us know if we are on the first winning
		// participant.
		boolean first = true;
		
		for (Participant winningParticipant : winningParticipants) {
			// Check to see if this is the first winning participant.
			if (first) {
				// Set it to false as the next one will not be the first
				// winning participant.
				first = false;
			} else {
				// Concatenates a comma separator to the string of winning
				// participants.
				winningParticipantsConcatenated += ", ";
			}
			
			// Concatenates the participant's name to the string of winning
			// participants .
			winningParticipantsConcatenated += winningParticipant.getPlayer().getName();
		}
		
		// Loops through each participant in the list of participants.
		for (Participant participant : participants) {
			// A boolean which decides whether or not they won.
			boolean won = false;
			// A boolean which decides whether or not they drew.
			boolean draw = false;
			
			// Loops through each winning participant in the list of winning
			// participants.
			for (Participant winningParticipant : winningParticipants) {
				// Compares their names to see if they are the same.
				if (winningParticipant.getPlayer().getName().equals(participant.getPlayer().getName())) {
					// Decide that the participant has won.
					won = true;
				}
			}
			
			// Check to see if there are more than one winning participants.
			if (winningParticipants.size() > 1) {
				// Decide that the participant has drew.
				draw = true;
			}
			
			// Checks if the participant has drew.
			if (draw) {
				// Checks if the participant has won.
				if (won) {
					// Informs them that multiple players won.
					this.main.message(participant.getPlayer(),
							"Multiple people won: " 
									+ winningParticipantsConcatenated);
				} else {
					// Informs them that they lost, but multiple players won.
					this.main.message(participant.getPlayer(),
							"You lost. The winners were: " 
									+ winningParticipantsConcatenated);
				}
			} else {
				// Checks if the participant has won.
				if (won) {
					// Informs the participant that they won.
					this.main.message(participant.getPlayer(), "You won!");
				} else {
					// Informs the participant that they lost.
					this.main.message(participant.getPlayer(), "You lost. " 
							+ winningParticipantsConcatenated + " won.");
				}
			}
			
			// Sets the number of rounds complete to zero.
			this.roundsComplete = 0;
			
			// Teleports the player back to the world's spawn.
			participant.getPlayer().teleport(Bukkit.getServer().getWorld("world").getSpawnLocation());
			
			// Gives the player the server's starting equipment.
			this.main.getEquipmentManager().givePlayerStarter(participant.getPlayer());
		}
	}
	
	/**
	 * Queues a player for this arena.
	 * 
	 * @param player	the player to queue for this arena
	 */
	public void queuePlayer(Player player) {
		// Checks to ensure the arena is not disabled.
		if (this.isDisabled()) {
			return;
		}
		
		// Checks to ensure the player is not already in another queue.
		if (this.inQueue(player)) {
			return;
		}
		
		// Initialises the participant and adds them to the arena queue.
		this.queue.add(new Participant(player));
		// Updates the arena's item stack.
		this.updateItemStack();
		
		// Checks the queue to see if the arena can start.
		this.checkQueue();
	}
	
	/**
	 * Removes a player from the arena queue.
	 * 
	 * @param player	the player to remove from the arena queue.
	 */
	public void removePlayer(Player player) {
		// Checks to see if the player is in queue.
		if (this.inQueue(player)) {
			// Declare and initialise an iterator for the queue.
			Iterator<Participant> iterator = this.queue.iterator();
			
			// Loop while the iterator is iterating.
			while (iterator.hasNext()) {
				// Gets the queued player from the queue.
				Player queuedPlayer = iterator.next().getPlayer();
				
				// Check to see if their name is the name we are looking for.
				if (queuedPlayer.getName().equals(player.getName())) {
					// Remove the player from the queue.
					iterator.remove();
				}
			}
		}
		
		// Update the arena's item stack.
		this.updateItemStack();
	}
	
	/**
	 * Checks to see if a player is in queue.
	 * 
	 * @param player	the player to check
	 * @return		whether or not the player is in queue
	 */
	public boolean inQueue(Player player) {
		// Loop through each participant in the queue.
		for (Participant participant : this.queue) {
			// Check to see if the participant's name is the player we
			// are looking for.
			if (participant.getPlayer().getDisplayName().equals(player.getDisplayName())) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Checks to see if we can start the round.
	 */
	public void checkQueue() {
		// Ensures the arena is not disabled.
		if (this.isDisabled()) {
			return;
		}
		
		// Checks to see if we have enough players to start a new round.
		if (this.queue.size() >= this.playerCap) {
			// Makes sure there is not an on-going round.
			if (this.round == null) {
				// Start a new round.
				this.newRound();
			}
		}
	}
	
	/**
	 * Handles the entity death event and passes it to the current round.
	 * 
	 * @param entityDeathEvent	the entity death event
	 */
	public void onEntityDeath(EntityDeathEvent entityDeathEvent) {
		// Checks to ensure the current round isn't null.
		if (this.round == null) {
			return;
		}
		
		// Checks to ensure that the entity is a player.
		if (!(entityDeathEvent.getEntity() instanceof Player)) {
			return;
		}
		
		// Pass the event.
		this.round.onEntityDeath(entityDeathEvent);
	}
	
	/**
	 * Handles the player command pre-process event and passes it to the
	 * current round.
	 * 
	 * @param playerCommandPreprocessEvent	the player command pre-process event
	 */
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent playerCommandPreprocessEvent) {
		// Checks to ensure the current round isn't null.
		if (this.round == null) {
			return;
		}
		
		// Pass the event.
		this.round.onPlayerCommandPreprocess(playerCommandPreprocessEvent);
	}
	
	/**
	 * Handles the player pickup item event and passes it to the current round.
	 * 
	 * @param playerPickupItemEvent	the player pickup item event
	 */
	public void onPlayerPickupItem(PlayerPickupItemEvent playerPickupItemEvent) {
		// Checks to ensure the current round isn't null.
		if (this.round == null) {
			return;
		}
		
		// Pass the event.
		this.round.onPlayerPickupItem(playerPickupItemEvent);
	}
	
	/**
	 * Handles the player quit event and passes it to the current round.
	 * 
	 * @param playerQuitEvent	the player quit event
	 */
	public void onPlayerQuit(PlayerQuitEvent playerQuitEvent) {
		// Checks to ensure the current round isn't null.
		if (this.round == null) {
			return;
		}
		
		// Pass the event.
		this.round.onPlayerQuitEvent(playerQuitEvent);
	}
	
	/**
	 * Increments the number of completed rounds.
	 */
	public void incrementRoundsComplete() {
		this.roundsComplete++;
	}
	
	/**
	 * Gets the arena's name.
	 * 
	 * @return	the arena's name
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Gets the arena's item stack.
	 * 
	 * @return	the arena's item stack
	 */
	public ItemStack getItemStack() {
		return this.itemStack;
	}
	
	/**
	 * Gets the arena's locations.
	 * 
	 * @return	the arena's spawn locations
	 */
	public ArrayList<Location> getLocations() {
		return this.locations;
	}
	
	/**
	 * Gets the maximum number of players that the arena can support.
	 * 
	 * @return	the arena's player cap
	 */
	public int getPlayerCap() {
		return this.playerCap;
	}
	
	/**
	 * Gets whether or not the arena is disabled.
	 * 
	 * @return	whether or not the arena is disabled
	 */
	public boolean isDisabled() {
		return this.disabled;
	}
	
	/**
	 * Gets the queue for the arena.
	 * 
	 * @return	the arena's queue
	 */
	public ArrayList<Participant> getQueue() {
		return this.queue;
	}
	
	/**
	 * Gets the current arena round.
	 * 
	 * @return	the current arena round
	 */
	public Round getRound() {
		return this.round;
	}
	
	/**
	 * Gets the number of arena rounds.
	 * 
	 * @return	the number of arena rounds.
	 */
	public int getRounds() {
		return this.rounds;
	}
	
	/**
	 * Gets the number of arena rounds that have been completed so far.
	 * 
	 * @return	the number of completed arena rounds
	 */
	public int getRoundsComplete() {
		return this.roundsComplete;
	}
}
