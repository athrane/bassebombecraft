package bassebombecraft.operator.entity;

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
 * Implementation of the {@linkplain Operator2} interface which removes entity
 * from the server charmed mob repository, i.e.
 * {@linkplain ServerCharmedMobsRepository}.
 * 
 * Sets the result port to true if removal succeeds.
 */
public class RemoveCharm2 implements Operator2 {

	/**
	 * Function to get living entity.
	 */
	Function<Ports, LivingEntity> fnGetLivingEntity;

	/**
	 * Constructor.
	 * 
	 * @param fnGetLivingEntity function to get entity to test.
	 */
	public RemoveCharm2(Function<Ports, LivingEntity> fnGetLivingEntity) {
		this.fnGetLivingEntity = fnGetLivingEntity;
	}

	/**
	 * Constructor.
	 * 
	 * Instance is configured with living entity #1 from ports.
	 */
	public RemoveCharm2() {
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

		// remove from repository
		getProxy().getServerCharmedMobsRepository().remove(mobEntity);

		// update result port
		ports.setResultAsSucces();
	}

}
