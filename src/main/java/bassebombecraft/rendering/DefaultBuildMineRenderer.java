package bassebombecraft.rendering;

import bassebombecraft.item.book.BuildMineBook;
import bassebombecraft.player.PlayerUtils;
import net.minecraft.entity.LivingEntity;

/**
 * Implementation of the {@linkplain Renderer} for rendering construction of
 * mine in the {@linkplain BuildMineBook}.
 */
public class DefaultBuildMineRenderer implements EntityRenderer {

	@Override
	public void render(LivingEntity entity, RenderingInfo info) {

		// exit if entity isn't player
		if (!PlayerUtils.isTypePlayerEntity(entity))
			return;

		
		
	}

}
