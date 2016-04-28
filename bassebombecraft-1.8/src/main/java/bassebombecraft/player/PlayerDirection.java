package bassebombecraft.player;

/**
 * Enumeration defining player compass direction.
 */
public enum PlayerDirection {

	South, West, North, East;	
	
	public static PlayerDirection getById(int side) {
		return values()[side];
	}	
	
}
