package bassebombecraft.client.operator.rendering;

import static bassebombecraft.operator.DefaultPorts.getBcSetVector4f2;
import static net.minecraft.util.math.MathHelper.lerp;

import java.util.function.BiConsumer;

import static bassebombecraft.geom.GeometryUtils.*;
import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import net.minecraft.client.renderer.Vector4f;

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
	BiConsumer<Ports, Vector4f> bcSetColor;

	/**
	 * Base color #1.
	 */
	Vector4f color1;

	/**
	 * Base color #2.
	 */
	Vector4f color2;

	/**
	 * Constructor.
	 * 
	 * Instance is configured to set vector4f #2 as calculated color from ports.
	 * 
	 * @param color1 base color #1.
	 * @param color1 base color #2.
	 */
	public InitColor2(Vector4f color1, Vector4f color2) {
		this(getBcSetVector4f2(), color1, color2);
	}

	/**
	 * Constructor.
	 * 
	 * @param bcSetColor function to set color.
	 * @param color1     base color #1.
	 * @param color1     base color #2.
	 */
	public InitColor2(BiConsumer<Ports, Vector4f> bcSetColor, Vector4f color1, Vector4f color2) {
		this.color1 = color1;
		this.color2 = color2;
		this.bcSetColor = bcSetColor;
	}

	@Override
	public void run(Ports ports) {

		float oscValue = oscillateFloat(0, 1);
		float x = lerp(oscValue, color1.getX(), color2.getX());
		float y = lerp(oscValue, color1.getY(), color2.getY());
		float z = lerp(oscValue, color1.getZ(), color2.getZ());
		float alpha = lerp(oscValue, color1.getW(), color2.getW());
		Vector4f color = new Vector4f(x, y, z, alpha);
		bcSetColor.accept(ports, color);
	}

}
