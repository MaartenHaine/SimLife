package sim.neuralnet;

import java.util.Arrays;

import sim.Chromosome;
import util.Orientation;

/**
 * Neuron references are freely accesible by the client
 */
public class NeuralNetwork
{

	/**
	 * @representationObject
	 * @invar | inputLayerNeurons != null
	 * @invar | Arrays.stream(inputLayerNeurons).allMatch(n -> n != null) 
	 */

    private final SensorNeuron[] inputLayerNeurons;

    /**
     * @invar | moveForwardNeuron != null
     * @invar | moveForwardNeuron.getDependencies().stream().allMatch(dep -> Arrays.stream(inputLayerNeurons).anyMatch(sensor -> dep.getFirst() == sensor))
     */
    private final ActivationFunctionNeuron moveForwardNeuron;
    	
    /**
     * @invar | turnNeuron != null
     * @invar
     *   | turnNeuron.getDependencies().stream().allMatch(dep -> Arrays.stream(inputLayerNeurons).anyMatch(sensor -> dep.getFirst() == sensor))
     */
	private final ActivationFunctionNeuron turnNeuron;

	/**
	 * Neural network with
	 * - 5 input neurons (3 FreePassageSensorNeuron with respectively N, NW, NE as parameters;
	 *   1 HunterSenor neuron; 1 ShelterSensor)
	 * - 2 output neurons (2 LinearFunctionNeuron)
	 */
    public NeuralNetwork()
    {
    	inputLayerNeurons = new SensorNeuron[] {
    			new FreePassageSensorNeuron(Orientation.north()),
        		new FreePassageSensorNeuron(Orientation.northWest()),
        		new FreePassageSensorNeuron(Orientation.northEast()),
        		new HunterSensor(),
        		new ShelterSensor()
    	};

    	moveForwardNeuron = new LinearFunctionNeuron();
    	turnNeuron = new LinearFunctionNeuron();
    }

    /**
     * @creates | result
     * @post | result != null
     * @post | Arrays.stream(result).allMatch(n -> n != null)
     * @post | result.length == 5
     */
	public SensorNeuron[] getInputNeurons()
    {
		return Arrays.copyOf(this.inputLayerNeurons, this.inputLayerNeurons.length);
    }

    /**
     * @creates | result
     * @post | result != null
     * @post | Arrays.stream(result).allMatch(n -> n != null)
     * @post | result.length == 2
     * @post | result[0] == getMoveForwardNeuron()
     * @post | result[1] == getTurnNeuron()
     */
    public ActivationFunctionNeuron[] getOutputNeurons()
    {
        return new ActivationFunctionNeuron[] { this.moveForwardNeuron, this.turnNeuron };
    }

    /**
     * @post | result != null
     */

    public ActivationFunctionNeuron getMoveForwardNeuron() { return this.moveForwardNeuron; }

    /**
     * @post | result != null
     */

    public ActivationFunctionNeuron getTurnNeuron() { return this.turnNeuron; }

    /**
     * LEGIT
     * @pre | chromosome != null
     * 
     */
    public static NeuralNetwork fromChromosome(Chromosome chromosome)
    {
        var neuralNetwork = new NeuralNetwork();
        var geneIndex = 0;
        
        for ( var outputNeuron : neuralNetwork.getOutputNeurons() )
        {
            for ( var inputNeuron : neuralNetwork.getInputNeurons() )
            {
                var weight = chromosome.getGene(geneIndex++);

                outputNeuron.connect(inputNeuron, weight);
            }
        }
        
        for (var outputNeuron : neuralNetwork.getOutputNeurons()) {
            var bias = chromosome.getGene(geneIndex++);
            outputNeuron.setBias(bias);        	
        }

        return neuralNetwork;
    }
}
