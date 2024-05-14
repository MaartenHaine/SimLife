package sim.neuralnet;

import sim.entities.Prey;
import util.Orientation;

/**
 * @immutable
 */
public class FreePassageSensorNeuron extends BinarySensorNeuron
{
	/**
	 * @invar | orientation != null
	 */
    private final Orientation orientation;

    /**
     * @throws IllegalArgumentException | orientation ==null
     * @post | getOrientation() == orientation
     */
    public FreePassageSensorNeuron(Orientation orientation)
    {
    	if (orientation == null) { throw new IllegalArgumentException(); }
        this.orientation = orientation;
    }

    @Override
    /**
     * LEGIT
     */
    public boolean detect(Prey prey)
    {
    	var world = prey.getWorld();
    	var detectionOrientation = prey.getOrientation().compose(this.orientation);
        var position = prey.getPosition();
        var targetPosition = position.move(detectionOrientation.toVector());

        return world.isFree(targetPosition);
    }
    
    /**
     * 
     * @post | result !=null
     */
    public Orientation getOrientation() {
    	return orientation;
    }
    
}
