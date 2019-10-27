package bassebombecraft.event.rendering;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.ModConstants.TARGETING_OVERLAY_ITEM;
import static bassebombecraft.ModConstants.TEXT_BILLBOARD_ROTATION;
import static bassebombecraft.ModConstants.TEXT_COLOR;
import static bassebombecraft.ModConstants.VERSION;
import static bassebombecraft.entity.EntityUtils.getAliveTarget;
import static bassebombecraft.player.PlayerUtils.CalculatePlayerPosition;
import static bassebombecraft.player.PlayerUtils.getPlayer;
import static bassebombecraft.player.PlayerUtils.isItemInHotbar;
import static bassebombecraft.player.PlayerUtils.isPlayerDefined;
import static bassebombecraft.rendering.RenderingUtils.renderHudTextBillboard;
import static bassebombecraft.rendering.RenderingUtils.renderLineBillboard;
import static bassebombecraft.rendering.RenderingUtils.renderRectangleBillboard;
import static bassebombecraft.rendering.RenderingUtils.renderTextBillboard;
import static bassebombecraft.rendering.RenderingUtils.renderTextBillboardV2;
import static bassebombecraft.rendering.RenderingUtils.renderTriangleBillboard;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import javax.vecmath.Vector4f;

import bassebombecraft.entity.EntityUtils;
import bassebombecraft.entity.commander.MobCommand;
import bassebombecraft.entity.commander.MobCommanderRepository;
import bassebombecraft.event.charm.CharmedMob;
import bassebombecraft.event.charm.CharmedMobsRepository;
import bassebombecraft.event.entity.target.TargetedEntitiesRepository;
import bassebombecraft.event.entity.team.TeamRepository;
import bassebombecraft.rendering.RenderingUtils;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Event handler for additional rendering.
 */
@Mod.EventBusSubscriber
public class RenderingEventHandler {

	/**
	 * Number of targets to render.
	 */
	static final int TARGETS_TO_RENDER = 7;

	/**
	 * Number of targets to render.
	 */
	static final int TEAM_MEMBERS_TO_RENDER = 7;

	/**
	 * HUD displacement of text.
	 */
	static final double HUD_TEXT_DISP = 0.3;

	/**
	 * Random constant to put text above the hotbar.
	 */
	static final int TEXT_RANDOM_Y_OFFSET = 31 + 4 + 12;

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

	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public static void handleEntityViewRenderEvent(EntityViewRenderEvent event) {
		// TODO: Can this replace loops for rendering of charmed, team and targets?
		return;
	}

	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public static void handleRenderLivingEvent(RenderLivingEvent event) {
		// TODO: Can this replace loops for rendering of charmed, team and targets?
	}

	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public static void handleRenderGameOverlayEvent(RenderGameOverlayEvent event) {

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

		// get font renderer
		Minecraft mc = Minecraft.getInstance();
		FontRenderer fontRenderer = mc.fontRenderer;

		String text = "Hello technical info!";
		MainWindow window = event.getWindow();
		int x = (window.getScaledWidth() - fontRenderer.getStringWidth(text)) / 2;
		int y = window.getScaledHeight() - TEXT_RANDOM_Y_OFFSET - 70;
		fontRenderer.drawString(text, x, y, TEXT_COLOR);
		fontRenderer.drawString("Position(PP): " + playerPos, x, y + 10, TEXT_COLOR);
		fontRenderer.drawString("Look: " + player.getLookVec(), x, y + 20, TEXT_COLOR);

		Vec3d renderPos = RenderingUtils.getRenderPos();
		fontRenderer.drawString("Render position(RP): " + renderPos, x, y + 30, TEXT_COLOR);

		Vec3d translation = playerPos.subtract(renderPos);
		fontRenderer.drawString("PP-RP: " + translation, x, y + 40, TEXT_COLOR);

		Vec3d playerView = new Vec3d(mc.getRenderManager().playerViewX, mc.getRenderManager().playerViewY, 0);
		fontRenderer.drawString("Plaver view: " + playerView, x, y + 50, TEXT_COLOR);

		final Entity rve = mc.getRenderViewEntity();
		Vec3d rve2 = new Vec3d(rve.rotationPitch, rve.rotationYaw, 0);
		fontRenderer.drawString("Render view entity: " + rve2, x, y + 60, TEXT_COLOR);
	}

	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public static void handleRenderWolrdLastEvent(RenderWorldLastEvent event) {

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

		try {
			// render
			renderCharmedEntities(player, playerPos);
			renderTeamEntities(player, playerPos);
			renderTargetedEntities(player, playerPos);
			Vec3d renderPos = RenderingUtils.getRenderPos();
			Vec3d translation = playerPos.subtract(renderPos);
			renderCompass(translation);
			renderHudVersionInfo(translation);
			renderTeamInfo(player, translation);
			renderTargetsInfo(player, translation);
			renderCharmedInfo(translation);

		} catch (Exception e) {
			getBassebombeCraft().reportAndLogException(e);
		}
	}

	static void renderTargetsInfo(PlayerEntity player, Vec3d translation) {
		TargetedEntitiesRepository targetRepository = getBassebombeCraft().getTargetedEntitiesRepository();
		Stream<LivingEntity> targets = targetRepository.get(player);

		Vec3d textTranslation = new Vec3d(-8, 4, 4);
		renderHudTextBillboard(translation, textTranslation, "TARGETS");

		// create counter to use inside loop
		final AtomicInteger count = new AtomicInteger();

		// render members
		targets.forEach(m -> {
			int counter = count.incrementAndGet();

			// exit if enough members has been rendered
			if (counter > TARGETS_TO_RENDER)
				return;

			int disp = 0 + counter;
			String targetName = m.getName().getUnformattedComponentText();
			String text = "Target: " + targetName;
			renderHudTextBillboard(translation, textTranslation.add(0, -HUD_TEXT_DISP * disp, 0), text);
		});
	}

	static void renderCharmedInfo(Vec3d translation) {
		// Render HUD charmed info
		Vec3d textTranslation = new Vec3d(8, 0, 4);
		renderHudTextBillboard(translation, textTranslation, "CHARMED");
		renderHudTextBillboard(translation, textTranslation.add(0, -HUD_TEXT_DISP * 1, 0), "Numbers: ");
		renderHudTextBillboard(translation, textTranslation.add(0, -HUD_TEXT_DISP * 2, 0), "Timeout #1: ");
		renderHudTextBillboard(translation, textTranslation.add(0, -HUD_TEXT_DISP * 3, 0), "Timeout #2: ");
	}

	static void renderTeamInfo(PlayerEntity player, Vec3d translation) {
		TeamRepository teamRepository = getBassebombeCraft().getTeamRepository();
		Collection<LivingEntity> team = teamRepository.get(player);
		int teamSize = teamRepository.size(player);
		MobCommanderRepository commanderRepository = getBassebombeCraft().getMobCommanderRepository();
		MobCommand command = commanderRepository.getCommand(player);

		Vec3d textTranslation = new Vec3d(8, 4, 4);
		renderHudTextBillboard(translation, textTranslation, "TEAM");
		renderHudTextBillboard(translation, textTranslation.add(0, -HUD_TEXT_DISP * 1, 0),
				"Command: " + command.getTitle());
		renderHudTextBillboard(translation, textTranslation.add(0, -HUD_TEXT_DISP * 2, 0), "Size: " + teamSize);

		// create counter to use inside loop
		final AtomicInteger count = new AtomicInteger();

		// render members
		team.forEach(m -> {
			int counter = count.incrementAndGet();

			// exit if enough members has been rendered
			if (counter > TEAM_MEMBERS_TO_RENDER)
				return;

			int disp = 2 + counter;
			Optional<LivingEntity> optTarget = Optional.ofNullable(getAliveTarget(m));
			String memberName = m.getName().getUnformattedComponentText();
			String targetName = optTarget.map(t -> t.getName().getUnformattedComponentText()).orElse("N/A");
			String text = "Member: " + memberName + ", Target: " + targetName;
			renderHudTextBillboard(translation, textTranslation.add(0, -HUD_TEXT_DISP * disp, 0), text);
		});

	}

	static void renderHudVersionInfo(Vec3d translation) {
		renderTextBillboardV2(translation.add(5, 5, 0), "HUD // BasseBombeCraft, version " + VERSION,
				TEXT_BILLBOARD_ROTATION);
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
		Vec3d entityPos = entity.getBoundingBox().getCenter();
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
		Vec3d entityPos = entity.getBoundingBox().getCenter();
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
		Vec3d targetPos = target.getBoundingBox().getCenter();
		renderRectangleBillboard(playerPos, targetPos);
		renderTextBillboard(playerPos, targetPos, TARGET_LABEL, TEXT_BILLBOARD_ROTATION);
	}

	static void renderTargetedEntity(LivingEntity entity, LivingEntity target, Vec3d playerPos) {
		Vec3d entityPos = entity.getBoundingBox().getCenter();
		Vec3d targetPos = target.getBoundingBox().getCenter();
		renderRectangleBillboard(playerPos, targetPos);
		renderTextBillboard(playerPos, targetPos, TARGET_LABEL, TEXT_BILLBOARD_ROTATION);
		renderLineBillboard(playerPos, entityPos, targetPos);
	}

	/**
	 * Render compass.
	 * 
	 * @param translation translation vector.
	 */
	static void renderCompass(Vec3d translation) {
		Vec3d ct = translation.add(5, 0, 0);
		Vector4f br1 = new Vector4f(0.0F, 1.0F, 0.0F, 0.0F);
		RenderingUtils.renderRotatedTextBillboard(ct, br1, "-E-");

		Vector4f br2 = new Vector4f(0.0F, 1.0F, 0.0F, 45);
		RenderingUtils.renderRotatedTextBillboard(ct, br2, "-NE-");

		Vector4f br3 = new Vector4f(0.0F, 1.0F, 0.0F, 90);
		RenderingUtils.renderRotatedTextBillboard(ct, br3, "-N-");

		Vector4f br4 = new Vector4f(0.0F, 1.0F, 0.0F, 90 + 45);
		RenderingUtils.renderRotatedTextBillboard(ct, br4, "-NW-");

		Vector4f br5 = new Vector4f(0.0F, 1.0F, 0.0F, 180);
		RenderingUtils.renderRotatedTextBillboard(ct, br5, "-W-");

		Vector4f br6 = new Vector4f(0.0F, 1.0F, 0.0F, 180 + 45);
		RenderingUtils.renderRotatedTextBillboard(ct, br6, "-SW-");

		Vector4f br7 = new Vector4f(0.0F, 1.0F, 0.0F, 270);
		RenderingUtils.renderRotatedTextBillboard(ct, br7, "-S-");

		Vector4f br8 = new Vector4f(0.0F, 1.0F, 0.0F, 270 + 45);
		RenderingUtils.renderRotatedTextBillboard(ct, br8, "-SE-");
	}

}
