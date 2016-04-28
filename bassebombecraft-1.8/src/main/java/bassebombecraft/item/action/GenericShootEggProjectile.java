package bassebombecraft.item.action;

import java.util.Random;

import bassebombecraft.projectile.GenericEggProjectile;
import bassebombecraft.projectile.action.ProjectileAction;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

/**
 * Implementation of the {@linkplain RightClickedItemAction} which shoot an egg projectile
 * which executes an {@linkplain ProjectileAction} on impact.
 */
public class GenericShootEggProjectile implements RightClickedItemAction {

	private static final String SOUND = "mob.chicken.say";
	static Random random = new Random();
	ProjectileAction action;

	/**
	 * GenericShootEggProjectile constructor.
	 * 
	 * @param action
	 *            item action which is executed on impact.
	 */
	public GenericShootEggProjectile(ProjectileAction action) {
		this.action = action;
	}

	@Override
	public void onRightClick(World world, EntityLivingBase entity) {
		Vec3 v3 = entity.getLook(1);

		GenericEggProjectile projectile = new GenericEggProjectile(world, entity, action);
		world.playSoundAtEntity(entity, SOUND, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
		world.spawnEntityInWorld(projectile);
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		// NO-OP
	}

}
