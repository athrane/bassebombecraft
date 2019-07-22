package bassebombecraft.event.rendering;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.ModConstants.TARGETING_OVERLAY_ITEM;
import static bassebombecraft.entity.EntityUtils.getAliveTarget;
import static bassebombecraft.player.PlayerUtils.CalculatePlayerPosition;
import static bassebombecraft.player.PlayerUtils.getPlayer;
import static bassebombecraft.player.PlayerUtils.isItemInHotbar;
import static bassebombecraft.player.PlayerUtils.isPlayerDefined;
import static bassebombecraft.rendering.RenderingUtils.resetBillboardRendering;
import static bassebombecraft.rendering.RenderingUtils.setupBillboardRendering;
import static bassebombecraft.rendering.RenderingUtils.setupBillboardRotation;

import java.awt.Color;
import java.util.Collection;
import java.util.stream.Stream;

import javax.vecmath.Vector4f;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.platform.GlStateManager;

import bassebombecraft.entity.EntityUtils;
import bassebombecraft.event.charm.CharmedMob;
import bassebombecraft.event.charm.CharmedMobsRepository;
import bassebombecraft.event.entity.target.TargetedEntitiesRepository;
import bassebombecraft.event.entity.team.TeamRepository;
import bassebombecraft.event.particle.ParticleRendering;
import bassebombecraft.event.particle.ParticleRenderingRepository;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Event handler for additional rendering.
 */
@Mod.EventBusSubscriber
public class RenderingEventHandler {

	static int testCounter = 0;

	/**
	 * Text color.
	 */
	static final int TEXT_COLOR = 0xFFFFFF;

	/**
	 * Text scale
	 */
	static final float TEXT_SCALE = 0.03F;

	/**
	 * Charmed label.
	 */
	static final String CHARMED_LABEL = "Charmed";

	/**
	 * Team label.
	 */
	static final String TEAM_LABEL = "Team";

	/**
	 * Team target label.
	 */
	static final String TARGET_LABEL = "Target";

	/**
	 * Angle for rotation of text billboard.
	 */
	static final int TEXT_BILLBOARD_ANGLE = 180;

	/**
	 * Rotation of text billboard.
	 */
	static final Vector4f TEXT_BILLBOARD_ROTATION = new Vector4f(0.0F, 0.0F, 1.0F, TEXT_BILLBOARD_ANGLE);

	/**
	 * Angle for rotation of billboard for triangle for rendering team members and
	 * charmed entities.
	 */
	static final int TEAM_N_CHARMED_BILLBOARD_ANGLE = 0;

	/**
	 * Rotation of billboard for triangle for rendering team members and charmed
	 * entities.
	 */
	static final Vector4f TEAM_N_CHARMED_BILLBOARD_ROTATION = new Vector4f(0.0F, 0.0F, 1.0F,
			TEAM_N_CHARMED_BILLBOARD_ANGLE);

	/**
	 * Line width for rendering billboards.
	 */
	static final int BILLBOARD_LINE_WIDTH = 1;

	/**
	 * Triangle height for equilateral triangle with side length 1.
	 */
	static final float EQUILATERAL_TRIANGLE_HEIGHT = 0.866F;

	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public static void handleEvent(RenderWorldLastEvent event) {
		testCounter++;
		testCounter = testCounter % 360;

		// exit if player is undefined
		if (!isPlayerDefined())
			return;

		// get player
		PlayerEntity player = getPlayer();

		// exit if targeting overlay isn't in hotbar
		if (!isItemInHotbar(player, TARGETING_OVERLAY_ITEM))
			return;

		// get player position
		Vec3d playerPos = CalculatePlayerPosition(player, event.getPartialTicks());

		// renderParticles(playerPos);
		renderCharmedEntities(player, playerPos);
		renderTeamEntities(player, playerPos);
		renderTargetedEntities(player, playerPos);

	}

	static void renderCharmedEntities(PlayerEntity player, Vec3d playerPos) {
		CharmedMobsRepository repository = getBassebombeCraft().getCharmedMobsRepository();
		Collection<CharmedMob> entities = repository.get();
		synchronized (entities) {
			for (CharmedMob entity : entities)
				renderTeamEntity(player, entity.getEntity(), playerPos);
		}
	}

	static void renderCharmedEntity(LivingEntity entity, Vec3d playerPos) {
		Vec3d entityPos = entity.getEntityBoundingBox().getCenter();
		renderTriangleBillboard(playerPos, entityPos, TEAM_N_CHARMED_BILLBOARD_ROTATION);
		renderTextBillboard(playerPos, entityPos, CHARMED_LABEL, TEXT_BILLBOARD_ROTATION);

		// render target
		renderTarget(entity, playerPos);
	}

	static void renderTeamEntities(PlayerEntity player, Vec3d playerPos) {
		TeamRepository repository = getBassebombeCraft().getTeamRepository();
		Collection<LivingEntity> entities = repository.get(player);
		synchronized (entities) {
			for (LivingEntity entity : entities)
				renderTeamEntity(player, entity, playerPos);
		}
	}

	static void renderTeamEntity(PlayerEntity player, LivingEntity entity, Vec3d playerPos) {
		Vec3d entityPos = entity.getEntityBoundingBox().getCenter();
		renderTriangleBillboard(playerPos, entityPos, TEAM_N_CHARMED_BILLBOARD_ROTATION);
		renderTextBillboard(playerPos, entityPos, TEAM_LABEL, TEXT_BILLBOARD_ROTATION);

		// render target
		renderTarget(entity, playerPos);
	}

	static void renderTarget(LivingEntity entity, Vec3d playerPos) {
		if (!EntityUtils.hasAliveTarget(entity))
			return;
		LivingEntity target = getAliveTarget(entity);
		renderTargetedEntity(entity, target, playerPos);
	}

	static void renderTargetedEntities(PlayerEntity player, Vec3d playerPos) {
		TargetedEntitiesRepository repository = getBassebombeCraft().getTargetedEntitiesRepository();

		// get entities
		Stream<LivingEntity> members = repository.get(player);
		members.forEach(e -> renderTargetedEntity(e, playerPos));
	}

	static void renderTargetedEntity(LivingEntity target, Vec3d playerPos) {
		Vec3d targetPos = target.getEntityBoundingBox().getCenter();
		renderRectangleBillboard(playerPos, targetPos);
		renderTextBillboard(playerPos, targetPos, TARGET_LABEL, TEXT_BILLBOARD_ROTATION);
	}

	static void renderTargetedEntity(LivingEntity entity, LivingEntity target, Vec3d playerPos) {
		Vec3d entityPos = entity.getEntityBoundingBox().getCenter();
		Vec3d targetPos = target.getEntityBoundingBox().getCenter();
		renderRectangleBillboard(playerPos, targetPos);
		renderTextBillboard(playerPos, targetPos, TARGET_LABEL, TEXT_BILLBOARD_ROTATION);
		renderLineBillboard(playerPos, entityPos, targetPos);
	}

	/**
	 * Render text at origin.
	 */
	static void renderTextBillboard(Vec3d playerPos, Vec3d entityPos, String text, Vector4f rotation) {
		setupBillboardRendering();

		// get minecraft
		Minecraft mc = Minecraft.getMinecraft();

		// enable for rendering of text
		GlStateManager.enableTexture2D();

		// translate to camera position
		GlStateManager.translate(entityPos.x - playerPos.x, entityPos.y - playerPos.y, entityPos.z - playerPos.z);

		// set up billboard rotation
		setupBillboardRotation();

		// scale text
		GlStateManager.scale(TEXT_SCALE, TEXT_SCALE, TEXT_SCALE);

		// add addition rotation
		GlStateManager.rotate(rotation.w, rotation.x, rotation.y, rotation.z);

		// draw
		mc.fontRenderer.drawString(text, 0, 0, TEXT_COLOR);

		resetBillboardRendering();
	}

	/**
	 * Render a equilateral triangle billboard centered at origin.
	 */
	static void renderTriangleBillboard(Vec3d playerPos, Vec3d entityPos, Vector4f rotation) {
		setupBillboardRendering();

		// set line width & color
		GlStateManager.glLineWidth(BILLBOARD_LINE_WIDTH);
		GlStateManager.color(1, 1, 1);

		// translate to camera position
		GlStateManager.translate(entityPos.x - playerPos.x, entityPos.y - playerPos.y, entityPos.z - playerPos.z);

		// set up billboard rotation
		setupBillboardRotation();

		// add addition rotation
		GlStateManager.rotate(rotation.w, rotation.x, rotation.y, rotation.z);

		// create tessellator & bufferbuilder
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferBuilder = tessellator.getBuffer();

		// build buffer
		bufferBuilder.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION);

		// AB
		bufferBuilder.pos(0 - 0.5F, 0 - 0.289F, 0).endVertex();
		bufferBuilder.pos(1 - 0.5F, 0 - 0.289F, 0).endVertex();

		// BC
		bufferBuilder.pos(1 - 0.5F, 0 - 0.289F, 0).endVertex();
		bufferBuilder.pos(0.5F - 0.5F, EQUILATERAL_TRIANGLE_HEIGHT - 0.289F, 0).endVertex();

		// CA
		bufferBuilder.pos(0.5F - 0.5F, EQUILATERAL_TRIANGLE_HEIGHT - 0.289F, 0).endVertex();
		bufferBuilder.pos(0 - 0.5F, -0.289F, 0).endVertex();

		tessellator.draw();

		resetBillboardRendering();
	}

	/**
	 * Render a rectangle billboard centered at origin.
	 */
	static void renderRectangleBillboard(Vec3d playerPos, Vec3d entityPos) {
		setupBillboardRendering();

		// set line width & color
		GlStateManager.glLineWidth(1);
		GlStateManager.color(1, 1, 1);

		// translate and rotate billboard
		GlStateManager.translate(entityPos.x - playerPos.x, entityPos.y - playerPos.y, entityPos.z - playerPos.z);
		setupBillboardRotation();

		// create tessellator & bufferbuilder
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferBuilder = tessellator.getBuffer();

		// build buffer
		bufferBuilder.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION);

		// AB
		bufferBuilder.pos(0 - 0.5F, 0 - 0.5F, 0).endVertex();
		bufferBuilder.pos(1 - 0.5F, 0 - 0.5F, 0).endVertex();

		// BC
		bufferBuilder.pos(1 - 0.5F, 0 - 0.5F, 0).endVertex();
		bufferBuilder.pos(1 - 0.5F, 1 - 0.5F, 0).endVertex();

		// CD
		bufferBuilder.pos(1 - 0.5F, 1 - 0.5F, 0).endVertex();
		bufferBuilder.pos(0 - 0.5F, 1 - 0.5F, 0).endVertex();

		// DA
		bufferBuilder.pos(0 - 0.5F, 1 - 0.5F, 0).endVertex();
		bufferBuilder.pos(0 - 0.5F, 0 - 0.5F, 0).endVertex();

		tessellator.draw();

		resetBillboardRendering();
	}

	/**
	 * Render a line centered at origin.
	 */
	static void renderLineBillboard(Vec3d playerPos, Vec3d entityPos, Vec3d targetPos) {
		setupBillboardRendering();

		// set line width & color
		GlStateManager.glLineWidth(BILLBOARD_LINE_WIDTH);
		GlStateManager.color(1, 1, 1);

		// translate to camera position
		GlStateManager.translate(entityPos.x - playerPos.x, entityPos.y - playerPos.y, entityPos.z - playerPos.z);

		// set up billboard rotation
		setupBillboardRotation();

		// create tessellator & bufferbuilder
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferBuilder = tessellator.getBuffer();

		// build buffer
		bufferBuilder.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION);

		// ET
		bufferBuilder.pos(0, 0, 0).endVertex();
		bufferBuilder.pos(targetPos.x - entityPos.x, targetPos.y - entityPos.y, targetPos.z - entityPos.z).endVertex();

		tessellator.draw();

		resetBillboardRendering();
	}

	/**
	 * Render a billboard at origin.
	 */
	static void renderBillboardOrgin(Vec3d playerPos, Vec3d entityPos) {
		setupBillboardRendering();

		// set line width & color
		GlStateManager.glLineWidth(2);
		GlStateManager.color(1, 0, 0);

		// translate and rotate billboard
		GlStateManager.translate(entityPos.x - playerPos.x, entityPos.y - playerPos.y, entityPos.z - playerPos.z);
		setupBillboardRotation();

		// create tessellator & bufferbuilder
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferBuilder = tessellator.getBuffer();

		// build buffer
		bufferBuilder.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION);

		// AB
		bufferBuilder.pos(0, 0, 0).endVertex();
		bufferBuilder.pos(1, 0, 0).endVertex();

		// DA
		bufferBuilder.pos(0, 1, 0).endVertex();
		bufferBuilder.pos(0, 0, 0).endVertex();

		tessellator.draw();

		resetBillboardRendering();
	}

	static void renderParticles(Vec3d playerPos) {
		// get particles
		ParticleRenderingRepository repository = getBassebombeCraft().getParticleRenderingRepository();
		ParticleRendering[] paticles = repository.getParticles();

		// set line width & color
		GlStateManager.glLineWidth(1);
		Color color = new Color(255, 255, 255, 150);

		// render particles
		for (ParticleRendering particle : paticles) {
			BlockPos pos = particle.getPosition();
			double d0 = pos.getX();
			double d1 = pos.getY();
			double d2 = pos.getZ();
			double dx = 1;
			double dy = 1;
			double dz = 1;
			Vec3d posA = new Vec3d(d0, d1, d2);

			renderBox(playerPos.x, playerPos.y, playerPos.z, dx, dy, dz, posA, color);
		}
	}

	/**
	 * Render a box.
	 */
	@Deprecated
	static void renderBox(double doubleX, double doubleY, double doubleZ, double dx, double dy, double dz, Vec3d posA,
			Color c) {
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferBuilder = tessellator.getBuffer();
		bufferBuilder.setTranslation(-doubleX, -doubleY, -doubleZ);
		bufferBuilder.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION);

		// AB
		bufferBuilder.pos(posA.x, posA.y, posA.z).endVertex();
		bufferBuilder.pos(posA.x, posA.y, posA.z + dz).endVertex();
		// BC
		bufferBuilder.pos(posA.x, posA.y, posA.z + dz).endVertex();
		bufferBuilder.pos(posA.x + dx, posA.y, posA.z + dz).endVertex();
		// CD
		bufferBuilder.pos(posA.x + dx, posA.y, posA.z + dz).endVertex();
		bufferBuilder.pos(posA.x + dx, posA.y, posA.z).endVertex();
		// DA
		bufferBuilder.pos(posA.x + dx, posA.y, posA.z).endVertex();
		bufferBuilder.pos(posA.x, posA.y, posA.z).endVertex();
		// EF
		bufferBuilder.pos(posA.x, posA.y + dy, posA.z).endVertex();
		bufferBuilder.pos(posA.x, posA.y + dy, posA.z + dz).endVertex();
		// FG
		bufferBuilder.pos(posA.x, posA.y + dy, posA.z + dz).endVertex();
		bufferBuilder.pos(posA.x + dx, posA.y + dy, posA.z + dz).endVertex();
		// GH
		bufferBuilder.pos(posA.x + dx, posA.y + dy, posA.z + dz).endVertex();
		bufferBuilder.pos(posA.x + dx, posA.y + dy, posA.z).endVertex();
		// HE
		bufferBuilder.pos(posA.x + dx, posA.y + dy, posA.z).endVertex();
		bufferBuilder.pos(posA.x, posA.y + dy, posA.z).endVertex();
		// AE
		bufferBuilder.pos(posA.x, posA.y, posA.z).endVertex();
		bufferBuilder.pos(posA.x, posA.y + dy, posA.z).endVertex();
		// BF
		bufferBuilder.pos(posA.x, posA.y, posA.z + dz).endVertex();
		bufferBuilder.pos(posA.x, posA.y + dy, posA.z + dz).endVertex();
		// CG
		bufferBuilder.pos(posA.x + dx, posA.y, posA.z + dz).endVertex();
		bufferBuilder.pos(posA.x + dx, posA.y + dy, posA.z + dz).endVertex();
		// DH
		bufferBuilder.pos(posA.x + dx, posA.y, posA.z).endVertex();
		bufferBuilder.pos(posA.x + dx, posA.y + dy, posA.z).endVertex();

		tessellator.draw();
		bufferBuilder.setTranslation(0, 0, 0);
	}

}
