package bassebombecraft.item.action;

import java.util.Random;

import static bassebombecraft.BassebombeCraft.*;
import static bassebombecraft.event.projectile.RegisteredEntityTypes.SKULL_PROJECTILE;

import bassebombecraft.entity.projectile.GenericCompositeProjectileEntity;
import bassebombecraft.entity.projectile.GenericEggProjectile;
import bassebombecraft.entity.projectile.SkullProjectileEntity;
import bassebombecraft.projectile.action.ProjectileAction;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

/**
 * Implementation of the {@linkplain RightClickedItemAction} which shoot an egg
 * projectile which executes an {@linkplain ProjectileAction} on impact.
 */
@Deprecated
public class ShootGenericEggProjectile implements RightClickedItemAction {

	static final float VELOCITY = 3.0F;
	static final float INACCURANCY = 1.0F;
	static final SoundEvent SOUND = SoundEvents.ENTITY_EVOKER_CAST_SPELL;
	ProjectileAction action;

	/**
	 * Acceleration modifier.
	 */
	static final double ACCELERATION_MODIFIER = 0.1D;
	
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

		// calculate orientation
		Vector3d orientation = entity.getLook(1).scale(ACCELERATION_MODIFIER);
		
		GenericEggProjectile projectile = new GenericEggProjectile(world, entity, action);
		projectile.setPosition(entity.getPosX(), entity.getPosY() + entity.getEyeHeight(), entity.getPosZ());		
		projectile.shoot(orientation.x, orientation.y, orientation.z, VELOCITY, INACCURANCY);
		entity.playSound(SOUND, 1.0F, 1.0F / (random.nextFloat() * 0.4F + 0.8F));
		world.addEntity(projectile);
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		// NO-OP
	}

}
