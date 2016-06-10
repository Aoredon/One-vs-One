package com.aoredon.onevsone.util;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.aoredon.onevsone.util.item.AItem;
import com.aoredon.onevsone.util.item.items.AItemMenu;

/**
 * This class manages the custom items that are utilised by the plugin. Items
 * extend an abstract item class and can then have custom actions and such.
 * 
 * This class also allows you to get a default 'blank' item which can be used as
 * a placeholder. If you need to compare two items, this class has a static method
 * which can be used to do so.
 * 
 * @author Alex Guest
 */
public class ItemManager implements Listener {
	public static final String MENU_ITEM = "menu";
	
	// Declares the instance for the blank item stack.
	private static ItemStack blankItem;
	// A list of items
	public ArrayList<AItem> items = new ArrayList<AItem>();
	
	/**
	 * Adds any items that need to be added to the item manager's
	 * list of items.
	 */
	public ItemManager() {
		this.items.add(new AItemMenu());
	}
	
	/**
	 * An event handler for the player's interaction event. This method will
	 * check to see if the item a player has used is one of the custom items.
	 * If it is, it will make the item execute its action.
	 * 
	 * @param playerInteractEvent	the player interaction event
	 */
	@EventHandler
	public void PlayerInteract(PlayerInteractEvent playerInteractEvent) {
		// Gets the player from the event.
		Player player = playerInteractEvent.getPlayer();
	
		// Loops through each item in the list of custom items.
		for (AItem aItem : this.items) {
			// Checks to see if the item used by the player is the same as the
			// current item that we're iterating over.
			if (ItemManager.compareItem(
					player.getInventory().getItemInMainHand(),
					aItem.createItemStack())) {
				// Execute the item's action and pass the event so that the
				// item can use it.
				aItem.itemAction(playerInteractEvent);
			}
		}
	}
	
	/**
	 * Returns the default for a blank item if it exists, otherwise it
	 * instantiates a blank item to be used as the default.
	 * 
	 * @return	the blank item instance
	 */
	public ItemStack getBlank() {
		// Checks to see if the blank item has been initialised.
		if (ItemManager.blankItem == null) {
			// Creates the blank item stack.
			ItemManager.blankItem = new ItemStack(Material.THIN_GLASS);
			// Ensures the blank item doesn't have a name.
			ItemManager.blankItem.getItemMeta().setDisplayName("");
		}
		
		return ItemManager.blankItem;
	}
	
	/**
	 * Gets an item from the item manager's list of items.
	 * 
	 * @param itemName	the name of the item to retrieve
	 * @return			an item from the item manager
	 */
	public AItem getAItem(String itemName) {
		// Loop through each item in the list of items.
		for (AItem aItem : this.items) {
			// Compares the item's name with the name of the item
			// that's being searched for.
			if (aItem.getItemName().equalsIgnoreCase(itemName)) {
				return aItem;
			}
		}
		
		return null;
	}
	
	/**
	 * Compares two item stacks to see if they are the same. Returns true if
	 * they are the same.
	 * 
	 * @param itemOne	the first item to compare
	 * @param itemTwo	the second item to compare
	 * @return			whether or not the items are the same
	 */
	public static boolean compareItem(ItemStack itemOne, ItemStack itemTwo) {
		return itemOne.equals(itemTwo);
	}
}