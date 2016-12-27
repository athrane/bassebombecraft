package bassebombecraft.projectile;

import bassebombecraft.projectile.action.NullAction;
import bassebombecraft.projectile.action.ProjectileAction;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class GenericEggProjectile extends EntityThrowable {
	
	public final static String PROJECTILE_NAME = "EggProjectile";
	ProjectileAction behaviour = new NullAction();
	
	public GenericEggProjectile(World worldIn) {
		super(worldIn);
	}

	public GenericEggProjectile(World worldIn, EntityLivingBase entity, ProjectileAction behaviour) {
		super(worldIn, entity);
		setBehaviour(behaviour);
	}

	public GenericEggProjectile(World worldIn, double x, double y, double z) {
		super(worldIn, x, y, z);
	}

	public void setBehaviour(ProjectileAction behaviour) {
		this.behaviour = behaviour;
	}
	
	/**
	 * Called when this EntityThrowable hits a block or entity.
	 */
	protected void onImpact(RayTraceResult movObjPos) {

		// get world
		World worldObj = this.getEntityWorld();
		
		// exit if on server
		if (worldObj.isRemote) return;
		
		// execute behaviour
		behaviour.execute(this, worldObj, movObjPos);
				
		for (int j = 0; j < 8; ++j) {
			worldObj.spawnParticle(EnumParticleTypes.ITEM_CRACK, this.posX, this.posY, this.posZ,
					((double) this.rand.nextFloat() - 0.5D) * 0.08D, ((double) this.rand.nextFloat() - 0.5D) * 0.08D,
					((double) this.rand.nextFloat() - 0.5D) * 0.08D, new int[] { Item.getIdFromItem(Items.EGG) });
		}

		// destroy this projectile 
		if (!worldObj.isRemote) {
			this.setDead();
		}
	}
}