package bassebombecraft.item.action;

import java.util.Random;

import bassebombecraft.projectile.GenericEggProjectile;
import bassebombecraft.projectile.action.ProjectileAction;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * Implementation of the {@linkplain RightClickedItemAction} which shoot an egg projectile
 * which executes an {@linkplain ProjectileAction} on impact.
 */
public class GenericShootEggProjectile implements RightClickedItemAction {

	static final SoundEvent SOUND = SoundEvents.ENTITY_CHICKEN_HURT;	
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
		Vec3d v3 = entity.getLook(1);

		GenericEggProjectile projectile = new GenericEggProjectile(world, entity, action);
		entity.playSound(SOUND, 1.0F, 1.0F / (random.nextFloat() * 0.4F + 0.8F));
		world.spawnEntity(projectile);
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		// NO-OP
	}

}
