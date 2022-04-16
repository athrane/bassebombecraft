package bassebombecraft.color;

import net.minecraft.util.Mth;

/**
 * Implementation of the {@linkplain Color4f} interface.
 */
public class DefaultColor4f implements Color4f {

	/**
	 * R-component.
	 */
	float r;

	/**
	 * G-component.
	 */
	float g;

	/**
	 * B-component.
	 */
	float b;

	/**
	 * Alpha component.
	 */
	float alpha;

	/**
	 * Constructor.
	 * 
	 * @param r     r-component
	 * @param g     g-component
	 * @param b     b-component
	 * @param alpha alpha component
	 */
	DefaultColor4f(float r, float g, float b, float alpha) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.alpha = alpha;
	}

	@Override
	public float getR() {
		return r;
	}

	@Override
	public float getG() {
		return g;
	}

	@Override
	public float getB() {
		return b;
	}

	@Override
	public float getAlpha() {
		return alpha;
	}

	@Override
	public Color4f lerp(Color4f color, float value) {
		float r = Mth.lerp(value, getR(), color.getR());
		float g = Mth.lerp(value, getG(), color.getG());
		float b = Mth.lerp(value, getB(), color.getB());
		float a = Mth.lerp(value, getAlpha(), color.getAlpha());
		return new DefaultColor4f(r, g, b, a);
	}

	public String toString() {
		return "[" + this.r + ", " + this.g + ", " + this.b + ", " + this.alpha + "]";
	}

	/**
	 * Factory method.
	 * 
	 * @param r     r-component
	 * @param g     g-component
	 * @param b     b-component
	 * @param alpha alpha component
	 * 
	 * @return color RGBA color.
	 */
	public static Color4f getInstance(float r, float g, float b, float alpha) {
		return new DefaultColor4f(r, g, b, alpha);
	}
}
