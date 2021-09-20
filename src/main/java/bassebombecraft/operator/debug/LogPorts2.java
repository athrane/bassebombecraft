package bassebombecraft.operator.debug;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.operator.Operators2.createDebugInfo;

import org.apache.logging.log4j.Logger;

import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;

/**
 * Implementation of the {@linkplain Operator2} interface which logs the content
 * of the ports as a INFO statement.
 */
public class LogPorts2 implements Operator2 {

	/**
	 * Some header.
	 */
	String header;

	/**
	 * Constructor.
	 * 
	 * @param header some header.
	 */
	public LogPorts2(String header) {
		super();
		this.header = header;
	}

	@Override
	public void run(Ports ports) {
		Logger logger = getBassebombeCraft().getLogger();
		String debugInfo = createDebugInfo(header, ports);
		logger.info(debugInfo);
	}

}
