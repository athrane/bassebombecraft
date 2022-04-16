package bassebombecraft.client.event.rendering;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.BassebombeCraft.getProxy;
import static bassebombecraft.ClientModConstants.TEXT_COLOR;
import static bassebombecraft.ClientModConstants.TEXT_SCALE;
import static bassebombecraft.client.player.ClientPlayerUtils.getClientSidePlayer;
import static bassebombecraft.client.player.ClientPlayerUtils.isClientSidePlayerDefined;
import static bassebombecraft.entity.ai.AiUtils.getFirstRunningAiGoalName;
import static bassebombecraft.entity.ai.AiUtils.getFirstRunningAiTargetGoalName;
import static bassebombecraft.geom.GeometryUtils.oscillate;
import static bassebombecraft.item.RegisteredItems.HUD;
import static bassebombecraft.player.PlayerUtils.isItemInHotbar;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexConsumer;

import bassebombecraft.client.rendering.rendertype.RenderTypes;
import bassebombecraft.event.entity.team.TeamRepository;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderLivingEvent.Pre;

/**
 * Rendering team member information in the HUD item.
 */
public class TeamEnityRenderer {

	/**
	 * Handle {@linkplain RenderLivingEvent}.
	 * 
	 * @param event event to trigger rendering of information.
	 */
	public static void handleRenderLivingEvent(Pre<Player, PlayerModel<Player>> event) {
		try {

			// exit if player is undefined
			if (!isClientSidePlayerDefined())
				return;

			// get player
			Player player = getClientSidePlayer();

			// exit if HUD item isn't in hotbar
			if (!isItemInHotbar(player, HUD.get()))
				return;

			// exit if entity isn't a member
			TeamRepository repository = getProxy().getServerTeamRepository();
			if (!repository.isMember(player, event.getEntity()))
				return;

			render(event.getMatrixStack(), event.getEntity());

		} catch (Exception e) {
			getBassebombeCraft().reportAndLogException(e);
		}
	}

	/**
	 * Render team member info.
	 * 
	 * @param matrixStack matrix static for rendering transforms.
	 * @param entity      member of the players team.
	 */
	static void render(PoseStack matrixStack, LivingEntity entity) {
		// get render buffer
		MultiBufferSource.BufferSource buffer = Minecraft.getInstance().renderBuffers().bufferSource();

		float height = entity.getBbHeight();
		renderText(matrixStack, buffer, 0, 0, "TEAM");
		renderText(matrixStack, buffer, 0, -10, getFirstRunningAiGoalName(entity));
		renderText(matrixStack, buffer, 0, -20, getFirstRunningAiTargetGoalName(entity));

		float w = (float) oscillate(-10, 10);
		matrixStack.pushPose();
		matrixStack.translate(0, height, 0);
		matrixStack.mulPose(Vector3f.YP.rotationDegrees(180));
		Matrix4f positionMatrix = matrixStack.last().pose();
		VertexConsumer builder = buffer.getBuffer(RenderTypes.OVERLAY_LINES);
		renderTriangle(builder, positionMatrix);
		matrixStack.popPose();

		// see: https://wiki.mcjty.eu/modding/index.php?title=Tut15_Ep15
		RenderSystem.disableDepthTest();
		buffer.endBatch(RenderTypes.OVERLAY_LINES);
	}

	static void renderText(PoseStack matrixStack, MultiBufferSource buffer, float x, float y, String text) {
		EntityRenderDispatcher renderManager = Minecraft.getInstance().getEntityRenderDispatcher();
		Font fontrenderer = renderManager.getFont();

		matrixStack.pushPose();
		matrixStack.scale(TEXT_SCALE, TEXT_SCALE, TEXT_SCALE);
		matrixStack.mulPose(renderManager.cameraOrientation());
		matrixStack.mulPose(Vector3f.ZP.rotationDegrees(180));
		Matrix4f positionMatrix = matrixStack.last().pose();
		fontrenderer.drawInBatch(text, x, y, TEXT_COLOR, false, positionMatrix, buffer, false, 0, 0xf000f0);
		matrixStack.popPose();

	}

	/**
	 * Render shield.
	 */
	public static void renderTriangle(VertexConsumer builder, Matrix4f positionMatrix) {

		addWhiteVertex(builder, positionMatrix, 0 - 0.5F, 0, 0);
		addWhiteVertex(builder, positionMatrix, 1 - 0.5F, 0, 0);

		addWhiteVertex(builder, positionMatrix, 1 - 0.5F, 0, 0);
		addWhiteVertex(builder, positionMatrix, 1 - 0.5F, -1, 0);

		addWhiteVertex(builder, positionMatrix, 1 - 0.5F, -1, 0);
		addWhiteVertex(builder, positionMatrix, 0.5F - 0.5F, -1.25F, 0);

		addWhiteVertex(builder, positionMatrix, 0.5F - 0.5F, -1.25F, 0);
		addWhiteVertex(builder, positionMatrix, 0 - 0.5F, -1, 0);

		addWhiteVertex(builder, positionMatrix, 0 - 0.5F, -1, 0);
		addWhiteVertex(builder, positionMatrix, 0 - 0.5F, 0, 0);
	}

	static void addWhiteVertex(VertexConsumer builder, Matrix4f positionMatrix, float x, float y, float z) {
		builder.vertex(positionMatrix, x, y, z).color(0.0f, 1.0f, 0.0f, 1.0f).endVertex();
	}

	static void addWhiteVertex(VertexConsumer builder, Matrix4f positionMatrix, double x, double y, double z) {
		addWhiteVertex(builder, positionMatrix, (float) x, (float) y, (float) z);
	}

}
