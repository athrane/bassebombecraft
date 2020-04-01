package bassebombecraft.rendering;

import static bassebombecraft.ModConstants.TEXT_COLOR;
import static bassebombecraft.ModConstants.TEXT_SCALE;
import static bassebombecraft.player.PlayerUtils.CalculatePlayerPosition;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;

import bassebombecraft.BassebombeCraft;
import static bassebombecraft.ModClientConstants.*;
import bassebombecraft.player.PlayerUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

/**
 * Implementation of the {@linkplain EntityRenderer} for debug rendering af
 * text.
 */
public class DebugTextBillBoardRenderer implements EntityRenderer {

	/**
	 * Team label.
	 */
	static final String TEAM_LABEL = "DEBUG";

	@Override
	public void render(LivingEntity entity, RenderingInfo info) {

		// exit if entity isn't player
		if (!PlayerUtils.isTypePlayerEntity(entity))
			return;

		// typecast
		PlayerEntity player = (PlayerEntity) entity;

		// get player position
		Vec3d playerPos = CalculatePlayerPosition(player, info.getPartialTicks());

		// calculate translation of text
		Minecraft mc = Minecraft.getInstance();
		ActiveRenderInfo activeRenderInfo = mc.gameRenderer.getActiveRenderInfo();
		
		Vec3d pv = activeRenderInfo.getProjectedView();
		Vec3d renderPos = new Vec3d(pv.getX(), pv.getY(), pv.getZ());
		Vec3d translation = playerPos.subtract(renderPos);		

		BassebombeCraft.getBassebombeCraft().getLogger().debug("rve-translated=" + info.getRveTranslatedViewX()+","+info.getRveTranslatedViewY()+","+info.getRveTranslatedViewZ());
		BassebombeCraft.getBassebombeCraft().getLogger().debug("rve-translated with player offset=" + info.getRveTranslatedViewX()+","+info.getRveTranslatedViewYOffsetWithPlayerEyeHeight()+","+info.getRveTranslatedViewZ());		
		BassebombeCraft.getBassebombeCraft().getLogger().debug("playerPos=" + playerPos);
		BassebombeCraft.getBassebombeCraft().getLogger().debug("renderPos =" + renderPos );		
		BassebombeCraft.getBassebombeCraft().getLogger().debug("camera translation=" + translation);
		BassebombeCraft.getBassebombeCraft().getLogger().debug("info.yaw=" + activeRenderInfo.getYaw());
		BassebombeCraft.getBassebombeCraft().getLogger().debug("info.pitch=" + activeRenderInfo.getPitch());
		BassebombeCraft.getBassebombeCraft().getLogger().debug("view vector=" + activeRenderInfo.getViewVector());
		BassebombeCraft.getBassebombeCraft().getLogger().debug("up vector=" + activeRenderInfo.getUpVector());
		BassebombeCraft.getBassebombeCraft().getLogger().debug("ProjectedView=" + activeRenderInfo.getProjectedView());
		BassebombeCraft.getBassebombeCraft().getLogger().debug("RenderViewEntity=" + activeRenderInfo.getRenderViewEntity());
		BassebombeCraft.getBassebombeCraft().getLogger().debug("RenderViewEntity.lookVec=" + activeRenderInfo.getRenderViewEntity().getLookVec());
		BassebombeCraft.getBassebombeCraft().getLogger().debug("RenderViewEntity.positionVec=" + activeRenderInfo.getRenderViewEntity().getPositionVec());

		int scaledWidth = mc.getMainWindow().getScaledWidth();
		int scaledHeight = mc.getMainWindow().getScaledHeight();
		BassebombeCraft.getBassebombeCraft().getLogger().debug("scaledWidth=" + scaledWidth);		
		BassebombeCraft.getBassebombeCraft().getLogger().debug("scaledHeight =" + scaledHeight );		
				
		// render basic info
		// renderHudTextBillboard(translation, textTranslation.add(0, -HUD_TEXT_DISP *
		// 2, 0), "TEXT #3");

		/**
		renderHudTextBillboard(translation, new Vec3d(0, 0, 4), "TEXT at (0,0,4)");
		Vec3d textTranslation = new Vec3d(5, 4, 4);
		renderHudTextBillboard(translation, textTranslation, "TEXT at (5,4,4)");
		renderHudTextBillboard(translation, textTranslation.add(-2, 0, 0), "TEXT at (3,4,4)");
		renderHudTextBillboard(translation, textTranslation.add(-4, 0, 0), "TEXT at (1,4,4)");		
		renderHudTextBillboard(translation, textTranslation.add(0, -2, 0), "TEXT at (5,2,4)");
		renderHudTextBillboard(translation, textTranslation.add(0, -4, 0), "TEXT at (5,0,4)");
				
		translation = new Vec3d(0,0,0);				
		renderHudTextBillboard(translation, new Vec3d(0, 0, 4), "ATEXT at (0,0,4)");
		renderHudTextBillboard(translation, textTranslation, "ATEXT at (5,4,4)");
		renderHudTextBillboard(translation, textTranslation.add(-2, 0, 0), "ATEXT at (3,4,4)");
		renderHudTextBillboard(translation, textTranslation.add(-4, 0, 0), "ATEXT at (1,4,4)");		
		renderHudTextBillboard(translation, textTranslation.add(0, -2, 0), "ATEXT at (5,2,4)");
		renderHudTextBillboard(translation, textTranslation.add(0, -4, 0), "ATEXT at (5,0,4)");
		
		renderHudTextBillboard(new Vec3d(0, 0, 4), "BTEXT at (0,0,4)");
		renderHudTextBillboard(textTranslation, "BTEXT at (5,4,4)");
		renderHudTextBillboard(textTranslation.add(-2, 0, 0), "BTEXT at (3,4,4)");
		renderHudTextBillboard(textTranslation.add(-4, 0, 0), "BTEXT at (1,4,4)");		
		renderHudTextBillboard(textTranslation.add(0, -2, 0), "BTEXT at (5,2,4)");
		renderHudTextBillboard(textTranslation.add(0, -4, 0), "BTEXT at (5,0,4)");
		**/

		/**
		renderHudTextBillboardV2(new Vec3d(0, 0, 4), "V2-TEXT at (0,0,4)");
		Vec3d textTranslation = new Vec3d(5, 4, 4);		
		renderHudTextBillboardV2(textTranslation, "V2-TEXT at (5,4,4)");
		renderHudTextBillboardV2(textTranslation.add(-2, 0, 0), "V2-TEXT at (3,4,4)");
		renderHudTextBillboardV2(textTranslation.add(-4, 0, 0), "V2-TEXT at (1,4,4)");		
		renderHudTextBillboardV2(textTranslation.add(0, -2, 0), "V2-TEXT at (5,2,4)");
		renderHudTextBillboardV2(textTranslation.add(0, -4, 0), "V2-TEXT at (5,0,4)");
		**/

		/**
		renderHudTextBillboardV3("V3-TEXT at (0,0)", 0,0);
		renderHudTextBillboardV3("V3-TEXT at (10,10)", 10,10);
				
        int i1 = (scaledWidth - mc.fontRenderer.getStringWidth("V3-TEXT at (-20,0)")) / 2;
        int j1 = scaledHeight - 31 - 4;
		BassebombeCraft.getBassebombeCraft().getLogger().debug("i1=" + i1);		
		BassebombeCraft.getBassebombeCraft().getLogger().debug("j1 ="+ j1);		
		
		renderHudTextBillboardV3("V3-TEXT at (2,+1)", 2,j1+1);
		renderHudTextBillboardV3("V3-TEXT at (2,+2)", 2,j1+2);
		renderHudTextBillboardV3("V3-TEXT at (2,+3)", 2,j1+3);
		*/
		
		renderHudTextBillboardV2a(new Vec3d(0, 0, 4), "V2a-TEXT at (0,0,4)");
		renderHudTextBillboardV2a(new Vec3d(2, 0, 4), "V2a-TEXT at (2,0,4)");
		renderHudTextBillboardV2a(new Vec3d(2, 2, 4), "V2a-TEXT at (2,2,4)");
		renderHudTextBillboardV2a(new Vec3d(0, 2, 4), "V2a-TEXT at (0,2,4)");		
	}

	/**
	 * Render text at origin for rendering of HUD text.
	 * 
	 * This method supports translation of the text relative to the player view
	 * direction and independent of the camera (or player) orientation and
	 * placement.
	 * 
	 * @param textTranslation   text translation vector for translation of text
	 *                          relative to view direction. Defines the placement of
	 *                          the HUD text.
	 * @param text              text to render
	 */
	void renderHudTextBillboardV2a(Vec3d textTranslation, String text) {
		
		RenderHelper.disableStandardItemLighting();
		// setupBillboardRendering();
		// save matrix
		// V2		
		//RenderSystem.pushMatrix();		
		//RenderHelper.disableStandardItemLighting();
		// V1
		GlStateManager.pushMatrix();
		GlStateManager.disableLighting();
		GlStateManager.disableTexture();
		GlStateManager.disableDepthTest();

		// get minecraft
		Minecraft mc = Minecraft.getInstance();

		// set up billboard rotation
		// setupBillboardRotation();		
		float w = (float) RenderingUtils.oscillate(0, 20);
		RenderSystem.rotatef(180, 0, 1, 0);
		RenderSystem.rotatef(180+w, 0, 0, 1);		

		// translation of text relative to view direction.
		// Defines the placement of the HUD text
		RenderSystem.translated(textTranslation.x, textTranslation.y, textTranslation.z);

		// scale text
		RenderSystem.scaled(TEXT_SCALE, TEXT_SCALE, TEXT_SCALE);
		
		// draw
		mc.fontRenderer.drawString(text, 0, 0, TEXT_COLOR);		
		
		// resetBilboardRendering();
		// V1
		GlStateManager.popMatrix();
		GlStateManager.enableTexture();
		GlStateManager.enableLighting();
		GlStateManager.enableDepthTest();

		// V2
		//RenderHelper.enableStandardItemLighting();		
        //RenderSystem.popMatrix();						
	}
	
	/**
	 * Render text at origin for rendering of HUD text.
	 * 
	 * This method supports translation of the text relative to the player view
	 * direction and independent of the camera (or player) orientation and
	 * placement.
	 * 
	 * @param textTranslation   text translation vector for translation of text
	 *                          relative to view direction. Defines the placement of
	 *                          the HUD text.
	 * @param text              text to render
	 */
	void renderHudTextBillboardV3(String text, int x, int y) {
		// setupBillboardRendering();
		// save matrix
		RenderSystem.pushMatrix();
		RenderSystem.disableLighting();

		// set up billboard rotation
		// setupBillboardRotation();		
		RenderSystem.rotatef(180, 0, 1, 0);

		float z = (float) RenderingUtils.oscillate(0.1F, 0.2F);
		RenderSystem.translated(0,0, 0.1);
		
		// scale text
		double text_scale = 0.001D;
		RenderSystem.scaled(text_scale, text_scale, text_scale);

		// add billboard rotation for text readability
		RenderSystem.rotatef(TEXT_BILLBOARD_ROTATION.getW(), TEXT_BILLBOARD_ROTATION.getX(),
				TEXT_BILLBOARD_ROTATION.getY(), TEXT_BILLBOARD_ROTATION.getZ());
		
		// draw
		Minecraft mc = Minecraft.getInstance();		
		mc.fontRenderer.drawString(text, x, y, TEXT_COLOR);

		// resetBilboardRendering();
		// get matrix
		RenderSystem.popMatrix();
		RenderSystem.enableLighting();
	}
	
	
	/**
	 * Render text at origin for rendering of HUD text.
	 * 
	 * This method supports translation of the text relative to the player view
	 * direction and independent of the camera (or player) orientation and
	 * placement.
	 * 
	 * @param textTranslation   text translation vector for translation of text
	 *                          relative to view direction. Defines the placement of
	 *                          the HUD text.
	 * @param text              text to render
	 */
	void renderHudTextBillboardV2(Vec3d textTranslation, String text) {
		// setupBillboardRendering();
		// save matrix
		GlStateManager.pushMatrix();

		GlStateManager.disableLighting();
		GlStateManager.disableTexture();
		GlStateManager.disableDepthTest();
		
		// get minecraft
		Minecraft mc = Minecraft.getInstance();

		// enable for rendering of text
		GlStateManager.enableTexture();

		// set up billboard rotation
		// setupBillboardRotation();
		
		float w = (float) RenderingUtils.oscillate(0, 180);
		RenderSystem.rotatef(180, 0, 1, 0);

		// translation of text relative to view direction.
		// Defines the placement of the HUD text
		GlStateManager.translated(textTranslation.x, textTranslation.y, textTranslation.z);

		// scale text
		GlStateManager.scalef(TEXT_SCALE, TEXT_SCALE, TEXT_SCALE);

		// add billboard rotation for text readability
		GlStateManager.rotatef(TEXT_BILLBOARD_ROTATION.getW(), TEXT_BILLBOARD_ROTATION.getX(),
				TEXT_BILLBOARD_ROTATION.getY(), TEXT_BILLBOARD_ROTATION.getZ());
		
		// draw
		mc.fontRenderer.drawString(text, 0, 0, TEXT_COLOR);

		// resetBilboardRendering();
		// get matrix
		GlStateManager.popMatrix();
		
		// set attribute
		GlStateManager.enableTexture();
		GlStateManager.enableLighting();
		GlStateManager.enableDepthTest();
	}

	/**
	 * Render text at origin for rendering of HUD text.
	 * 
	 * This method supports translation of the text relative to the player view
	 * direction and independent of the camera (or player) orientation and
	 * placement.
	 * 
	 * @param cameraTranslation camera translation vector.
	 * @param textTranslation   text translation vector for translation of text
	 *                          relative to view direction. Defines the placement of
	 *                          the HUD text.
	 * @param text              text to render
	 */
	void renderHudTextBillboard(Vec3d cameraTranslation, Vec3d textTranslation, String text) {
		// setupBillboardRendering();
		// save matrix
		GlStateManager.pushMatrix();

		GlStateManager.disableLighting();
		GlStateManager.disableTexture();
		GlStateManager.disableDepthTest();

		// get minecraft
		Minecraft mc = Minecraft.getInstance();

		// enable for rendering of text
		GlStateManager.enableTexture();

		// translate to camera position
		GlStateManager.translated(cameraTranslation.x, cameraTranslation.y, cameraTranslation.z);

		// set up billboard rotation
		// setupBillboardRotation();
		
		float w = (float) RenderingUtils.oscillate(0, 180);
		RenderSystem.rotatef(180, 0, 1, 0);
		//RenderSystem.rotatef(w - 180, 0, 1, 0);
		//RenderSystem.rotatef(info.getYaw() +w - 180, 0, 1, 0);
		//RenderSystem.rotatef(info.getYaw() - 180, 0, 1, 0);		
		//RenderSystem.rotatef(info.getPitch(), 1, 0, 0);

		// translation of text relative to view direction.
		// Defines the placement of the HUD text
		GlStateManager.translated(textTranslation.x, textTranslation.y, textTranslation.z);

		// scale text
		GlStateManager.scalef(TEXT_SCALE, TEXT_SCALE, TEXT_SCALE);

		// add billboard rotation for text readability
		GlStateManager.rotatef(TEXT_BILLBOARD_ROTATION.getW(), TEXT_BILLBOARD_ROTATION.getX(),
				TEXT_BILLBOARD_ROTATION.getY(), TEXT_BILLBOARD_ROTATION.getZ());
		
		// draw
		mc.fontRenderer.drawString(text, 0, 0, TEXT_COLOR);

		// resetBilboardRendering();
		// get matrix
		GlStateManager.popMatrix();

		// set attribute
		GlStateManager.enableTexture();
		GlStateManager.enableLighting();
		GlStateManager.enableDepthTest();
	}
	
}
