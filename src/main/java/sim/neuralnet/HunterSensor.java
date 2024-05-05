package sim.neuralnet;

import sim.entities.Prey;

public class HunterSensor extends BinarySensorNeuron {

	@Override
	public boolean detect(Prey prey) {
		return false;
	}
}
