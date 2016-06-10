package com.aoredon.onevsone.gui.guis;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.aoredon.onevsone.Main;
import com.aoredon.onevsone.arena.Arena;
import com.aoredon.onevsone.arena.Participant;
import com.aoredon.onevsone.gui.Gui;
import com.aoredon.onevsone.gui.GuiManager;

/**
 * A GUI which allows players to browse a list of arenas and
 * queue for them.
 * 
 * @author Alex Guest
 */
public class ArenaGui extends Gui {
	/**
	 * Sets up the arena GUI.
	 */
	public ArenaGui() {
		super(GuiManager.ARENA_GUI);
	}

	/**
	 * Initialises the arena GUI.
	 */
	@Override
	public void init() {
		// Sets up the inventory.
		this.setInventory(Bukkit.createInventory(null, 9, Main.COLOUR + "Select an Arena"));

		// Sets up the buttons.
		this.getInventory().setItem(0, this.main.getItemManager().getBlank());
		for (int i = 0; i < this.main.getArenaManager().getArenas().size(); i++) {
			this.getInventory().addItem(this.main.getArenaManager().getArenas().get(i).getItemStack());
		}
		this.getInventory().setItem(8, this.main.getItemManager().getBlank());
	}

	/**
	 * Handles the inventory click event and allows users to queue for an arena
	 * when they click the appropriate item stack.
	 */
	@Override
	public void onInventoryClick(InventoryClickEvent inventoryClickEvent) {
		// Gets the player from the event.
		Player player = (Player) inventoryClickEvent.getWhoClicked();
		// Gets the item stack from the event.
		ItemStack itemStack = inventoryClickEvent.getCurrentItem();
		// Gets the item meta from the item stack.
		ItemMeta itemMeta = itemStack.getItemMeta();
		
		// Loops through each arena in the list of arenas.
		for (Arena arena : this.main.getArenaManager().getArenas()) {
			// Checks to see if the player is queued for this arena.
			if (arena.inQueue(player)) {
				// Remove the player from the arena queue.
				arena.removePlayer(player);
				// inform them of this change.
				this.main.message(player, "You have been removed from your previous queue.");
				
				continue;
			}
			
			// Checks to see if the arena has an on-going round.
			if (arena.getRound() != null) {
				// Loops through each participant in the arena.
				for (Participant participant : arena.getRound().getParticipants()) {
					// Checks to see if the participant is the player.
					if (participant.getPlayer().getName().equals(player.getName())) {
						// Informs the player that they cannot join a queue
						// whilst they are already in an arena.
						this.main.error(player,
								"Sorry, currently you may not queue for an arena while you are in one.");
						
						return;
					}
				}
			}
		}

		// Ensures any exceptions encountered are caught.
		try {
			// Gets the arena that was clicked on from the arena manager.
			Arena arena = this.main.getArenaManager().getArena(ChatColor.stripColor(itemMeta.getDisplayName()));
			
			// Checks to see if the arena is null.
			if (arena == null) {
				// Inform the player that the arena does not exist.
				this.main.error(player, "Sorry an error occured. That arena does not exist.");
				
				return;
			}
			
			// Checks to see if the arena is disabled.
			if (arena.isDisabled()) {
				// Inform the player that the arena is disabled.
				this.main.error(player, "The following arena is disabled: " +  arena.getName());
			} else {
				// Queue the player for this arena.
				arena.queuePlayer(player);
				// Inform the player that they have been queued for the arena.
				this.main.message(player, "You have selected the followng arena: " +  arena.getName());
			}
		} catch (Exception exception) {
			this.main.opMessage(exception);
		}
	}
}
