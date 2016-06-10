package com.aoredon.onevsone.arena;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * A class which manages the arenas in the plugin. This class has a list which
 * holds all the available arenas. It handles all certain events and passes
 * those events down to the arenas.
 * 
 * @author Alex Guest
 */
public class ArenaManager implements Listener {
	// The list of arenas.
	private ArrayList<Arena> arenas = new ArrayList<Arena>();
	
	/**
	 * Initialises all of the available arenas.
	 */
	public ArenaManager() {
		this.arenas.add(new Arena("Beach", "A sandy arena.", Material.SAND, new ArrayList<Location>() {{
		    this.add(new Location(Bukkit.getServer().getWorld("world"), 264D, 51D, 1379D));
		    this.add(new Location(Bukkit.getServer().getWorld("world"), 221D, 51D, 1383D));
		}}, 2, 1));
		this.arenas.add(new Arena("Ruins", "A ruined arena.", Material.SANDSTONE, new ArrayList<Location>() {{
		    this.add(new Location(Bukkit.getServer().getWorld("world"), 284D, 46D, 1185D));
		    this.add(new Location(Bukkit.getServer().getWorld("world"), 242D, 46D, 1180D));
		}}, 2, 1));
		this.arenas.add(new Arena("Nether", "A very hot arena.", Material.NETHERRACK, new ArrayList<Location>() {{
		    this.add(new Location(Bukkit.getServer().getWorld("world"), 281D, 51D, 1326D));
		    this.add(new Location(Bukkit.getServer().getWorld("world"), 270D, 51D, 1370D));
		}}, 2, 1));
		this.arenas.add(new Arena("Frost", "A very cold arena.", Material.ICE, new ArrayList<Location>() {{
		    this.add(new Location(Bukkit.getServer().getWorld("world"), 212D, 51D, 1326D));
		    this.add(new Location(Bukkit.getServer().getWorld("world"), 212D, 51D, 1369D));
		}}, 2, 1));
		this.arenas.add(new Arena("Jungle", "A muddy arena.", Material.VINE, new ArrayList<Location>() {{
		    this.add(new Location(Bukkit.getServer().getWorld("world"), 229D, 47D, 1174D));
		    this.add(new Location(Bukkit.getServer().getWorld("world"), 229D, 47D, 1125D));
		}}, 2, 1));
		this.arenas.add(new Arena("City", "An urban arena.", Material.STONE, new ArrayList<Location>() {{
		    this.add(new Location(Bukkit.getServer().getWorld("world"), 266D, 50D, 1314D));
		    this.add(new Location(Bukkit.getServer().getWorld("world"), 219D, 50D, 1314D));
		}}, 2, 1));
		this.arenas.add(new Arena("Water", "A very wet arena.", Material.WATER_LILY, new ArrayList<Location>() {{
		    this.add(new Location(Bukkit.getServer().getWorld("world"), 292D, 47D, 1125D));
		    this.add(new Location(Bukkit.getServer().getWorld("world"), 302D, 47D, 1173D));
		}}, 2, 1));
	}
	
	/**
	 * Handles entity death and passes this event down to the arenas.
	 * 
	 * @param entityDeathEvent	the entity death event
	 */
	@EventHandler
	public void onEntityDeath(EntityDeathEvent entityDeathEvent) {
		// Loop through each arena.
		for (Arena arena : this.arenas) {
			// Pass the event.
			arena.onEntityDeath(entityDeathEvent);
		}
	}
	
	/**
	 * Handles the player command pre-process event and passes this event down
	 * to the arenas.
	 * 
	 * @param playerCommandPreprocessEvent	the player command pre-process event
	 */
	@EventHandler
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent playerCommandPreprocessEvent) {
		// Loop through each arena.
		for (Arena arena : this.arenas) {
			// Pass the event.
			arena.onPlayerCommandPreprocess(playerCommandPreprocessEvent);
		}
	}
	
	/**
	 * Handles the player pickup item event and passes this event down to the
	 * arenas.
	 * 
	 * @param playerPickupItemEvent	the player pickup item event
	 */
	@EventHandler
	public void onPlayerPickupItem(PlayerPickupItemEvent playerPickupItemEvent) {
		// Loop through each arena.
		for (Arena arena : this.arenas) {
			// Pass the event.
			arena.onPlayerPickupItem(playerPickupItemEvent);
		}
	}
	
	/**
	 * Handles the player quit event and passes this event down to the arenas.
	 * 
	 * @param playerQuitEvent	the player quit event
	 */
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent playerQuitEvent) {
		// Loop through each arena.
		for (Arena arena : this.arenas) {
			// Pass the event.
			arena.onPlayerQuit(playerQuitEvent);
		}
	}
	
	/**
	 * Loops through each arena in the list of arenas and returns the arena
	 * with the specified name if it exists.
	 * 
	 * @param name	the name of the arena to retrieve
	 * @return		an arena with the specified name
	 */
	public Arena getArena(String name) {
		// Loop through each arena.
		for (Arena arena : this.arenas) {
			// Compare the two arena names.
			if (arena.getName().equals(name)) {
				return arena;
			}
		}
		
		return null;
	}
	
	/**
	 * Gets the list of arenas.
	 * 
	 * @return	the list of arenas
	 */
	public ArrayList<Arena> getArenas() {
		return this.arenas;
	}
}