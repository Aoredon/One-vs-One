package com.aoredon.onevsone.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import com.aoredon.onevsone.Main;

/**
 * A listener which listens specifically for player respawns. When the player
 * is respawned, it ensures they respawn in the correct location with the
 * correct items.
 * 
 * @author Alex Guest
 */
public class PlayerRespawnListener implements Listener {
	// Allows for quick access to the instance of the main class.
	private Main main = Main.getInstance();

	/**
	 * Handles the player respawn event, positioning them correctly and
	 * ensuring they have the correct items.
	 * 
	 * @param playerRespawnEvent	the player respawn event
	 */
	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent playerRespawnEvent) {
		// Gets the player from the event.
		Player player = playerRespawnEvent.getPlayer();
		
		// Teleports the player to the world spawn.
		player.teleport(Bukkit.getServer().getWorld("world").
				getSpawnLocation());
		// Clears their inventory.
		this.main.getEquipmentManager().clearInventory(player);
		
		// Gives the player the starter equipment.
		this.main.getEquipmentManager().givePlayerStarter(player);
	}
}