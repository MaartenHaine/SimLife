package sim.neuralnet;

import sim.entities.Prey;

public class ShelterSensor extends BinarySensorNeuron
{
	@Override
	public boolean detect(Prey prey)
	{
		var shelterPosition = prey.getShelter().getPosition();
		var preyPosition = prey.getPosition();
		var orientation = preyPosition.vectorTo(shelterPosition).toClosestOrientation();
		
		return orientation.isEqual( prey.getOrientation());
	}
}
