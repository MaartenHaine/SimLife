package sim.neuralnet;

import java.util.ArrayList;

import sim.entities.Prey;
import util.Pair;

/**
 * @invar | getDependencies() != null
 * @invar | getDependencies().stream().allMatch(p -> p != null)
 * @invar | getDependencies().size() <= 7
 */

public abstract class ActivationFunctionNeuron implements Neuron
{

	/**
	 * @representationObject
	 * @representationObjects
	 * @invar | dependencies != null
	 * @invar | dependencies.stream().allMatch(p -> p != null)
	 * @invar | dependencies.size() >= 0
	 * Can be any >= 0 size
	 */
    private ArrayList<Pair<Neuron, Integer>> dependencies;

    private int bias;
    
    /**
     * The Neuron references are freely accessible by the client
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
     * @mutates | this
     * @inspects | deps
     * @pre | deps != null
     * @pre | deps.stream().allMatch(p -> p != null)
     * @pre | deps.size() <= 7
     * @post | getDependencies().size() == deps.size()
     * @post | equalDependencies(deps, getDependencies())
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
     * 
     * @post | getDependencies().size() == 0
     * @post | getBias() == 0
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
     * @post | implies(old(getDependencies().size()) == 7, equalDependencies(old(getDependencies()), getDependencies()) && !result)
     * @post | implies(old(getDependencies().size()) < 7, equalDependenciesWithAddedPair(old(getDependencies()), getDependencies(), dependency, weight) && result)
     */

    public boolean connect(Neuron dependency, int weight)
    {
    	if (dependencies.size() == 7)
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

    /**
     * @post | getBias() == bias
     */

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
