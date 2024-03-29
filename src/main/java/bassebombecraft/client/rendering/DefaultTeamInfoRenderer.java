package bassebombecraft.client.rendering;

import static bassebombecraft.BassebombeCraft.getProxy;
import static bassebombecraft.ModConstants.TEAM_MEMBERS_TO_RENDER;
import static bassebombecraft.entity.EntityUtils.getTarget;
import static bassebombecraft.player.PlayerUtils.CalculatePlayerPosition;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

import bassebombecraft.entity.EntityUtils;
import bassebombecraft.entity.commander.MobCommand;
import bassebombecraft.entity.commander.MobCommanderRepository;
import bassebombecraft.event.entity.team.TeamRepository;
import bassebombecraft.player.PlayerUtils;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

/**
 * Implementation of the {@linkplain Renderer} for rendering team information in
 * the HUD item.
 */
@Deprecated
public class DefaultTeamInfoRenderer implements EntityRenderer {

	/**
	 * Team label.
	 */
	static final String TEAM_LABEL = "TEAM";

	@Override
	public void render(LivingEntity entity, RenderingInfo info) {

		// exit if entity isn't player
		if (!PlayerUtils.isTypePlayerEntity(entity))
			return;

		// typecast
		Player player = (Player) entity;

		// get player position
		Vec3 playerPos = CalculatePlayerPosition(player, info.getPartialTicks());

		// calculate translation of text
		Vec3 renderPos = RenderingUtils.getRenderPos();
		Vec3 translation = playerPos.subtract(renderPos);

		// get team
		TeamRepository repository = getProxy().getServerTeamRepository();
		Collection<LivingEntity> team = repository.get(player);
		int teamSize = repository.size(player);

		// get current commander command
		MobCommanderRepository commanderRepository = getProxy().getServerMobCommanderRepository();
		MobCommand command = commanderRepository.getCommand(player);

		// render basic info
		Vec3 textTranslation = new Vec3(5, 4, 4);
		// renderHudTextBillboard(translation, textTranslation, TEAM_LABEL);
		// renderHudTextBillboard(translation, textTranslation.add(0, -HUD_TEXT_DISP *
		// 1, 0),
		// "Commander command: " + command.getTitle());
		// renderHudTextBillboard(translation, textTranslation.add(0, -HUD_TEXT_DISP *
		// 2, 0), "Team size: " + teamSize);

		// create counter to use inside loop
		final AtomicInteger count = new AtomicInteger();

		// render members
		team.forEach(m -> {
			int counter = count.incrementAndGet();

			// exit if enough members has been rendered
			if (counter > TEAM_MEMBERS_TO_RENDER)
				return;

			int disp = 2 + counter;
			String memberName = m.getName().getContents();
			String targetName = getTargetName(m);
			String text = "Member: " + memberName + ", Target: " + targetName;
			// renderHudTextBillboard(translation, textTranslation.add(0, -HUD_TEXT_DISP *
			// disp, 0), text);
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
		return target.getName().getContents();
	}

}
