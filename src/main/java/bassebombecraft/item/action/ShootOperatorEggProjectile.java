package bassebombecraft.item.action;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;

import java.util.Random;

import bassebombecraft.operator.Operator;
import bassebombecraft.operator.Operators;
import bassebombecraft.projectile.OperatorEggProjectile;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

/**
 * Implementation of the {@linkplain RightClickedItemAction} which shoot an egg
 * projectile which executes an {@linkplain Operator} on impact.
 */
public class ShootOperatorEggProjectile implements RightClickedItemAction {

	static final float PITCH_OFFSET = 0.0F;
	static final float VELOCITY = 3.0F;
	static final float INACCURANCY = 1.0F;
	static final SoundEvent SOUND = SoundEvents.ENTITY_EVOKER_CAST_SPELL;

	/**
	 * Operators to execution.
	 */
	Operators operators;

	/**
	 * GenericOperatorShootEggProjectile constructor.
	 * 
	 * @param operators operators which is executed on impact.
	 */
	public ShootOperatorEggProjectile(Operators operators) {
		this.operators = operators;
	}

	@Override
	public void onRightClick(World world, LivingEntity entity) {
		Random random = getBassebombeCraft().getRandom();

		OperatorEggProjectile projectile = new OperatorEggProjectile(world, entity, operators);
		projectile.shoot(entity, entity.rotationPitch, entity.rotationYaw, PITCH_OFFSET, VELOCITY, INACCURANCY);
		entity.playSound(SOUND, 1.0F, 1.0F / (random.nextFloat() * 0.4F + 0.8F));
		world.addEntity(projectile);
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		// NO-OP
	}

}
