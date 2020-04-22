package bassebombecraft.rendering;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.ModConstants.HUD_TEXT_DISP;
import static bassebombecraft.ModConstants.TEAM_MEMBERS_TO_RENDER;
import static bassebombecraft.player.PlayerUtils.CalculatePlayerPosition;
import static bassebombecraft.rendering.RenderingUtils.renderHudTextBillboard;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import bassebombecraft.event.charm.CharmedMob;
import bassebombecraft.event.charm.CharmedMobsRepository;
import bassebombecraft.player.PlayerUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

/**
 * Implementation of the {@linkplain Renderer} for rendering charmed information
 * in the HUD item.
 */
@Deprecated
public class DefaultCharmedInfoRenderer implements EntityRenderer {

	/**
	 * Team label.
	 */
	static final String CHARMED_LABEL = "CHARMED";

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

		// get charmed entities
		CharmedMobsRepository repository = getBassebombeCraft().getCharmedMobsRepository();
		Stream<CharmedMob> charmedMobs = repository.get();
		int charmedSize = repository.size();

		// render basic info
		Vec3d textTranslation = new Vec3d(5, 1, 4);
		renderHudTextBillboard(translation, textTranslation, CHARMED_LABEL);
		renderHudTextBillboard(translation, textTranslation.add(0, -HUD_TEXT_DISP * 1, 0),
				"Number charmed: " + charmedSize);

		// create counter to use inside loop
		final AtomicInteger count = new AtomicInteger();

		// render members
		charmedMobs.forEach(c -> {
			int counter = count.incrementAndGet();

			// exit if enough members has been rendered
			if (counter > TEAM_MEMBERS_TO_RENDER)
				return;

			int disp = 1 + counter;
			LivingEntity charmedEntity = c.getEntity();
			String memberName = charmedEntity.getName().getUnformattedComponentText();
			int duration = c.getDuration();
			String text = "Mob: " + memberName + ", Charm duration: " + duration;
			renderHudTextBillboard(translation, textTranslation.add(0, -HUD_TEXT_DISP * disp, 0), text);
		});

	}

}
