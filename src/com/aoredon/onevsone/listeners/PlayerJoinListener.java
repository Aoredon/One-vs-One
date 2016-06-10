package com.aoredon.onevsone.listeners;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.aoredon.onevsone.Main;

/**
 * A listener which listens specifically for the player join event. When a
 * player joins this ensures that they are in a healthy state in the correct
 * position with the right items.
 * 
 * @author Alex Guest
 */
public class PlayerJoinListener implements Listener {
	// Allows for quick access to the instance of the main class.
	private Main main = Main.getInstance();

	/**
	 * Handles the player join event, ensuring they are healthy, in the correct
	 * position and have the right items.
	 * 
	 * @param playerJoinEvent	the player join event
	 */
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent playerJoinEvent) {
		// Gets the player from the event.
		Player player = playerJoinEvent.getPlayer();
		
		// Sets the player's health to ten hearts.
		player.setHealth(20D);
		// Makes sure the player is not on fire.
		player.setFireTicks(0);
		// Makes sure the player is well fed.
		player.setFoodLevel(20);
		// Makes sure the player is not in creative.
		player.setGameMode(GameMode.SURVIVAL);
		
		// Teleport the player to the world spawn.
		player.teleport(Bukkit.getServer().getWorld("world").getSpawnLocation());
		
		// Clears the player's inventory.
		this.main.getEquipmentManager().clearInventory(player);
		// Gives the player the starter equipment.
		this.main.getEquipmentManager().givePlayerStarter(playerJoinEvent.getPlayer());
	}
}
