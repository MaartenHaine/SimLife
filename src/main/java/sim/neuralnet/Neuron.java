package sim.neuralnet;

import sim.entities.Prey;

public interface Neuron
{
	/**
	 * @pre | creature !=null
	 * @inspects | creature
	 */
    int computeOutput(Prey creature);
}
