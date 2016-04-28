package bassebombecraft.item.action;

import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

/**
 * Implementation of the {@linkplain RightClickedItemAction} which shoot a large fireball. 
 */
public class ShootWitherSkull implements RightClickedItemAction {

	private static final String SOUND = "mob.ghast.fireball";
	static Random random = new Random();	
	
	@Override
	public void onRightClick(World world, EntityLivingBase entity) {
        Vec3 v3 = entity.getLook(1);
        EntityWitherSkull projectile = new EntityWitherSkull(world, entity.posX, entity.posY + entity.getEyeHeight(), entity.posZ, v3.xCoord, v3.yCoord, v3.zCoord);
        projectile.shootingEntity = entity;
        world.playSoundAtEntity(entity, SOUND, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
        world.spawnEntityInWorld(projectile);		
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		// NOP 
	}	

}
