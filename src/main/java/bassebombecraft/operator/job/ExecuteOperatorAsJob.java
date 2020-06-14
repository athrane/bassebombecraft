package bassebombecraft.operator.job;

import static bassebombecraft.operator.Operators2.run;

import bassebombecraft.event.job.Job;
import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;

/**
 * Implementation of the {@linkplain Job} interface which executes operators as
 * job on each update.
 * 
 * The used ports is reused (and thus updated) for all invocations of the
 * operators.
 *
 */
public class ExecuteOperatorAsJob implements Job {

	/**
	 * Operator ports.
	 */
	Ports ports;

	/**
	 * Operators for the job.
	 */
	Operator2[] ops;

	/**
	 * Constructor.
	 * 
	 * @param ports ports used by operators.
	 * @param ops   operators executed by the job.
	 */
	ExecuteOperatorAsJob(Ports ports, Operator2[] ops) {
		this.ops = ops;
		this.ports = ports;
	}

	@Override
	public void update() {
		run(ports, ops);
	}

	/**
	 * Factory method.
	 * 
	 * @param ports ports used by operators.
	 * @param ops   operators executed by the job.
	 * 
	 * @return ports.
	 */
	public static Job getInstance(Ports ports, Operator2[] ops) {
		return new ExecuteOperatorAsJob(ports, ops);
	}

}
