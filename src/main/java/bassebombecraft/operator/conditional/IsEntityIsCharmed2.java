package bassebombecraft.operator.conditional;

import static bassebombecraft.BassebombeCraft.getProxy;

import bassebombecraft.event.charm.CharmedMobsRepository;
import bassebombecraft.event.charm.ServerCharmedMobsRepository;
import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import net.minecraft.entity.MobEntity;

/**
 * Implementation of the {@linkplain Operator2} interface which updates the
 * result port as successful if the entity is charmed, i.e. registered in
 * {@linkplain ServerCharmedMobsRepository}.
 */
public class IsEntityIsCharmed2 implements Operator2 {

	@Override
	public Ports run(Ports ports) {
		
		// get entity
		MobEntity mobEntity = (MobEntity) ports.getLivingEntity1();
		
		// get repository
		CharmedMobsRepository repository = getProxy().getServerCharmedMobsRepository();
		
		// test if entity is charmed
		if (repository.contains(mobEntity))
			ports.setResultAsSucces();
		else
			ports.setResultAsFailed();

		return ports;
	}
}
