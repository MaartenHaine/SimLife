package sim.neuralnet;

import sim.entities.Prey;

public interface Neuron
{
	/**
	 * @pre | creature !=null
	 * @pre | creature.getWorld() != null
	 * 
	 * @inspects | creature
	 * @inspects | creature.getWorld()
	 */
    int computeOutput(Prey creature);
}
