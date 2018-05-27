package bassebombecraft.event.rendering;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.player.PlayerUtils.CalculatePlayerPosition;
import static bassebombecraft.player.PlayerUtils.getPlayer;
import static bassebombecraft.player.PlayerUtils.isPlayerDefined;
import static bassebombecraft.rendering.RenderingUtils.resetBillboardRendering;
import static bassebombecraft.rendering.RenderingUtils.setupBillboardRendering;
import static bassebombecraft.rendering.RenderingUtils.setupBillboardRotation;

import java.awt.Color;
import java.util.stream.Stream;

import javax.vecmath.Vector4f;

import org.lwjgl.opengl.GL11;

import bassebombecraft.event.charm.CharmedMob;
import bassebombecraft.event.charm.CharmedMobsRepository;
import bassebombecraft.event.entity.team.TeamRepository;
import bassebombecraft.event.particle.ParticleRendering;
import bassebombecraft.event.particle.ParticleRenderingRepository;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Event handler for additional rendering.
 */
@Mod.EventBusSubscriber
public class RenderingEventHandler {

	static int testCounter = 0 ;
	
	/**
	 * Angle for rotation of billboard for triangle for rendering 
	 * team members and charmed entities.
	 */
	static final int TEAM_N_CHARMED_BILLBOARD_ANGLE = 0;

	/**
	 * Rotation of billboard for triangle for rendering 
	 * team members and charmed entities.
	 */	
	static final Vector4f TEAM_N_CHARMED_BILLBOARD_ROTATION = new Vector4f(0.0F,0.0F,1.0F,TEAM_N_CHARMED_BILLBOARD_ANGLE);
	
	/**
	 * Line width for rendering billboards.
	 */
	static final int BILLBOARD_LINE_WIDTH = 1;
	
	/**
	 * Triangle height for equilateral triangle with side length 1.
	 */
	static final float EQUILATERAL_TRIANGLE_HEIGHT = 0.866F;
		
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void handleEvent(RenderWorldLastEvent event) {
		testCounter ++;
		testCounter = testCounter % 360;
		
		// exit if player is undefined
		if (!isPlayerDefined())
			return;
				
		// get player
		EntityPlayer player = getPlayer();
				
		// exit if targeting overlay isn't in hotbar
		//final ItemStack stack = new ItemStack(ModConstants.TARGETING_OVERLAY_ITEM);		
		//if (!isItemHeldInOffHand(player, stack)) return;
		
		// get player position
		Vec3d playerPos = CalculatePlayerPosition(player, event.getPartialTicks());

		// renderParticles(playerPos);
		renderCharmedEntities(playerPos);
		renderTeamEntities(player, playerPos);
	}

	static void renderCharmedEntities(Vec3d playerPos) {
		CharmedMobsRepository repository = getBassebombeCraft().getCharmedMobsRepository();
				
		// get charmed mobs
		Stream<CharmedMob> charmed = repository.getCharmedMobs();
		charmed.forEach(e -> renderCharmedEntity(e.getEntity(), playerPos));
	}

	static void renderCharmedEntity(EntityLiving entity, Vec3d playerPos) {
		Vec3d entityPos = entity.getEntityBoundingBox().getCenter();
		renderTriangleBillboard(playerPos, entityPos, TEAM_N_CHARMED_BILLBOARD_ROTATION);
	}

	static void renderTeamEntities(EntityPlayer player, Vec3d playerPos) {
		TeamRepository repository = getBassebombeCraft().getTeamRepository();
				
		// get team members
		Stream<EntityLivingBase> members = repository.getTeamMembers(player);
		members.forEach(e -> renderTeamEntity(e, playerPos));
	}
	
	static void renderTeamEntity(EntityLivingBase e, Vec3d playerPos) {
		Vec3d entityPos = e.getEntityBoundingBox().getCenter();
		renderTriangleBillboard(playerPos, entityPos, TEAM_N_CHARMED_BILLBOARD_ROTATION);	
	}
	
	/**
	 * Render a equilateral triangle billboard centered at origin.
	 */
	static void renderTriangleBillboard(Vec3d playerPos, Vec3d entityPos, Vector4f rotation) {			    	    
		setupBillboardRendering();
				
		// set line width & color
		GlStateManager.glLineWidth(BILLBOARD_LINE_WIDTH);
		GlStateManager.color(1,1,1);
		
		// translate to camera position 
		GlStateManager.translate(entityPos.x-playerPos.x, entityPos.y-playerPos.y, entityPos.z-playerPos.z);		
		
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
		bufferBuilder.pos(0-0.5F, 0-0.289F, 0).endVertex();
		bufferBuilder.pos(1-0.5F, 0-0.289F, 0).endVertex();

		// BC
		bufferBuilder.pos(1-0.5F, 0-0.289F, 0).endVertex();
		bufferBuilder.pos(0.5F-0.5F, EQUILATERAL_TRIANGLE_HEIGHT-0.289F, 0).endVertex();

		// CA
		bufferBuilder.pos(0.5F-0.5F, EQUILATERAL_TRIANGLE_HEIGHT-0.289F, 0).endVertex();
		bufferBuilder.pos(0-0.5F, -0.289F, 0).endVertex();

		tessellator.draw();
		
		resetBillboardRendering();		
	}

	/**
	 * Render a rectangle billboard centered at origin.
	 */
	static void renderBillboardRect(Vec3d playerPos, Vec3d entityPos) {			    	    
		setupBillboardRendering();
		
		// set line width & color
		GlStateManager.glLineWidth(1);
		GlStateManager.color(1,1,1);
		
		// translate and rotate billboard
		GlStateManager.translate(entityPos.x-playerPos.x, entityPos.y-playerPos.y, entityPos.z-playerPos.z);
		setupBillboardRotation();
		
		// create tessellator & bufferbuilder
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferBuilder = tessellator.getBuffer();
		
		// build buffer
		bufferBuilder.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION);
		
		// AB
		bufferBuilder.pos(0-0.5F, 0-0.5F, 0).endVertex();
		bufferBuilder.pos(1-0.5F, 0-0.5F, 0).endVertex();

		// BC
		bufferBuilder.pos(1-0.5F, 0-0.5F, 0).endVertex();
		bufferBuilder.pos(1-0.5F, 1-0.5F, 0).endVertex();

		// CD
		bufferBuilder.pos(1-0.5F, 1-0.5F, 0).endVertex();
		bufferBuilder.pos(0-0.5F, 1-0.5F, 0).endVertex();

		// DA		
		bufferBuilder.pos(0-0.5F, 1-0.5F, 0).endVertex();
		bufferBuilder.pos(0-0.5F, 0-0.5F, 0).endVertex();
				
		tessellator.draw();
		
		resetBillboardRendering();		
	}
	
	
	/**
	 * Render a billboard origin.
	 */
	static void renderBillboardOrgin(Vec3d playerPos, Vec3d entityPos) {			    	    
		setupBillboardRendering();
		
		// set line width & color
		GlStateManager.glLineWidth(2);
		GlStateManager.color(1,0,0);
		
		// translate and rotate billboard
		GlStateManager.translate(entityPos.x-playerPos.x, entityPos.y-playerPos.y, entityPos.z-playerPos.z);
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
