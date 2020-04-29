package bassebombecraft.client.rendering;

import static bassebombecraft.BassebombeCraft.getProxy;
import static bassebombecraft.ModConstants.HUD_TEXT_DISP;
import static bassebombecraft.ModConstants.TEAM_MEMBERS_TO_RENDER;
import static bassebombecraft.client.rendering.RenderingUtils.renderHudTextBillboard;
import static bassebombecraft.player.PlayerUtils.CalculatePlayerPosition;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import bassebombecraft.event.entity.target.TargetRepository;
import bassebombecraft.player.PlayerUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

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
		PlayerEntity player = (PlayerEntity) entity;

		// get player position
		Vec3d playerPos = CalculatePlayerPosition(player, info.getPartialTicks());

		// calculate translation of text
		Vec3d renderPos = RenderingUtils.getRenderPos();
		Vec3d translation = playerPos.subtract(renderPos);

		// get targets
		TargetRepository repository = getProxy().getTargetRepository(player.getEntityWorld());
		Stream<LivingEntity> targets = repository.get(player);
		int targetsSize = repository.size(player);

		// get current commander target
		String commanderTargetName = getCommanderTargetName(player);

		// render basic info
		Vec3d textTranslation = new Vec3d(-3, 4, 4);
		renderHudTextBillboard(translation, textTranslation, TARGETS_LABEL);
		renderHudTextBillboard(translation, textTranslation.add(0, -HUD_TEXT_DISP * 1, 0),
				"Commander target: " + commanderTargetName);
		renderHudTextBillboard(translation, textTranslation.add(0, -HUD_TEXT_DISP * 2, 0),
				"Number targets: " + targetsSize);

		// create counter to use inside loop
		final AtomicInteger count = new AtomicInteger();

		// render members
		targets.forEach(m -> {
			int counter = count.incrementAndGet();

			// exit if enough members has been rendered
			if (counter > TEAM_MEMBERS_TO_RENDER)
				return;

			int disp = 2 + counter;
			String targetName = m.getName().getUnformattedComponentText();
			String text = "Target: " + targetName;
			renderHudTextBillboard(translation, textTranslation.add(0, -HUD_TEXT_DISP * disp, 0), text);
		});

	}

	/**
	 * Get commander target name.
	 * 
	 * @param player commander to resolved target name from.
	 * 
	 * @return commander target name.
	 */
	String getCommanderTargetName(PlayerEntity player) {

		// get commander target
		TargetRepository repository = getProxy().getTargetRepository(player.getEntityWorld());
		Optional<LivingEntity> optTarget = repository.getFirst(player);

		// exit if entity has no target
		if (!optTarget.isPresent())
			return "N/A";

		// get live target info
		LivingEntity target = optTarget.get();
		return target.getName().getUnformattedComponentText();
	}

}
