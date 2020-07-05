package bassebombecraft.operator.entity;

import static bassebombecraft.BassebombeCraft.getProxy;

import bassebombecraft.event.charm.ServerCharmedMobsRepository;
import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import net.minecraft.entity.MobEntity;

/**
 * Implementation of the {@linkplain Operator2} interface which removes entity
 * from the server charmed mob repository, i.e.
 * {@linkplain ServerCharmedMobsRepository}.
 * 
 * Sets the result port to true if removal succeeds.
 * 
 * The charm is removed from living entity #1 in the ports.
 */
public class RemoveCharm2 implements Operator2 {

	@Override
	public Ports run(Ports ports) {
		
		// remove from repository
		MobEntity mobEntity = (MobEntity) ports.getLivingEntity1();
		getProxy().getServerCharmedMobsRepository().remove(mobEntity);

		// update result port
		ports.setResultAsSucces();
		return ports;
	}

}
