package bassebombecraft.client.screen;

/**
 * Interface for generating advice for using the item.
 */
public interface ItemAdviceGenerator {

	/**
	 * Generate advice.
	 * 
	 * @return multi-line advice.
	 */
	String[] generate();
	
}
