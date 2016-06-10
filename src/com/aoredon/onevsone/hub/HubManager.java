package com.aoredon.onevsone.hub;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;

/**
 * The plugin supports multiple hubs. This class manages the available hubs.
 * 
 * @author Alex Guest
 */
public class HubManager {
	// A list of available hubs.
	private ArrayList<Hub> hubs = new ArrayList<Hub>();
	
	/**
	 * Initialises the hubs.
	 */
	public HubManager() {
		this.hubs.add(new Hub("Hub 1", Material.WOOL, new Location(
				Bukkit.getServer().getWorld("world"), 242D, 53D, 1348D)));
		this.hubs.add(new Hub("Hub 2", Material.WOOL, new Location(
				Bukkit.getServer().getWorld("world"), 263D, 48D, 1149D)));
	}
	
	/**
	 * Gets the list of hubs.
	 * 
	 * @return	the list of hubs
	 */
	public ArrayList<Hub> getHubs() {
		return this.hubs;
	}
}