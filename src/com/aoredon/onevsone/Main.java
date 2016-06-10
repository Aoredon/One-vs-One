package com.aoredon.onevsone;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.aoredon.onevsone.arena.ArenaManager;
import com.aoredon.onevsone.equipment.EquipmentManager;
import com.aoredon.onevsone.gui.GuiManager;
import com.aoredon.onevsone.hub.HubManager;
import com.aoredon.onevsone.listeners.PlayerJoinListener;
import com.aoredon.onevsone.listeners.PlayerRespawnListener;
import com.aoredon.onevsone.util.ItemManager;

/**
 * One vs One is a Minecraft plugin which allows players to duel each other in
 * a controlled environment.
 * 
 * The players can queue for specific arenas. When the players get put in the
 * arena they are both given the same equipment so that the fight is fair.
 * 
 * The players do not lose anything from the duel and so they can freely
 * duel whoever they want without worry.
 * 
 * This plugin was made for servers which will be solely for dueling by
 * request. As such you cannot run the plugin on a server alongside
 * something else such as vanilla Minecraft.
 * 
 * @author Alex Guest
 */
public class Main extends JavaPlugin {
	// The chat colour that will be used for messages from this plugin.
	public static ChatColor COLOUR = ChatColor.DARK_PURPLE;
	// The text that will prefix all messages from this plugin.
	public static String PREFIX = "[" + Main.COLOUR + ChatColor.BOLD 
			+ "Mist" +  ChatColor.RESET + "]: " + ChatColor.GRAY;
	
	// An instance of this class.
	private static Main mainInstance;
	// An instance of the hub manager.
	private HubManager hubManager;
	// An instance of the arena manager.
	private ArenaManager arenaManager;
	// An instance of the item manager.
	private ItemManager itemManager;
	// An instance of the GUI manager.
	private GuiManager guiManager;
	// An instance of the equipment manager.
	private EquipmentManager equipmentManager;

	/**
	 * Called when the plugin is enabled. This: initialises the singleton
	 * and the managers; registers the event listeners and sets up the players
	 * appropriately.
	 */
	public void onEnable() {
		// Initialises the main instance.
		Main.mainInstance = this;
		
		// Initialises the various managers.
		this.hubManager = new HubManager();
		this.arenaManager = new ArenaManager();
		this.itemManager = new ItemManager();
		this.guiManager = new GuiManager();
		this.equipmentManager = new EquipmentManager();
		
		// Calls the initialise function for the GUI manager after all other
		// managers have been initialised.
		this.guiManager.init();
		
		// Registers event listeners.
		Bukkit.getPluginManager().registerEvents(this.arenaManager, this);
		Bukkit.getPluginManager().registerEvents(this.itemManager, this);
		Bukkit.getPluginManager().registerEvents(this.guiManager, this);
		Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), this);
		Bukkit.getPluginManager().registerEvents(new PlayerRespawnListener(), this);
		
		// Loops through each online player.
		for (Player player : Bukkit.getServer().getOnlinePlayers()) {
			// Teleports them to the world's spawn location.
			player.teleport(Bukkit.getServer().getWorld("world").getSpawnLocation());
			// Clears their inventory.
			player.getInventory().clear();
			// Gives the player the plugin's starter equipment.
			this.getEquipmentManager().givePlayerStarter(player);
		}
	}
	
	/**
	 * Called when the plugin is disabled. This is not yet used.
	 */
	public void onDisable() {
		
	}
	
	/**
	 * Sends a message to the specified player.
	 * 
	 * @param player	the player to send the message to
	 * @param message	the message to send
	 */
	public void message(Player player, String message) {
		player.sendMessage(Main.PREFIX + message);
	}
	
	/**
	 * Sends an error message to the specified player.
	 * 
	 * @param player	the player to send the error message to
	 * @param message	the error message to send
	 */
	public void error(Player player, String message) {
		player.sendMessage(Main.PREFIX + ChatColor.RED + "[Error] " + ChatColor.GRAY + message);
	}
	
	/**
	 * Sends a message to all online server operators.
	 * 
	 * @param message	the message to send
	 */
	public void opMessage(String message) {
		// Loop through each online player.
		for (Player player : Bukkit.getServer().getOnlinePlayers()) {
			// Check if they are an operator.
			if (player.isOp()) {
				player.sendMessage(Main.PREFIX + message);
			}
		}
	}
	
	/**
	 * Sends a detailed error message to all online server operators
	 * and then generates an error log. This is typically only used
	 * for more severe errors.
	 * 
	 * @param exception	the exception to log
	 */
	public void opMessage(Exception exception) {
		// Loop through each online player.
		for (Player player : Bukkit.getServer().getOnlinePlayers()) {
			// Check if they are an operator.
			if (player.isOp()) {
				// Generates the file name for the error log.
				String fileName = "error-log-" +
						new SimpleDateFormat("dd.MM.yy").format(new Date())
						+ "(" + System.nanoTime() + ").txt";
				// Initialises the file.
				File errorFile = new File(this.getDataFolder() + File.separator + "errors" + File.separator + fileName);
				// Loads the file as a YAML configuration file.
				YamlConfiguration errorConfiguration = YamlConfiguration.loadConfiguration(errorFile);
				
				// Messages the operator to inform them that an error has
				// occurred.
				player.sendMessage(Main.COLOUR + "Stack Trace");
				
				// Loops through each stack trace element entry in the stack
				// trace.
				for (int i = 0; i < exception.getStackTrace().length; i++) {
					// Sends the stack trace element to the operator.
					player.sendMessage(Main.COLOUR + "" + i + ")" +
							ChatColor.RESET + exception.getStackTrace()[i]);
					// Logs the stack trace element in the error log.
					errorConfiguration.set("error.stacktrace." + i, "" +
							exception.getStackTrace()[i]);
				}
				
				// Sends the operator the simple class name of the exception.
				player.sendMessage(Main.COLOUR + "Name");
				player.sendMessage(exception.getClass().getSimpleName());
				// Logs the simple class name of the exception.
				errorConfiguration.set("error.name", exception.getClass().getSimpleName());
				
				// Sends the operator the exception's message.
				player.sendMessage(Main.COLOUR + "Normal Message");
				player.sendMessage(exception.getMessage());
				// Logs the exception's message.
				errorConfiguration.set("error.localizedmessage", exception.getMessage());
				
				// Sends the operator the exception's localised message.
				player.sendMessage(Main.COLOUR + "Localized Message");
				player.sendMessage(exception.getLocalizedMessage());
				// Logs the exception's localised message.
				errorConfiguration.set("error.normalmessage", exception.getLocalizedMessage());
				
				// Lets the operator know where the error log was saved to.
				player.sendMessage(Main.COLOUR + "Error Saved As");
				player.sendMessage(Main.COLOUR + fileName);
				
				try {
					// Attempts to save the file.
					errorConfiguration.save(errorFile);
				} catch (IOException ioException) {
					// Prints a stack trace if an input/output error occurs.
					ioException.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Gets the instance of the main class.
	 * 
	 * @return	the instance of the main class
	 */
	public static Main getInstance() {
		return mainInstance;
	}
	
	/**
	 * Gets the instance of the hub manager.
	 * 
	 * @return	the instance of the hub manager
	 */
	public HubManager getHubManager() {
		return this.hubManager;
	}
	
	/**
	 * Gets the instance of the arena manager.
	 * 
	 * @return	the instance of the arena manager
	 */
	public ArenaManager getArenaManager() {
		return this.arenaManager;
	}
	
	/**
	 * Gets the instance of the item manager.
	 * 
	 * @return	the instance of the item manager
	 */
	public ItemManager getItemManager() {
		return this.itemManager;
	}
	
	/**
	 * Gets the instance of the graphical user interface manager.
	 * 
	 * @return	the instance of the graphical user interface manager
	 */
	public GuiManager getGuiManger() {
		return this.guiManager;
	}
	
	/**
	 * Gets the instance of the equipment manager.
	 * 
	 * @return	the instance of the equipment manager
	 */
	public EquipmentManager getEquipmentManager() {
		return this.equipmentManager;
	}
}