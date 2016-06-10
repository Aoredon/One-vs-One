package com.aoredon.onevsone.gui.guis;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import com.aoredon.onevsone.Main;
import com.aoredon.onevsone.gui.Gui;
import com.aoredon.onevsone.gui.GuiManager;
import com.aoredon.onevsone.util.ItemManager;

/**
 * The GUI for the main menu.
 * 
 * @author Alex Guest
 */
public class MenuGui extends Gui {
	/**
	 * Sets up the menu GUI.
	 */
	public MenuGui() {
		super(GuiManager.MENU_GUI);
	}

	/**
	 * Initialises the menu GUI.
	 */
	@Override
	public void init() {
		// Sets up the inventory.
		this.setInventory(Bukkit.createInventory(null, 9, Main.COLOUR + "Select an Option"));

		// Sets up the buttons.
		this.getInventory().setItem(0, this.main.getItemManager().getBlank());
		this.getInventory().setItem(1, this.main.getItemManager().getBlank());
		this.getInventory().setItem(2, this.main.getItemManager().getBlank());
		this.getInventory().addItem(this.getHubItem());
		this.getInventory().setItem(4, this.main.getItemManager().getBlank());
		this.getInventory().addItem(this.getArenaItem());
		this.getInventory().setItem(6, this.main.getItemManager().getBlank());
		this.getInventory().setItem(7, this.main.getItemManager().getBlank());
		this.getInventory().setItem(8, this.main.getItemManager().getBlank());
	}
	
	/**
	 * Gets the hub menu item stack.
	 * 
	 * @return	the hub menu item stack
	 */
	public ItemStack getHubItem() {
		// Creates item stack for the hub item.
		ItemStack itemStack = new ItemStack(Material.BLAZE_POWDER);
		// Changes the name of the item stack.
		itemStack.getItemMeta().setDisplayName(Main.COLOUR + "Hubs");
		
		return itemStack;
	}
	
	/**
	 * Gets the arena menu item stack.
	 * 
	 * @return the arena menu item stack
	 */
	public ItemStack getArenaItem() {
		// Creates the item stack for the arena item.
		ItemStack itemStack = new ItemStack(Material.IRON_SWORD);
		// Changes the name of the item stack.
		itemStack.getItemMeta().setDisplayName(Main.COLOUR + "Arenas");
		
		return itemStack;
	}

	/**
	 * Handles the inventory click event and opens the specified
	 * GUIs when the appropriate item stacks are clicked on.
	 */
	@Override
	public void onInventoryClick(InventoryClickEvent inventoryClickEvent) {
		// Gets the player from the event.
		Player player = (Player) inventoryClickEvent.getWhoClicked();
		// Gets the item stack from the event.
		ItemStack itemStack = inventoryClickEvent.getCurrentItem();
		
		// Compares the item to see if it is the hub item stack.
		if (ItemManager.compareItem(this.getHubItem(), itemStack)) {
			// Opens the hub GUI.
			this.main.getGuiManger().findGui(GuiManager.HUB_GUI).
				showGUI(player);
		}
		
		// Compares the item to see if it is the arena item stack.
		if (ItemManager.compareItem(this.getArenaItem(), itemStack)) {
			// Opens the arena GUI.
			this.main.getGuiManger().findGui(GuiManager.ARENA_GUI).
				showGUI(player);
		}
	}
}
