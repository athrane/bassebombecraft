package bassebombecraft.operator.job;

import static bassebombecraft.operator.Operators2.run;

import bassebombecraft.event.job.Job;
import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;

/**
 * Implementation of the {@linkplain Job} interface which executes an embedded
 * operator as a job on each update.
 * 
 * The ports is reused for all invocations of the job (and thus the operator).
 */
public class ExecuteOperatorAsJob2 implements Job {

	/**
	 * Operator ports.
	 */
	Ports ports;

	/**
	 * Operator for the job.
	 */
	Operator2 operator;

	/**
	 * Constructor.
	 * 
	 * @param ports ports used by operator.
	 * @param operator   operators executed by the job.
	 */
	public ExecuteOperatorAsJob2(Ports ports, Operator2 operator) {
		this.operator = operator;
		this.ports = ports;
	}

	@Override
	public void update() {
		run(ports, operator);
	}

}
