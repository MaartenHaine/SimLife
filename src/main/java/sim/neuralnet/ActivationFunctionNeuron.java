package sim.neuralnet;

import java.util.ArrayList;

import sim.entities.Prey;
import util.Pair;


public abstract class ActivationFunctionNeuron implements Neuron
{

	/**
	 * Can be any >= 0 size
	 */
    private ArrayList<Pair<Neuron, Integer>> dependencies;

    private int bias;
    
    /**
     * The Neuron references are freely accessible by the client
     */
    public ArrayList<Pair<Neuron, Integer>> getDependencies() {
    	return null;
    }
    

    public void setDependencies(ArrayList<Pair<Neuron, Integer>> deps) {

    }

    /**
     * Initializes with getBias = 0 and getDependencies is empty
     */
    public ActivationFunctionNeuron()
    {
        this.dependencies = new ArrayList<>();
        bias = 0;
    }


    public boolean connect(Neuron dependency, int weight)
    {
        return true;
    }

    public void setBias(int bias)
    {
        this.bias = bias;
    }
    
    public int getBias() {
    	return bias;
    }

    @Override
    /**
     * LEGIT
     */
    public int computeOutput(Prey prey)
    {
        var total = this.bias;

        for ( var pair : this.dependencies )
        {
            var dependency = pair.getFirst();
            var weight = pair.getSecond();

            total += dependency.computeOutput(prey) * weight / 1000;
        }

        return applyActivationFunction(total);
    }

    public abstract int applyActivationFunction(int input);
}
