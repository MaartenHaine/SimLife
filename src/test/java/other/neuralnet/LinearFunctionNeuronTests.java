package other.neuralnet;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import sim.neuralnet.LinearFunctionNeuron;

class LinearFunctionNeuronTests {
	 @Test
	    void testApplyActivationFunction() {
	        LinearFunctionNeuron neuron = new LinearFunctionNeuron();

	        // Test for input less than -1000
	        assertEquals(-1000, neuron.applyActivationFunction(-1500));

	        // Test for input greater than 1000
	        assertEquals(1000, neuron.applyActivationFunction(1500));

	        // Test for input within the range [-1000, 1000]
	        assertEquals(-500, neuron.applyActivationFunction(-500));
	        assertEquals(500, neuron.applyActivationFunction(500));
	        assertEquals(0, neuron.applyActivationFunction(0));
	    }
}
