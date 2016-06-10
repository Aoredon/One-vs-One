package com.aoredon.onevsone.util.item;

import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.aoredon.onevsone.Main;

/**
 * An abstract class which allows for custom items to be easily implemented.
 * 
 * @author Alex Guest
 */
public abstract class AItem {
	// Allows children to easily access the main class.
	protected Main main = Main.getInstance();
	
	// The name of the item.
	private String itemName;
	// The item stack for the item.
	private ItemStack itemStack;
	
	/**
	 * Sets the name of the item to the specified string. This is usually
	 * set by the child class.
	 * @param itemName
	 */
	public AItem(String itemName) {
		this.itemName = itemName;
		this.itemStack = this.createItemStack();
	}
	
	/**
	 * An abstract method which is overridden by the child class to allow you
	 * to create the item stack for the custom item.
	 * 
	 * @return	the item stack that will be used for the custom item
	 */
	public abstract ItemStack createItemStack();
	
	/**
	 * This is an abstract method which is called by the item manager whenever
	 * the item needs to do its custom action.
	 * 
	 * @param playerInteractEvent	the player interaction event
	 */
	public abstract void itemAction(PlayerInteractEvent playerInteractEvent);
	
	/**
	 * Gets the item's name.
	 * 
	 * @return	the item's name
	 */
	public String getItemName() {
		return this.itemName;
	}
	
	/**
	 * Get's the item's stack.
	 * 
	 * @return	the item's stack
	 */
	public ItemStack getItemStack() {
		return this.itemStack;
	}
}