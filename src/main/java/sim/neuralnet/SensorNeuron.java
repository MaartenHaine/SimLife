package sim.neuralnet;

import sim.entities.Prey;

/**
 * @immutable
 */
public abstract class SensorNeuron implements Neuron {

	/**
	 * @pre | prey != null
	 * @inspects | prey
	 */
	@Override
	public abstract int computeOutput(Prey prey);
}
