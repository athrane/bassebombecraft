package bassebombecraft;

import bassebombecraft.client.event.rendering.BuildMineBookRenderer;
import bassebombecraft.client.event.rendering.HudItemHighlightedBlockRenderer;
import bassebombecraft.client.op.rendering.RenderTextBillboard2;
import bassebombecraft.item.basic.HudItem;
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

	/**
	 * Rendering: Text color for {@linkplain HudItem} in
	 * {@linkplain HudItemCharmedInfoRenderer}.
	 */
	public static final int HUD_TEXT_COLOR = 0x00C000;
	
	/**
	 * Rendering: Text translation along Z-axis for rendering of billboard text in
	 * HUD item in the {@linkplain RenderTextBillboard2} and {@linkplain RenderingUtils}class.
	 */
	public static final int TEXT_Z_TRANSLATION = 200;

	/**
	 * Rendering: Text scale for rendering of billboard text in HUD item in the
	 * {@linkplain RenderTextBillboard2} class.
	 */
	public static final float TEXT_SCALE_2 = 0.0007F;

	/**
	 * Rendering: Text color for {@linkplain BuildMineBook} in
	 * {@linkplain BuildMineBookRenderer}.
	 */
	public static final int BUILDMINEBOOK_TEXT_COLOR = 0xC0C000;

	/**
	 * Rendering: Text color.
	 */
	public static final int TEXT_COLOR = 0x00C000;

	/**
	 * Rendering: Text scale
	 */
	public static final float TEXT_SCALE = 0.02F;

	/**
	 * Entity texture path.
	 */
	public static final String ENTITY_TEXTURE_PATH = "textures/entity/";

	/**
	 * Screen texture path.
	 */
	public static final String GUI_TEXTURE_PATH = "textures/gui/";

	/**
	 * Default spawn duration for particle renderings.
	 */
	public static final int PARTICLE_SPAWN_DURATION = 2;

	/**
	 * Default line color 
	 */
	public static final Vector4f DEFAULT_LINE_COLOR = new Vector4f(0, 0.75F, 0, 1);

	/**
	 * Rendering: Line color for lightning for {@linkplain ClientGraphicalEffectRepository}.
	 */
	public static final Vector4f LIGHTNING_LINE_COLOR = new Vector4f(0.25F, 0.25F, 0.75F, 0.75F);
	
}
