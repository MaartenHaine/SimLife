package sim.neuralnet;

import sim.entities.Prey;

/**
 * @immutable
 * 
*/
public class ShelterSensor extends BinarySensorNeuron
{
	/**
	 * @pre | prey != null
	 * @pre | prey.getShelter() !=null
	 * @pre zie super| prey.getWorld() !=null
	 * @inspects | prey
	 * @inspects | prey.getShelter()
	 * @post check if orientation of the pray is the same as the shortest one to the shelter 
	 * | result == prey.getPosition().vectorTo(prey.getShelter().getPosition()).toClosestOrientation().isEqual(prey.getOrientation())
	 */
	@Override
	public boolean detect(Prey prey)
	{
		var shelterPosition = prey.getShelter().getPosition();
		var preyPosition = prey.getPosition();
		var orientation = preyPosition.vectorTo(shelterPosition).toClosestOrientation();
		
		return orientation.isEqual( prey.getOrientation());
	}
}
