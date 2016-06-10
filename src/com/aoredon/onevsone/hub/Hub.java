package com.aoredon.onevsone.hub;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.aoredon.onevsone.Main;

/**
 * A base class for the hubs. It has a material which is essentially the icon
 * that will be used for the hub in GUIs and a location which is the place
 * the player gets put when they teleport to that hub.
 * 
 * @author Alex Guest
 */
public class Hub {
	// The name of the hub.
	private String name;
	// The material for the hub which is used as the icon in GUIs.
	private Material material;
	// The item stack for the hub.
	private ItemStack itemStack;
	// The location to put the player in when they transfer to the HUB.
	private Location location;
	
	/**
	 * Takes in the name of the hub, the material which acts as the icon in
	 * GUIs and the location which is where the player is put to when they
	 * teleport to the hub.
	 * 
	 * @param name		the name of the hub
	 * @param material	the material which acts as the icon for the hub
	 * @param location	the location the player gets moved to in the hub
	 */
	public Hub(String name, Material material, Location location) {
		this.name = name;
		this.material = material;
		this.itemStack = this.initItemStack();
		this.location = location;
	}
	
	/**
	 * Creates the item stack for the hub.
	 * 
	 * @return
	 */
	public ItemStack initItemStack() {
		// Creates an item stack using the hub's material.
		ItemStack itemStack = new ItemStack(this.material, 1);
		// Sets the display name of the item stack to the hub's name.
		itemStack.getItemMeta().setDisplayName(ChatColor.BOLD + "" + Main.COLOUR + this.name);
		
		return itemStack;
	}
	
	/**
	 * Gets the hub's name.
	 * 
	 * @return	the hub's name.
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Gets the material used for the hub.
	 * 
	 * @return	the material used for the hub
	 */
	public Material getMaterial() {
		return this.material;
	}
	
	/**
	 * Gets the hub's item stack.
	 * 
	 * @return	the hub's item stack
	 */
	public ItemStack getItemStack() {
		return this.itemStack;
	}
	
	/**
	 * Gets the location that the player is put to inside of the hub upon
	 * transfer.
	 * 
	 * @return	the hub's location
	 */
	public Location getLocation() {
		return this.location;
	}
}