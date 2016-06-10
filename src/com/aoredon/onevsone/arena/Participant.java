package com.aoredon.onevsone.arena;

import org.bukkit.entity.Player;

/**
 * A participant in an arena. This class keeps track of the player's score
 * and whether or not they are currently dead.
 * 
 * @author Alex
 */
public class Participant {
	// The player to keep track of.
	private Player player;
	// The player's score.
	private int score;
	// Whether or not the player is dead.
	private boolean dead;
	
	/**
	 * Creates a participant from the specified player.
	 * 
	 * @param player	the player to create the participant from
	 */
	public Participant(Player player) {
		this.player = player;
	}
	
	/**
	 * Increments the participant's score.
	 * 
	 * @param score	the amount to increment the score by
	 */
	public void incrementScore(int score) {
		this.score += score;
	}
	
	/**
	 * Gets the player from the participant.
	 * 
	 * @return	the player
	 */
	public Player getPlayer() {
		return this.player;
	}
	
	/**
	 * Gets the player's score.
	 * 
	 * @return	the player's score
	 */
	public int getScore() {
		return this.score;
	}
	
	/**
	 * Gets whether or not the player is dead.
	 * 
	 * @return	whether or not the player is dead
	 */
	public boolean isDead() {
		return this.dead;
	}
	
	/**
	 * Sets the player's score to the specified value.
	 * 
	 * @param score	the score to set the player's score to
	 */
	public void setScore(int score) {
		this.score = score;
	}
	
	/**
	 * Sets whether or not the player is dead.
	 * 
	 * @param dead	whether or not the player is dead
	 */
	public void setDead(boolean dead) {
		this.dead = dead;
	}
}