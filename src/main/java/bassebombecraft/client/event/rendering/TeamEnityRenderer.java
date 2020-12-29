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

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import bassebombecraft.client.rendering.rendertype.RenderTypes;
import bassebombecraft.event.entity.team.TeamRepository;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Matrix4f;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
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
	public static void handleRenderLivingEvent(Pre<PlayerEntity, PlayerModel<PlayerEntity>> event) {
		try {

			// exit if player is undefined
			if (!isClientSidePlayerDefined())
				return;

			// get player
			PlayerEntity player = getClientSidePlayer();

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
	static void render(MatrixStack matrixStack, LivingEntity entity) {
		// get render buffer
		IRenderTypeBuffer.Impl buffer = Minecraft.getInstance().getRenderTypeBuffers().getBufferSource();

		float height = entity.getHeight();
		renderText(matrixStack, buffer, 0, 0, "TEAM");
		renderText(matrixStack, buffer, 0, -10, getFirstRunningAiGoalName(entity));
		renderText(matrixStack, buffer, 0, -20, getFirstRunningAiTargetGoalName(entity));

		float w = (float) oscillate(-10, 10);
		matrixStack.push();
		matrixStack.translate(0, height, 0);
		matrixStack.rotate(Vector3f.YP.rotationDegrees(180));
		Matrix4f positionMatrix = matrixStack.getLast().getPositionMatrix();
		IVertexBuilder builder = buffer.getBuffer(RenderTypes.OVERLAY_LINES);
		renderTriangle(builder, positionMatrix);
		matrixStack.pop();

		// see: https://wiki.mcjty.eu/modding/index.php?title=Tut15_Ep15
		RenderSystem.disableDepthTest();
		buffer.finish(RenderTypes.OVERLAY_LINES);
	}

	static void renderText(MatrixStack matrixStack, IRenderTypeBuffer buffer, float x, float y, String text) {
		EntityRendererManager renderManager = Minecraft.getInstance().getRenderManager();
		FontRenderer fontrenderer = renderManager.getFontRenderer();

		matrixStack.push();
		matrixStack.scale(TEXT_SCALE, TEXT_SCALE, TEXT_SCALE);
		matrixStack.rotate(renderManager.getCameraOrientation());
		matrixStack.rotate(Vector3f.ZP.rotationDegrees(180));
		Matrix4f positionMatrix = matrixStack.getLast().getPositionMatrix();
		fontrenderer.renderString(text, x, y, TEXT_COLOR, false, positionMatrix, buffer, false, 0, 0xf000f0);
		matrixStack.pop();

	}

	/**
	 * Render shield.
	 */
	public static void renderTriangle(IVertexBuilder builder, Matrix4f positionMatrix) {

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

	static void addWhiteVertex(IVertexBuilder builder, Matrix4f positionMatrix, float x, float y, float z) {
		builder.pos(positionMatrix, x, y, z).color(0.0f, 1.0f, 0.0f, 1.0f).endVertex();
	}

	static void addWhiteVertex(IVertexBuilder builder, Matrix4f positionMatrix, double x, double y, double z) {
		addWhiteVertex(builder, positionMatrix, (float) x, (float) y, (float) z);
	}

}
