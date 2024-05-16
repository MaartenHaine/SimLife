package sim.neuralnet;

import sim.entities.Prey;

/**
 * @immutable
 */
public abstract class SensorNeuron implements Neuron {

	/**
	 * @pre | prey != null
	 * @pre | prey.getWorld() != null
	 * @inspects | prey
	 * @inspects | prey.getWorld()
	 */
	@Override
	public abstract int computeOutput(Prey prey);
}
