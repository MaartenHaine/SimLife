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
	 * @pre | prey.getWorld() !=null
	 * @inspects | prey
	 * @inspects | prey.getWorld()
	 * @post check if orientation of the pray is the same as the shortest one to the shelter 
	 * | prey.getOrientation().isEqual(prey.getPosition().vectorTo(prey.getShelter().getPosition()).toClosestOrientation()) 
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
