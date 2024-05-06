package sim.neuralnet;

import sim.entities.Prey;

/**
 * @immutable
 * 
 * @invar | prey != null
 * @post check if orientation of the pray is the same as the shortest one to the shelter 
 * | prey.getOrientation().isEqual(prey.getPosition().vectorTo(prey.getShelter().getPosition()).toClosestOrientation())
 */
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
