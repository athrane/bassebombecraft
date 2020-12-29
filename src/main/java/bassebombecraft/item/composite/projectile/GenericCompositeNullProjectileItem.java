package bassebombecraft.item.composite.projectile;

import static net.minecraft.util.text.TextFormatting.*;

import java.util.List;

import javax.annotation.Nullable;

import bassebombecraft.config.ItemConfig;
import bassebombecraft.config.ProjectileEntityConfig;
import bassebombecraft.item.composite.GenericCompositeNullItem;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Sub class of {@linkplain GenericCompositeNullItem} which supports rendering a
 * bit more meta data about the projectile in the tool tip.
 * 
 * The object implements no logic when the item is right clicked.
 * 
 * Abstract class intended to extended by sub classes.
 */
public abstract class GenericCompositeNullProjectileItem extends GenericCompositeNullItem {

	/**
	 * Projectile configuration, contains meta data used in tool tip.
	 */
	ProjectileEntityConfig projectileConfig;

	/**
	 * constructor.
	 * 
	 * @param config           item configuration.
	 * @param projectileConfig projectile configuration.
	 */
	public GenericCompositeNullProjectileItem(ItemConfig config, ProjectileEntityConfig projectileConfig) {
		super(config);
		this.projectileConfig = projectileConfig;
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip,
			ITooltipFlag flagIn) {
		tooltip.add(new TranslationTextComponent(GREEN + this.tooltip));
		tooltip.add(new TranslationTextComponent("desc.bassebombecraft.projectile.damage", projectileConfig.damage.get()));
		tooltip.add(new TranslationTextComponent("desc.bassebombecraft.projectile.force", projectileConfig.force.get()));
		tooltip.add(new TranslationTextComponent("desc.bassebombecraft.projectile.gravity", projectileConfig.gravity.get()));
		tooltip.add(new TranslationTextComponent("desc.bassebombecraft.projectile.inaccuracy", projectileConfig.inaccuracy.get()));		
	}

}
