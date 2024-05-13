package sim.neuralnet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.IntStream;
import util.Pair;
import static util.Logic.*;

import sim.entities.Prey;
import util.Pair;


public abstract class ActivationFunctionNeuron implements Neuron
{

	/**
	 * @representationObject
	 * @representationObjects
	 * @invar | dependencies != null
	 * @invar | dependencies.stream().allMatch(p -> p != null)
	 * @invar | dependencies.size() <= 5
	 */

    private ArrayList<Pair<Neuron, Integer>> dependencies;

    private int bias;
    
    /**
     * The Neuron references are freely accessible by the client
     *
     * @creates | result
     * @post | result != null
     * @post | result.stream().allMatch(p -> p != null)
     */

    public ArrayList<Pair<Neuron, Integer>> getDependencies() {
    	ArrayList<Pair<Neuron, Integer>> res = new ArrayList<Pair<Neuron, Integer>>();
    	for (Pair<Neuron, Integer> p : dependencies) {
    		Pair<Neuron, Integer> aux = new Pair<Neuron, Integer>(p.getFirst(), p .getSecond());
    		res.add(aux);
    	}
    	return res;
    }
    
    /**
     * pre: deps has correct length
     * The Neuron references are stored as is.
     *
     * @mutates | this
     * @inspects | deps
     * @pre | deps != null
     * @pre | deps.stream().allMatch(p -> p != null)
     * @pre | deps.size() <= 5
     * @post | getDependencies().size() == deps.size()
     * @mutates_properties | getDependencies()
     */

    public void setDependencies(ArrayList<Pair<Neuron, Integer>> deps) {
    	ArrayList<Pair<Neuron, Integer>> res = new ArrayList<Pair<Neuron, Integer>>();
    	for (Pair<Neuron, Integer> p : deps) {
    		res.add(new Pair<Neuron, Integer>(p.getFirst() , p.getSecond()));
    	}
    	dependencies = res;
    }

    /**
     * Initializes with getBias = 0 and getDependencies is empty
     */
    public ActivationFunctionNeuron()
    {
        this.dependencies = new ArrayList<>();
        bias = 0;
    }

    /**
     * If the connection fails, do nothing and return false
     *
     * @mutates | this
     * @pre | dependency != null
     * @post | implies(old(getDependencies().size()) == 5, result == false)
     * @post | implies(old(getDependencies().size()) < 5, ((result == true)&&(getDependencies().stream().anyMatch(e->e.getFirst().equals(dependency) && e.getSecond()==weight))))
     * @post | getDependencies().size() == old(getDependencies().size()) + (result ? 1 : 0)
     * */
    public boolean connect(Neuron dependency, int weight)
    {
    	if (dependencies.size() == 5)
    	{
    		return false;
		}
    	else
    	{
            var pair = new Pair<Neuron, Integer>(dependency, weight);
            dependencies.add(pair);
            return true;
    	}
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
