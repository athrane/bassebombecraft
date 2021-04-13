package bassebombecraft.operator.sound;

import static bassebombecraft.operator.DefaultPorts.getFnGetLivingEntity1;
import static bassebombecraft.operator.Operators2.applyV;
import static net.minecraft.util.SoundCategory.NEUTRAL;

import java.util.Random;
import java.util.function.Function;
import java.util.function.Supplier;

import bassebombecraft.BassebombeCraft;
import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

/**
 * Generic implementation of the {@linkplain Operator2} interface which plays a
 * sound at the location of the entity.
 */
public class PlaySound2 implements Operator2 {

	/**
	 * Pitch variance.
	 */
	static final float PITCH_VARIANCE = 0.3F;

	/**
	 * Pitch base.
	 */
	static final float PITCH_BASE = 1.0F;

	/**
	 * Volume.
	 */
	static final float VOLUME = 1.0F;

	/**
	 * Function to get entity.
	 */
	Function<Ports, LivingEntity> fnGetEntity;

	/**
	 * Function to get sound.
	 */
	Supplier<SoundEvent> splGetSound;

	/**
	 * Constructor.
	 * 
	 * @param fnGetEntity function to get entity.
	 * @param splGetSound function to get sound player by operator.
	 */
	public PlaySound2(Function<Ports, LivingEntity> fnGetEntity, Supplier<SoundEvent> splGetSound) {
		this.fnGetEntity = fnGetEntity;
		this.splGetSound = splGetSound;
	}

	/**
	 * Constructor.
	 * 
	 * Instance is configured with living entity #1 as entity from ports.
	 * 
	 * @param splGetSound function to get sound player by operator.
	 */
	public PlaySound2(Supplier<SoundEvent> splGetSound) {
		this(getFnGetLivingEntity1(), splGetSound);
	}

	@Override
	public void run(Ports ports) {
		LivingEntity entity = applyV(fnGetEntity, ports);

		// get sound 
		SoundEvent sound = splGetSound.get();
		if(sound == null) return; 
		
		// get world
		World world = entity.getEntityWorld();
		
		// get random
		Random random = BassebombeCraft.getBassebombeCraft().getRandom();

		// calculcate pitch
		float pitch = PITCH_BASE + (random.nextFloat() - random.nextFloat()) * PITCH_VARIANCE;

		// invoke sound without player
		world.playSound((PlayerEntity) null, entity.getPosX(), entity.getPosY(), entity.getPosZ(), sound, NEUTRAL,
				VOLUME, pitch);
	}

}
