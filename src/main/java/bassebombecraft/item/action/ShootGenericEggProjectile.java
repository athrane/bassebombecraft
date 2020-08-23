package bassebombecraft.item.action;

import java.util.Random;

import static bassebombecraft.BassebombeCraft.*;

import bassebombecraft.entity.projectile.GenericEggProjectile;
import bassebombecraft.projectile.action.ProjectileAction;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

/**
 * Implementation of the {@linkplain RightClickedItemAction} which shoot an egg
 * projectile which executes an {@linkplain ProjectileAction} on impact.
 */
public class ShootGenericEggProjectile implements RightClickedItemAction {

	static final float PITCH_OFFSET = 0.0F;
	static final float VELOCITY = 3.0F;
	static final float INACCURANCY = 1.0F;
	static final SoundEvent SOUND = SoundEvents.ENTITY_EVOKER_CAST_SPELL;
	ProjectileAction action;

	/**
	 * GenericShootEggProjectile constructor.
	 * 
	 * @param action item action which is executed on impact.
	 */
	public ShootGenericEggProjectile(ProjectileAction action) {
		this.action = action;
	}

	@Override
	public void onRightClick(World world, LivingEntity entity) {
		Random random = getBassebombeCraft().getRandom();

		GenericEggProjectile projectile = new GenericEggProjectile(world, entity, action);
		projectile.shoot(entity, entity.rotationPitch, entity.rotationYaw, PITCH_OFFSET, VELOCITY, INACCURANCY);
		entity.playSound(SOUND, 1.0F, 1.0F / (random.nextFloat() * 0.4F + 0.8F));
		world.addEntity(projectile);
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		// NO-OP
	}

}
