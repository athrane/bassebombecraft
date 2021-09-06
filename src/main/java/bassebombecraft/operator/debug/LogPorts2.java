package bassebombecraft.operator.debug;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;

import org.apache.logging.log4j.Logger;

import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;

/**
 * Implementation of the {@linkplain Operator2} interface which logs the content
 * of the ports.
 */
public class LogPorts2 implements Operator2 {

	/**
	 * Some header.
	 */
	String header;
	
	
	/**
	 * Constructor. 
	 * @param header some header.
	 */
	public LogPorts2(String header) {
		super();
		this.header = header;
	}

	@Override
	public void run(Ports ports) {
		Logger logger = getBassebombeCraft().getLogger();
		logger.info(header);		
		logger.info("Result="+ports.getResult());
		logger.info("Counter="+ports.getCounter());		
		logger.info("String1="+ports.getString1());				
		logger.info("String2="+ports.getString2());						
		logger.info("Double1="+ports.getDouble1());								
		logger.info("Aabb1="+ports.getAabb1());										
		logger.info("BlockPosition1="+ports.getBlockPosition1());												
		logger.info("BlockPosition2="+ports.getBlockPosition2());												
		logger.info("Color4f1="+ports.getColor4f1());												
		logger.info("Color4f2="+ports.getColor4f2());												
		logger.info("DamageSource1="+ports.getDamageSource1());												
		logger.info("EffectInstance1="+ports.getEffectInstance1());												
		logger.info("Entities1="+ports.getEntities1());												
		logger.info("Entity1="+ports.getEntity1());												
		logger.info("Entity2="+ports.getEntity2());												
		logger.info("LivingEntity1="+ports.getLivingEntity1());												
		logger.info("LivingEntity2="+ports.getLivingEntity2());														
	}

}
