package bassebombecraft.operator.conditional;

import static bassebombecraft.BassebombeCraft.getProxy;
import static bassebombecraft.operator.DefaultPorts.getFnGetLivingEntity1;
import static bassebombecraft.operator.Operators2.applyV;

import java.util.function.Function;

import bassebombecraft.entity.EntityUtils;
import bassebombecraft.event.charm.ServerCharmedMobsRepository;
import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;

/**
 * Implementation of the {@linkplain Operator2} interface which updates the
 * result port as successful/unsuccessful depending on whether the the mob
 * entity is charmed or not, i.e. registered in
 * {@linkplain ServerCharmedMobsRepository}.
 */
public class IsEntityIsCharmed2 implements Operator2 {

	/**
	 * Function to get living entity.
	 */
	Function<Ports, LivingEntity> fnGetLivingEntity;

	/**
	 * Constructor.
	 * 
	 * @param fnGetLivingEntity function to get entity to test.
	 */
	public IsEntityIsCharmed2(Function<Ports, LivingEntity> fnGetLivingEntity) {
		this.fnGetLivingEntity = fnGetLivingEntity;
	}

	/**
	 * Constructor.
	 * 
	 * Instance is configured with living entity #1 from ports.
	 */
	public IsEntityIsCharmed2() {
		this(getFnGetLivingEntity1());
	}

	@Override
	public void run(Ports ports) {
		LivingEntity entity = applyV(fnGetLivingEntity, ports);

		// exit if entity isn't a mob entity
		if (!EntityUtils.isTypeMobEntity(entity))
			return;

		// type cast
		Mob mobEntity = (Mob) entity;

		// test if entity is charmed
		if (getProxy().getServerCharmedMobsRepository().contains(mobEntity))
			ports.setResultAsSucces();
		else
			ports.setResultAsFailed();
	}
}
