package com.aoredon.onevsone.util;

import java.util.ArrayList;

import org.bukkit.entity.Player;

import com.aoredon.onevsone.Main;

/**
 * A class which allows for stylised paragraphs to be built with ease.
 * The paragraph builder allows for information to be display to the player
 * in a user-friendly format.
 * 
 * @author Alex Guest
 */
public class ParagraphBuilder {
	// The constant for the prefix of the header.
	public static final String HEADER_PREFIX = "--[ ";
	// The constant for the suffix of the header.
	public static final String HEADER_SUFFIX = " ]--";
	
	// The constant for the suffix for the commands.
	public static final String COMMAND_NAME_SUFFIX = "/";
	// The constant for the seperator that sperates the parameters in commands.
	public static final String COMMAND_PARAMATER_SEPARATOR = ",";
	
	// The prefix for the command usage.
	public static final String COMMAND_USAGE_SUFFIX = "[";
	// The suffix for the command usage.
	public static final String COMMAND_USAGE_PREFIX = "]";
	
	// The lines in the paragraph.
	private ArrayList<String> paragraphLines = new ArrayList<String>();
	
	/**
	 * Adds a header element to the paragraph.
	 * 
	 * @param text	the text to be displayed on the header.
	 */
	public void addHeader(String text) {
		// Adds a header element to the paragraph lines.
		this.paragraphLines.add(Main.PREFIX 
				+ ParagraphBuilder.HEADER_PREFIX + text +
				ParagraphBuilder.HEADER_SUFFIX);
	}
	
	/**
	 * Adds a text element to the paragraph
	 * 
	 * @param text	the text to be added to the paragraph
	 */
	public void addText(String text) {
		// Adds a text element to the paragraph lines.
		this.paragraphLines.add(Main.PREFIX  + text);
	}
	
	/**
	 * Adds a command element to the paragraph lines.
	 * 
	 * @param commandName		the name of the command
	 * @param commandParamaters	a string array of the command's parameters
	 */
	public void addCommand(String commandName, String[] commandParamaters) {
		// Adds the command element to the paragraph lines.
		this.paragraphLines.add(Main.PREFIX +
				ParagraphBuilder.COMMAND_NAME_SUFFIX 
				+ commandName + " " +
				this.concatenateCommandParameters(commandParamaters) + " ");
	}
	
	/**
	 * Concatenates the paragraph's concatenate.
	 * 
	 * @param commandParamaters	the parameters to concatenate
	 * @return					the concatenated parameters
	 */
	public String concatenateCommandParameters(String[] commandParamaters) {
		// Declares and initialises the string for the end result.
		String concatenatedCommandParamaters = "";
		// A boolean to keep track of whether or not we are on the first
		// parameter in the array.
		boolean first = true;
		
		// Loops through each command parameter.
		for (int i = 0; i < commandParamaters.length; i++) {
			// Checks to see if we are on the first command parameter.
			if (first) {
				// Concatenates the parameter to the string without the
				// separator.
				concatenatedCommandParamaters += commandParamaters[i];
				// Sets first to false as we are no longer on the first
				// command parameter.
				first = false;
			} else {
				// Concatenates the parameter to the string with the
				// separator.
				concatenatedCommandParamaters +=
						ParagraphBuilder.COMMAND_PARAMATER_SEPARATOR + " " 
						+ commandParamaters[i];
			}
		}
		
		// Returns the concatenated command parameters.
		return concatenatedCommandParamaters;
	}
	
	/**
	 * Sends the constructed paragraph to the specified player.
	 * 
	 * @param player	the player to send the paragraph to
	 */
	public void messagePlayer(Player player) {
		// Loops through each line in the paragraph.
		for (String string : this.paragraphLines) {
			// Sends the line to the player as a message.
			player.sendMessage(string);
		}
	}
}