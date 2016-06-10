package com.aoredon.onevsone.util.item.items;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.aoredon.onevsone.Main;
import com.aoredon.onevsone.gui.GuiManager;
import com.aoredon.onevsone.util.ItemManager;
import com.aoredon.onevsone.util.item.AItem;

/**
 * The menu item is a custom item which allows the player to view the
 * plugin's menu.
 * 
 * @author Alex Guest
 */
public class AItemMenu extends AItem {
	/**
	 * Initialises the menu item.
	 */
	public AItemMenu() {
		super(ItemManager.MENU_ITEM);
	}

	/**
	 * Creates the menu item stack.
	 */
	@Override
	public ItemStack createItemStack() {
		// Creates the item stack.
		ItemStack itemStack = new ItemStack(Material.WATCH, 1);
		// Sets the item's name.
		itemStack.getItemMeta().setDisplayName(Main.COLOUR + "Menu");
		
		return itemStack;
	}

	/**
	 * Executes the menu item's action.
	 */
	@Override
	public void itemAction(PlayerInteractEvent playerInteractEvent) {
		// Gets the player from the event.
		Player player = playerInteractEvent.getPlayer();
		
		// Retrieves the menu GUI and displays it to the player.
		this.main.getGuiManger().findGui(GuiManager.MENU_GUI).showGUI(player);
	}
}
