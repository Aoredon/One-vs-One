package com.aoredon.onevsone.arena;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.aoredon.onevsone.Main;

/**
 * This class handles the players while they are fighting. It has a countdown before
 * the round starts, it prevents players from using commands inside the arena and it
 * keeps track of when the player's have died. Once the round has finished, it will
 * notify the arena.
 * 
 * @author Alex Guest
 */
public class Round {
	// The identifier of the repeating task.
	private int taskID;
	
	// The arena this round belongs to.
	private Arena arena;
	// The participants in this round.
	private ArrayList<Participant> participants = new ArrayList<Participant>();
	// The spawn locations in the round.
	private ArrayList<Location> locations = new ArrayList<Location>();

	/**
	 * Takes in the arena that this round belongs to.
	 * 
	 * @param arena	the parent arena
	 */
	public Round(Arena arena) {
		this.arena = arena;

		// Loop through each arena location.
		for (Location location : this.arena.getLocations()) {
			// Add the location to this round.
			this.locations.add(location);
		}
	}

	/**
	 * A method that starts the count down for the arena.
	 */
	public void start() {
		// Creates a new repeating task.
		this.taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(
				Main.getInstance(),new Runnable() {
			// The time in seconds before the round starts.
			int timer = 5;

			/**
			 * A method called by the thread.
			 */
			@Override
			public void run() {
				// Checks to make sure the timer is larger than zero.
				if (timer > 0) {
					// Loop through each participant.
					for (Participant participant : participants) {
						// Initialise and declare the text to send to the
						// participants.
						String text = "second";

						// Make sure the timer is larger than one before adding
						// plural characters.
						if (timer > 1) {
							// Concatenate the plural character to the text.
							text += "s";
						}
						
						// Sends the message to the player.
						Main.getInstance().message(participant.getPlayer(),
								timer + " " + text 
									+ " before the round starts!");
					}

					// Decrement the timer.
					timer--;
				} else {
					// Stop the timer.
					timer = -1;
					// Call the method to finish the count down.
					finish();
					// Cancel the repeating task.
					Bukkit.getServer().getScheduler().cancelTask(taskID);
				}
			}
		}, 20L, 20L);
	}

	/**
	 * Called when the count down is finished to start the round.
	 */
	public void finish() {
		// Catches any exceptions.
		try {
			// Loop through each participant in the round.
			for (Participant participant : this.participants) {
				// Checks to make sure the participant's player is not null.
				if (participant.getPlayer() == null) {
					continue;
				}
				
				// Checks to see if the participant's player is dead.
				if (participant.getPlayer().isDead()) {
					// Sets the participant to dead.
					participant.setDead(true);
					
					continue;
				}

				// Chooses a random location from the list.
				int randomLocation = new Random().nextInt(
						this.locations.size());
				
				// Sends a message to the player to let them know the round
				// has started.
				Main.getInstance().message(participant.getPlayer(),
						"The round has begun!");
				
				// Teleport the participant to their spawn location.
				participant.getPlayer().teleport(
						this.locations.get(randomLocation));
				// Remove the location from the list.
				this.locations.remove(randomLocation);
				
				// Sets the player's health to ten hearts.
				participant.getPlayer().setHealth(20D);
				
				// Gives the player the dueling equipment.
				Main.getInstance().getEquipmentManager().givePlayerEquipment(participant.getPlayer());
			}
		} catch (Exception exception) {
			Main.getInstance().opMessage(exception);
		}
	}

	/**
	 * Handles the entity death event. Increments the participants score when
	 * they kill the opposing participant and makes sure they don't drop items.
	 * After which it teleports them to the world spawn.
	 * 
	 * @param entityDeathEvent	the entity death event
	 */
	public void onEntityDeath(EntityDeathEvent entityDeathEvent) {
		// Gets the players from the event.
		Player player = (Player) entityDeathEvent.getEntity();
		// Gets the killer from the event.
		Player killer = entityDeathEvent.getEntity().getKiller();

		// Clears the player's inventory.
		Main.getInstance().getEquipmentManager().clearInventory(player);
		// Clears the drops from the event.
		entityDeathEvent.getDrops().clear();

		// Loop through each participants in the round.
		for (Participant participant : this.participants) {
			// Checks to see if the participant is the victim.
			if (participant.getPlayer().getName().equals(player.getName())) {
				// Sets the participant to dead.
				participant.setDead(true);
				// Checks to see if the round should finish.
				this.checkFinish();
				
				return;
			}
			
			// Makes sure the killer is not null.
			if (killer == null) {
				continue;
			}

			// Checks to see if the participant is the killer.
			if (participant.getPlayer().getName().equals(killer.getName())) {
				// Increments the participant's score.
				participant.incrementScore(1);
			}
		}
		
		// Sets the player's health to ten hearts.
		player.setHealth(20D);
		// Ensures the player is not on fire.
		player.setFireTicks(0);
		// Ensures the player is fed.
		player.setFoodLevel(20);
		// Makes sure the player is in survival.
		player.setGameMode(GameMode.SURVIVAL);
		
		// Teleports the player to the spawn.
		player.teleport(Bukkit.getServer().getWorld("world").
				getSpawnLocation());
	}

	/**
	 * Handles the player command pre-process event to ensure that the player
	 * doesn't use any commands inside of the arena.
	 * 
	 * @param playerCommandPreprocessEvent	the player command pre-process event
	 */
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent playerCommandPreprocessEvent) {
		// Gets the player from the event.
		Player player = playerCommandPreprocessEvent.getPlayer();
		
		// Loops through each participant in the round.
		for (Participant participant : this.participants) {
			// Checks to see if the player from the event is a participant in
			// this arena.
			if (participant.getPlayer().getName().equals(player.getName())) {
				// Messages the player to inform them that they cannot use
				// commands whilst in an arena.
				Main.getInstance().message(player, "You may not use commands in an arena.");
				// Cancels the event.
				playerCommandPreprocessEvent.setCancelled(true);
				
				return;
			}
		}
	}

	/**
	 * Handles the player pickup item event to prevent them from picking up
	 * items in the arena.
	 * 
	 * @param playerPickupItemEvent
	 */
	public void onPlayerPickupItem(PlayerPickupItemEvent playerPickupItemEvent) {
		// Gets the player from the event.
		Player player = playerPickupItemEvent.getPlayer();
		
		// Loop through each participant in the round.
		for (Participant participant : this.participants) {
			// Checks to see if the player from the event is a participant in
			// this arena.
			if (participant.getPlayer().getName().equals(player.getName())) {
				// Cancel the event.
				playerPickupItemEvent.setCancelled(true);
				return;
			}
		}
	}

	public void onPlayerQuitEvent(PlayerQuitEvent playerQuitEvent) {
		// Gets the player from the event.
		Player player = playerQuitEvent.getPlayer();
		
		// Loop through each participant in the round.
		for (Participant participant : this.participants) {
			// Checks to see if the player from the event is a participant in
			// this arena.
			if (participant.getPlayer().getName().equals(player.getName())) {
				// Sets the participant to dead.
				participant.setDead(true);
				// Checks to see if the round should finish.
				this.checkFinish();
				
				return;
			}
		}
	}

	/**
	 * A method used to check to see if the round should finish.
	 */
	public void checkFinish() {
		// Declares and initialises a variable to count the number of players
		// that are alive still.
		int numberAlive = 0;

		// Keeps track of the last living participant.
		Participant lastLivingParticipant = null;
		
		// Loop through each participant in the round.
		for (Participant participant : this.participants) {
			// Check to see if they are not dead.
			if (participant.isDead() == false) {
				// Increment the number of participants that are still alive.
				numberAlive++;
			} else {
				lastLivingParticipant = participant;
			}
		}

		// Check to see if there is only one participant alive.
		if (numberAlive == 1) {
			// Check to see if all rounds have finished.
			if (this.arena.getRoundsComplete() == this.arena.getRounds()) {
				// Kill the last living participant.
				lastLivingParticipant.getPlayer().setHealth(0D);
			}
			
			// Tell the arena this round has finished.
			this.arena.roundFinished(this);
			
			return;
		}
		
		if (lastLivingParticipant == null) {
			// Tell the arena this round has finished.
			this.arena.roundFinished(this);
			
			return;
		}
	}

	/**
	 * Copies participants from this round to the specified round.
	 * 
	 * @param round	the round to copy the participants to
	 */
	public void clone(Round round) {
		// Loop through each participant in the round.
		for (Participant participant : this.getParticipants()) {
			// Sets the particpant to alive.
			participant.setDead(false);
			// Adds the participant to the round.
			round.addParticipant(participant);
		}
	}

	/**
	 * Adds a participant to the round.
	 * 
	 * @param participant	the participant to add
	 */
	public void addParticipant(Participant participant) {
		this.participants.add(participant);
	}

	/**
	 * Removes a player from the round.
	 * 
	 * @param player	the player to remove
	 */
	public void removePlayer(Player player) {
		// Loops through each participant in the list of particpants.
		for (int i = 0; i < this.participants.size(); i++) {
			// Gets the player from the participant.
			Player roundPlayer = this.participants.get(i).getPlayer();

			// Checks to see if the current player's name is the same as
			// the one we are looking for.
			if (player.getName().equals(roundPlayer.getName())) {
				// Remove the participant.
				this.participants.remove(i);
			}
		}
	}

	/**
	 * Clears all the participants from the round.
	 */
	public void clearPlayers() {
		this.participants.clear();
	}

	/**
	 * Gets the participants from the round.
	 * 
	 * @return	the round participants
	 */
	public ArrayList<Participant> getParticipants() {
		return this.participants;
	}
}
