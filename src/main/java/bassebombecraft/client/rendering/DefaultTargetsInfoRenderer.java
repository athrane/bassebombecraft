package bassebombecraft.client.rendering;

import static bassebombecraft.BassebombeCraft.getProxy;
import static bassebombecraft.ModConstants.TEAM_MEMBERS_TO_RENDER;
import static bassebombecraft.player.PlayerUtils.CalculatePlayerPosition;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import bassebombecraft.event.entity.target.TargetRepository;
import bassebombecraft.player.PlayerUtils;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

/**
 * Implementation of the {@linkplain Renderer} for rendering target information
 * in the HUD item.
 */
@Deprecated
public class DefaultTargetsInfoRenderer implements EntityRenderer {

	/**
	 * Targets label.
	 */
	static final String TARGETS_LABEL = "TARGETS";

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

		// get targets
		TargetRepository repository = getProxy().getServerTargetRepository();
		Stream<LivingEntity> targets = repository.get(player);
		int targetsSize = repository.size(player);

		// get current commander target
		String commanderTargetName = getCommanderTargetName(player);

		// render basic info
		Vec3 textTranslation = new Vec3(-3, 4, 4);
		// renderHudTextBillboard(translation, textTranslation, TARGETS_LABEL);
		// renderHudTextBillboard(translation, textTranslation.add(0, -HUD_TEXT_DISP *
		// 1, 0),
		// "Commander target: " + commanderTargetName);
		// renderHudTextBillboard(translation, textTranslation.add(0, -HUD_TEXT_DISP *
		// 2, 0),
		// "Number targets: " + targetsSize);

		// create counter to use inside loop
		final AtomicInteger count = new AtomicInteger();

		// render members
		targets.forEach(m -> {
			int counter = count.incrementAndGet();

			// exit if enough members has been rendered
			if (counter > TEAM_MEMBERS_TO_RENDER)
				return;

			int disp = 2 + counter;
			String targetName = m.getName().getContents();
			String text = "Target: " + targetName;
			// renderHudTextBillboard(translation, textTranslation.add(0, -HUD_TEXT_DISP *
			// disp, 0), text);
		});

	}

	/**
	 * Get commander target name.
	 * 
	 * @param player commander to resolved target name from.
	 * 
	 * @return commander target name.
	 */
	String getCommanderTargetName(Player player) {

		// get commander target
		TargetRepository repository = getProxy().getServerTargetRepository();
		Optional<LivingEntity> optTarget = repository.getFirst(player);

		// exit if entity has no target
		if (!optTarget.isPresent())
			return "N/A";

		// get live target info
		LivingEntity target = optTarget.get();
		return target.getName().getContents();
	}

}
