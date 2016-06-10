package com.aoredon.onevsone.gui;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import com.aoredon.onevsone.gui.guis.ArenaGui;
import com.aoredon.onevsone.gui.guis.HubGui;
import com.aoredon.onevsone.gui.guis.MenuGui;

/**
 * This class is the GUI manager which manages all of the graphical user
 * interfaces in the plugin. It keeps a single instance of GUI which can be
 * accessed via the GUI's identifier. The GUI manager also handles inventory
 * click events and passes them to the appropriate GUI.
 * 
 * @author Alex
 */
public class GuiManager implements Listener {
	// Constants for the GUI identifiers to reduce possible mistakes and to
	// allow identifiers to be changed easily.
	public static final int MENU_GUI = 1;
	public static final int HUB_GUI = 2;
	public static final int ARENA_GUI = 3;
	
	// Declares and intialises the array of GUIs.
	private ArrayList<Gui> guiScreens = new ArrayList<Gui>();
	
	/**
	 * A method called when the plugin is ready to initalise the GUIs.
	 */
	public void init() {
		// Adds the GUI screens to the list of GUIs.
		this.guiScreens.add(new MenuGui());
		this.guiScreens.add(new HubGui());
		this.guiScreens.add(new ArenaGui());
		
		// Loop through each GUI.
		for (Gui gui : this.guiScreens) {
			// Initalise the GUI.
			gui.init();
		}
	}
	
	/**
	 * An event handler for the inventory click event. This event is passed
	 * down to all GUIs in the list of GUIs.
	 * 
	 * @param inventoryClickEvent
	 */
	@EventHandler
	public void onInventoryClick(InventoryClickEvent inventoryClickEvent) {
		// Check to make sure the item clicked isn't null.
		if (inventoryClickEvent.getCurrentItem() == null) {
			return;
		}

		// Checked to make sure the item clicked isn't air.
		if (inventoryClickEvent.getCurrentItem().getType() == Material.AIR) {
			return;
		}
		
		// Loop through each GUI.
		for (Gui gui : this.guiScreens) {
			// Pass the event.
			gui.parseInventoryClick(inventoryClickEvent);
		}
	}
	
	/**
	 * Gets a GUI from the list of GUI's via its identifier.
	 * 
	 * @param id	the unique identifier of the gui
	 * @return		the gui corresponding to the identifier
	 */
	public Gui findGui(int id) {
		// Loop through each GUI.
		for (Gui gui : this.guiScreens) {
			// Checks to see if this is the GUI we are looking for.
			if (gui.getId() == id) {
				return gui;
			}
		}
		
		return null;
	}
}
