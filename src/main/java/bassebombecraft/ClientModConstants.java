package bassebombecraft;

import bassebombecraft.client.event.rendering.BuildMineBookRenderer;
import bassebombecraft.client.event.rendering.HudItemHighlightedBlockRenderer;
import bassebombecraft.item.book.BuildMineBook;
import net.minecraft.client.renderer.Vector4f;

/**
 * Mod constants which should only be loaded by by the configuration: physical
 * client / logical client.
 */
public class ClientModConstants {

	/**
	 * Rendering: Angle for rotation of text billboard.
	 */
	public static final int TEXT_BILLBOARD_ANGLE = 180;

	/**
	 * Rendering: Rotation of text billboard.
	 */
	public static final Vector4f TEXT_BILLBOARD_ROTATION = new Vector4f(0.0F, 0.0F, 1.0F, TEXT_BILLBOARD_ANGLE);

	/**
	 * Rendering: Angle for rotation of billboard for triangle for rendering team
	 * members.
	 */
	public static final int BILLBOARD_ANGLE = 0;

	/**
	 * Rendering: Rotation of billboard for triangle for rendering team members.
	 */
	public static final Vector4f ICON_BILLBOARD_ROTATION = new Vector4f(0.0F, 0.0F, 1.0F, BILLBOARD_ANGLE);

	/**
	 * Rendering: Line color for wire frame box for {@linkplain BuildMineBook} in
	 * {@linkplain BuildMineBookRenderer}. And for the HUD item in the
	 * {@linkplain HudItemHighlightedBlockRenderer} class.
	 */
	public static final Vector4f HUD_LINE_COLOR = new Vector4f(0, 0.75F, 0, 1);

}
