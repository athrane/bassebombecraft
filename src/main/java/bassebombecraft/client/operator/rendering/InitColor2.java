package bassebombecraft.client.operator.rendering;

import static bassebombecraft.geom.GeometryUtils.oscillateFloat;
import static bassebombecraft.operator.DefaultPorts.getBcSetColor4f2;

import java.util.function.BiConsumer;

import bassebombecraft.color.Color4f;
import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import net.minecraft.util.math.vector.Vector4f;

/**
 * Implementation of the {@linkplain Operator2} interface which creates an color
 * interpolated between two colors.
 * 
 * the color is stored as a {@linkplain Vector4f} in the ports.
 */
public class InitColor2 implements Operator2 {

	/**
	 * Function to set color.
	 */
	BiConsumer<Ports, Color4f> bcSetColor;

	/**
	 * Base color #1.
	 */
	Color4f color1;

	/**
	 * Base color #2.
	 */
	Color4f color2;

	/**
	 * Constructor.
	 * 
	 * Instance is configured to set vector4f #2 as calculated color from ports.
	 * 
	 * @param color1 base color #1.
	 * @param color1 base color #2.
	 */
	public InitColor2(Color4f color1, Color4f color2) {
		this(getBcSetColor4f2(), color1, color2);
	}

	/**
	 * Constructor.
	 * 
	 * @param bcSetColor function to set color.
	 * @param color1     base color #1.
	 * @param color1     base color #2.
	 */
	public InitColor2(BiConsumer<Ports, Color4f> bcSetColor, Color4f color1, Color4f color2) {
		this.color1 = color1;
		this.color2 = color2;
		this.bcSetColor = bcSetColor;
	}

	@Override
	public void run(Ports ports) {
		float oscValue = oscillateFloat(0, 1);
		Color4f color = color1.lerp(color2, oscValue);
		bcSetColor.accept(ports, color);
	}

}
