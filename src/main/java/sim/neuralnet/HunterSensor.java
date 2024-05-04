package sim.neuralnet;

import sim.entities.Prey;

public class HunterSensor extends BinarySensorNeuron {

	/**
	 * A HunterSensor detects if there is a hunter somewhere ahead of the prey. 
	 * This logic is implemented in World.hasHunterInCone.
	 * 
	 * @pre | prey != null
	 * @post | result == prey.getWorld().hasHunterInCone(prey.getPosition(), prey.getOrientation())
	 */
	@Override
	public boolean detect(Prey prey) {

		return prey.getWorld().hasHunterInCone(prey.getPosition(), prey.getOrientation());
		
	}
}
