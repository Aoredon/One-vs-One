package com.aoredon.onevsone.gui.guis;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import com.aoredon.onevsone.Main;
import com.aoredon.onevsone.gui.Gui;
import com.aoredon.onevsone.gui.GuiManager;
import com.aoredon.onevsone.hub.Hub;
import com.aoredon.onevsone.util.ItemManager;

/**
 * A GUI for a menu which allows players to select a hub and subsequently
 * be teleported there.
 * 
 * @author Alex Guest
 */
public class HubGui extends Gui {
	/**
	 * Sets up the hub GUI.
	 */
	public HubGui() {
		super(GuiManager.HUB_GUI);
	}

	/**
	 * Initialises the hub GUI.
	 */
	@Override
	public void init() {
		// Sets up the inventory.
		this.setInventory(Bukkit.createInventory(null, 9, Main.COLOUR + "Select a Hub"));
		
		// Sets up the buttons.
		this.getInventory().setItem(0, this.main.getItemManager().getBlank());
		this.getInventory().setItem(1, this.main.getItemManager().getBlank());
		this.getInventory().setItem(2, this.main.getItemManager().getBlank());
		this.getInventory().addItem(this.main.getHubManager().getHubs().get(0).getItemStack());
		this.getInventory().setItem(4, this.main.getItemManager().getBlank());
		this.getInventory().addItem(this.main.getHubManager().getHubs().get(1).getItemStack());
		this.getInventory().setItem(6, this.main.getItemManager().getBlank());
		this.getInventory().setItem(7, this.main.getItemManager().getBlank());
		this.getInventory().setItem(8, this.main.getItemManager().getBlank());
	}

	/**
	 * Handles the inventory click event to take the player to the hub that
	 * they click on.
	 */
	@Override
	public void onInventoryClick(InventoryClickEvent inventoryClickEvent) {
		// Gets the player from the event.
		Player player = (Player) inventoryClickEvent.getWhoClicked();
		// Gets the item stack from the event.
		ItemStack itemStack = inventoryClickEvent.getCurrentItem();
		
		// Loop throgh each hub in the list of hubs.
		for (Hub hub : this.main.getHubManager().getHubs()) {
			// Compare the items to see if they are the same.
			if (ItemManager.compareItem(hub.getItemStack(), itemStack)) {
				// Teleport the player to the hub.
				player.teleport(hub.getLocation());
				// Close the player's inventory.
				player.closeInventory();
			}
		}
	}
}
