package bassebombecraft.projectile;

import bassebombecraft.projectile.action.NullAction;
import bassebombecraft.projectile.action.ProjectileAction;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class GenericEggProjectile extends EntityThrowable {
	
	public final static String PROJECTILE_NAME = "EggProjectile ";
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
	protected void onImpact(MovingObjectPosition movObjPos) {
		if (movObjPos.entityHit != null) {
			movObjPos.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), 0.0F);
		}

		// exit if on server
		if (this.worldObj.isRemote) return;
		
		// execute behaviour
		behaviour.execute(this, worldObj, movObjPos);
				
		double d0 = 0.08D;
		for (int j = 0; j < 8; ++j) {
			this.worldObj.spawnParticle(EnumParticleTypes.ITEM_CRACK, this.posX, this.posY, this.posZ,
					((double) this.rand.nextFloat() - 0.5D) * 0.08D, ((double) this.rand.nextFloat() - 0.5D) * 0.08D,
					((double) this.rand.nextFloat() - 0.5D) * 0.08D, new int[] { Item.getIdFromItem(Items.egg) });
		}

		// destroy this projectile 
		if (!this.worldObj.isRemote) {
			this.setDead();
		}
	}
}