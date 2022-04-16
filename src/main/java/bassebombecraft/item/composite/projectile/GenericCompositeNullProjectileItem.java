package bassebombecraft.item.composite.projectile;

import static bassebombecraft.ModConstants.NULL_I18N_ARGS;
import static bassebombecraft.item.ItemUtils.resolveCompositeItemTypeFromString;
import static net.minecraft.util.text.TextFormatting.DARK_BLUE;
import staticnet.minecraft.ChatFormattingg.GREEN;

import java.util.List;

import javax.annotation.Nullable;

import bassebombecraft.config.ItemConfig;
import bassebombecraft.config.ProjectileEntityConfig;
import bassebombecraft.item.composite.GenericCompositeNullItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;
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
	public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip,
			TooltipFlag flagIn) {
		String typeName = resolveCompositeItemTypeFromString(this);
		tooltip.add(new TranslatableComponent(GREEN + this.tooltip));
		tooltip.add(new TranslatableComponent("genericcompositenullitem.type", typeName));
		tooltip.add(new TranslatableComponent("genericcompositenullprojectileitem.damage",
				projectileConfig.damage.get()));
		tooltip.add(
				new TranslatableComponent("genericcompositenullprojectileitem.force", projectileConfig.force.get()));
		tooltip.add(new TranslatableComponent("genericcompositenullprojectileitem.gravity",
				projectileConfig.gravity.get()));
		tooltip.add(new TranslatableComponent("genericcompositenullprojectileitem.inaccuracy",
				projectileConfig.inaccuracy.get()));
		tooltip.add(new TranslatableComponent("genericcompositenullitem.usage", NULL_I18N_ARGS).withStyle(DARK_BLUE));
	}

}
