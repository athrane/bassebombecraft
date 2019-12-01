package bassebombecraft.rendering;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.ModConstants.HUD_TEXT_DISP;
import static bassebombecraft.ModConstants.TEAM_MEMBERS_TO_RENDER;
import static bassebombecraft.entity.EntityUtils.getTarget;
import static bassebombecraft.player.PlayerUtils.CalculatePlayerPosition;
import static bassebombecraft.rendering.RenderingUtils.renderHudTextBillboard;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import bassebombecraft.entity.EntityUtils;
import bassebombecraft.entity.commander.MobCommand;
import bassebombecraft.entity.commander.MobCommanderRepository;
import bassebombecraft.event.entity.team.TeamRepository;
import bassebombecraft.player.PlayerUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

/**
 * Implementation of the {@linkplain Renderer} for rendering team information in
 * the HUD item.
 */
public class DefaultTeamInfoRenderer implements EntityRenderer {

	/**
	 * Team label.
	 */
	static final String TEAM_LABEL = "Team";

	/**
	 * Renderer for rendering bounding box of an entity in the HUD Item.
	 */
	final static EntityRenderer boundingBoxRenderer = new DefaultBoundingBoxEntityRenderer();

	/**
	 * Renderer for rendering target of an entity in the HUD Item.
	 */
	final static EntityRenderer targetRenderer = new DefaultTargetEntityRenderer();

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
		Vec3d renderPos = RenderingUtils.getRenderPos();
		Vec3d translation = playerPos.subtract(renderPos);

		// get team
		TeamRepository teamRepository = getBassebombeCraft().getTeamRepository();
		Collection<LivingEntity> team = teamRepository.get(player);
		int teamSize = teamRepository.size(player);

		// get current command
		MobCommanderRepository commanderRepository = getBassebombeCraft().getMobCommanderRepository();
		MobCommand command = commanderRepository.getCommand(player);

		// render basic info
		Vec3d textTranslation = new Vec3d(4, 4, 4);
		renderHudTextBillboard(translation, textTranslation, "TEAM");
		renderHudTextBillboard(translation, textTranslation.add(0, -HUD_TEXT_DISP * 1, 0),
				"Commander command: " + command.getTitle());
		renderHudTextBillboard(translation, textTranslation.add(0, -HUD_TEXT_DISP * 2, 0), "Team size: " + teamSize);

		// create counter to use inside loop
		final AtomicInteger count = new AtomicInteger();

		// render members
		team.forEach(m -> {
			int counter = count.incrementAndGet();

			// exit if enough members has been rendered
			if (counter > TEAM_MEMBERS_TO_RENDER)
				return;

			int disp = 2 + counter;
			String memberName = m.getName().getUnformattedComponentText();
			String targetName = getTargetName(m);
			String text = "Member: " + memberName + ", Target: " + targetName;
			renderHudTextBillboard(translation, textTranslation.add(0, -HUD_TEXT_DISP * disp, 0), text);
		});

	}

	/**
	 * Get target name.
	 * 
	 * @param entity entity to resolved target name from.
	 * 
	 * @return target name.
	 */
	String getTargetName(LivingEntity entity) {
		
		// exit if entity has no target
		if (!EntityUtils.hasAliveTarget(entity))
			return "N/A";
		
		// get live target info
		LivingEntity target = getTarget(entity);
		return target.getName().getUnformattedComponentText();		
	}

}
