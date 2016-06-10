package com.aoredon.onevsone.gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import com.aoredon.onevsone.Main;
import com.aoredon.onevsone.util.ItemManager;

/**
 * An abstract class which allows for easy creation of inventory graphical
 * user interfaces.
 * 
 * @author Alex
 */
public abstract class Gui {
	// Allows children to easily access the main class.
	protected Main main = Main.getInstance();
	
	// Declares an inventory for the graphical user interface.
	private Inventory inventory;
	// Declares and initialises a default identifier for the interface.
	private int id = -1;

	/**
	 * Takes in an integer which serves as a way of quickly retrieving the
	 * interface from the graphical user interface manager.
	 * 
	 * @param id	the unique identifier for the interface
	 */
	public Gui(int id) {
		this.id = id;
	}
	
	/**
	 * A method called when the interface needs to be initialised.
	 * This is overridden by the child where it will initialise the
	 * inventory and set its contents appropriately.
	 */
	public abstract void init();
	
	/**
	 * Called whenever the interface needs to be updated. This will
	 * update the inventory of the interface to be 
	 */
	public void updateGui() {
		// Calls the method to initialise the interface.
		this.init();
		
		// Loops through each item in the inventory.
		for (int i = 0; i < this.getInventory().getContents().length; i++) {
			// Ensures the item is not null.
			if (this.getInventory().getContents()[i] != null) {
				// Sets the slot to the appropriate item from the array.
				this.getInventory().setItem(i, this.getInventory().getContents()[i]);
			}
		}
		
		// Loops through the online players.
		for (Player player : Bukkit.getServer().getOnlinePlayers()) {
			// Makes sure the player has an inventory screen open.
			if (player.getOpenInventory() == null) {
				continue;
			}
			
			// Make sure they have this inventory open.
			if (player.getOpenInventory().getTitle().equals(this.inventory.getTitle())) {
				// Update the player's inventory.
				player.updateInventory();
				// Open up this inventory again to ensure it updates.
				player.openInventory(this.getInventory());
			}
		}
	}
	
	/**
	 * Parses inventory click events for this GUI.
	 * 
	 * @param inventoryClickEvent	the inventory click event
	 */
	public void parseInventoryClick(InventoryClickEvent inventoryClickEvent) {
		// Checks to make sure the inventory in the event is this inventory.
		if (inventoryClickEvent.getInventory().getTitle().equals(this.inventory.getTitle())) {
			// Checks to see if they clicked a blank item.
			if (ItemManager.compareItem(this.main.getItemManager().getBlank(),
					inventoryClickEvent.getCurrentItem())) {
				// Opens the GUI again to prevent the player from taking anything.
				this.main.getGuiManger().findGui(GuiManager.MENU_GUI).
					showGUI((Player) inventoryClickEvent.getWhoClicked());
				return;
			}
			
			// Cancels the event to prevent the player from taking the item.
			inventoryClickEvent.setCancelled(true);
			
			// Updates the GUI.
			this.updateGui();
			
			// Passes the inventory click to the GUI.
			this.onInventoryClick(inventoryClickEvent);
		}
	}
	
	/**
	 * An abstract method which is overridden by the child class. This is
	 * called when the GUI is interacted with.
	 * 
	 * @param inventoryClickEvent
	 */
	public abstract void onInventoryClick(InventoryClickEvent inventoryClickEvent);
	
	/**
	 * Shows the GUI to the specified player.
	 * 
	 * @param player	the player to show the gui to
	 */
	public void showGUI(Player player) {
		// Opens the GUI for the player.
		player.openInventory(this.inventory);
	}
	
	/**
	 * Sets the GUI's inventory to the specified inventory.
	 * 
	 * @param inventory	the inventory to set the gui's inventory to
	 */
	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
	}
	
	/**
	 * Returns the GUI's inventory.
	 * 
	 * @return	the gui's inventory
	 */
	public Inventory getInventory() {
		return this.inventory;
	}
	
	/**
	 * Returns the identifier of the GUI.
	 * 
	 * @return	the gui's identifier
	 */
	public int getId() {
		return this.id;
	}
}