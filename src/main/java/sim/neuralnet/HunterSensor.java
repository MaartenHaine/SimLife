package sim.neuralnet;

import sim.entities.*;

/**
 * @immutable
 */
public class HunterSensor extends BinarySensorNeuron {

	/**
	 * @pre | prey !=null
	 * @pre | prey.getWorld()!= null
	 * @inspects | prey
	 * @inspects | prey.getWorld()
	 * @post | result == prey.getWorld().hasHunterInCone(prey.getPosition(),prey.getOrientation())
	 */
	@Override
	public boolean detect(Prey prey) {
		return prey.getWorld().hasHunterInCone(prey.getPosition(), prey.getOrientation());
	}
}
