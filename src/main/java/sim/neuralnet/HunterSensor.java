package sim.neuralnet;

import sim.entities.*;

public class HunterSensor extends BinarySensorNeuron {

	@Override
	public boolean detect(Prey prey) {
		return prey.getWorld().hasHunterInCone(prey.getPosition(), prey.getOrientation());
	}
}
