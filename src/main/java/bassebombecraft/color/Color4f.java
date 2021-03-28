package bassebombecraft.color;

public interface Color4f {

	/**
	 * Get r-component in range [0..1].
	 * 
	 * @return r-component
	 */
	float getR();

	/**
	 * Get g-component in range [0..1].
	 * 
	 * @return g-component
	 */
	float getG();

	/**
	 * Get b-component in range [0..1].
	 * 
	 * @return b-component
	 */
	float getB();

	/**
	 * Get alpha component in range [0..1].
	 * 
	 * @return alpha component
	 */
	float getAlpha();
	
	/**
	 * Computes lerp value between this color and input.
	 *  
	 * @param color another color.
	 * @param value lerp value in range [0..1]. 
	 * @return lerp value between this color and input.
	 */
	Color4f lerp(Color4f color, float value);
	
}
