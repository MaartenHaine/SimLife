package sim.neuralnet;

import sim.entities.*;

public class HunterSensor extends BinarySensorNeuron {

	/**
	 * @pre | prey !=null
	 * @inspects | prey
	 * @post | result == prey.getWorld().hasHunterInCone(prey.getPosition(),prey.getOrientation())
	 */
	@Override
	public boolean detect(Prey prey) {
		return prey.getWorld().hasHunterInCone(prey.getPosition(), prey.getOrientation());
	}
}
