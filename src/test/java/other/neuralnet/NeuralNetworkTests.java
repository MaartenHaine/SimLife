package other.neuralnet;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import sim.Chromosome;
import sim.neuralnet.*;

class NeuralNetworkTests {

	@Test
	public void testNeuralNetwork() {
		NeuralNetwork network = new NeuralNetwork();
		SensorNeuron[] inputNeurons = network.getInputNeurons();
		ActivationFunctionNeuron[] outputNeurons =network.getOutputNeurons();
		
		assertTrue(Arrays.stream(inputNeurons).allMatch(n->n!=null));
		assertTrue(Arrays.stream(outputNeurons).allMatch(n->n!=null));
		assertEquals(5, inputNeurons.length);
		assertEquals(2, outputNeurons.length);
	}

	@Test
	public void testGetInputLayerNeurons() {
        NeuralNetwork network = new NeuralNetwork();
        SensorNeuron[] inputNeurons = network.getInputNeurons();
        
        assertTrue(Arrays.stream(Arrays.copyOfRange(inputNeurons, 0, 3)).allMatch( n->n instanceof FreePassageSensorNeuron));
        assertTrue(inputNeurons[3] instanceof HunterSensor);
        assertTrue(inputNeurons[4] instanceof ShelterSensor);
        
        assertNotNull(inputNeurons);
        assertEquals(5, inputNeurons.length);
        
        assertTrue(Arrays.stream(inputNeurons).allMatch(n -> n != null));
    }

	@Test
    public void testGetMoveForwardNeuron() {
        NeuralNetwork network = new NeuralNetwork();
        ActivationFunctionNeuron moveForwardNeuron = network.getMoveForwardNeuron();
       
        assertTrue(moveForwardNeuron instanceof LinearFunctionNeuron);
        assertNotNull(moveForwardNeuron);
    }

	@Test
    public void testTurnNeuron() {
        NeuralNetwork network = new NeuralNetwork();
        ActivationFunctionNeuron turnNeutron = network.getTurnNeuron();
       
        assertTrue(turnNeutron instanceof LinearFunctionNeuron);
        assertNotNull(turnNeutron);
    }

	@Test
    public void testGetOutputNeurons() {
        NeuralNetwork network = new NeuralNetwork();
        ActivationFunctionNeuron[] outputNeurons = network.getOutputNeurons();
        
        assertNotNull(outputNeurons);
        assertEquals(2, outputNeurons.length);
        assertTrue(Arrays.stream(outputNeurons).allMatch(n -> n != null));
    }

	@Test
    public void testFromChromosome() {
		for (int i = 0; i < 100; i++) {
			Chromosome chromosome = Chromosome.createRandom();
	    	NeuralNetwork network = NeuralNetwork.fromChromosome(chromosome);
			assertNotNull(network);
		}
	    
	}
}
