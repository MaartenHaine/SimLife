package sim.neuralnet;

import sim.entities.Prey;
import util.Logic;

/**
 * @immutable
 * 
 */
public abstract class BinarySensorNeuron extends SensorNeuron
{
    @Override
    /**
     * @pre | prey != null
     * 
     * @inspects | prey
     * @post | Logic.implies(detect(prey) == true, result==750) 
     * @post | Logic.implies(detect(prey) == false,result== -750)
     */
    public int computeOutput(Prey prey)
    {
        return this.detect(prey) ? 750 : (-750);
    }

    public abstract boolean detect(Prey prey);
}
