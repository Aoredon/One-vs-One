package com.aoredon.onevsone.equipment;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.aoredon.onevsone.Main;
import com.aoredon.onevsone.util.ItemManager;

/**
 * A simple class which manages equipment within the plugin. It offers a method
 * which allows you to give a player the default equipment for duelling, a method which
 * the gives a player the starter equipment which they receive when they join the server
 * and a method which allows you to clear a player's inventory.
 * 
 * @author Alex
 */
public class EquipmentManager {
	private Main main = Main.getInstance();
	
	/**
	 * Gives the specified player the default duelling equipment.
	 * 
	 * @param player	the player to give the equipment to
	 */
	public void givePlayerEquipment(Player player) {
		// Clears the player's inventory.
		this.clearInventory(player);
		
		// Gives the player items to duel with.
		player.getInventory().addItem(new ItemStack(Material.IRON_SWORD));
		player.getInventory().addItem(new ItemStack(Material.FISHING_ROD));
		player.getInventory().addItem(new ItemStack(Material.BOW));
		player.getInventory().addItem(new ItemStack(Material.FIREBALL, 2));
		player.getInventory().addItem(new ItemStack(Material.ARROW, 16));
		
		// Gives the player armour to duel with.
		player.getInventory().setHelmet(new ItemStack(Material.IRON_HELMET));
		player.getInventory().setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
		player.getInventory().setLeggings(new ItemStack(Material.IRON_LEGGINGS));
		player.getInventory().setBoots(new ItemStack(Material.IRON_BOOTS));
	}
	
	/**
	 * Gives the specified player the default starting equipment.
	 * 
	 * @param player	the player to give the equipmen to
	 */
	public void givePlayerStarter(Player player) {
		// Clears the player's inventory.
		this.clearInventory(player);
		
		// Sets the player's health to ten hearts.
		player.setHealth(20D);
		// Gives the player the menu item.
		player.getInventory().addItem(this.main.getItemManager().getAItem(ItemManager.MENU_ITEM)
				.createItemStack());
	}
	
	/**
	 * Clears the inventory of the specified player.
	 * 
	 * @param player	the player to clear the inventory of
	 */
	public void clearInventory(Player player) {
		// Clears the inventory of the player.
		player.getInventory().clear();
		
		// Clears the player's currently equipped armour.
		player.getInventory().setHelmet(new ItemStack(Material.AIR));
		player.getInventory().setChestplate(new ItemStack(Material.AIR));
		player.getInventory().setLeggings(new ItemStack(Material.AIR));
		player.getInventory().setBoots(new ItemStack(Material.AIR));
	}
}
