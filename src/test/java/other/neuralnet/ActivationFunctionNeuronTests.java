package other.neuralnet;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import sim.Chromosome;
import sim.entities.World;
import sim.neuralnet.ActivationFunctionNeuron;
import sim.neuralnet.FreePassageSensorNeuron;
import sim.neuralnet.LinearFunctionNeuron;
import sim.neuralnet.Neuron;
import util.Pair;
import util.Point;
import sim.Constants;

class ActivationFunctionNeuronTests {

	@Test
    void testConstructor() {
        ActivationFunctionNeuron neuron = new ActivationFunctionNeuron() {
        	public int applyActivationFunction(int input) {return 0;}
        };

        assertEquals(0, neuron.getBias());
        assertTrue(neuron.getDependencies().isEmpty());
    }

	@Test
	void testBugsActivationFunctionNeuron() {

		
		ActivationFunctionNeuron neuron = new ActivationFunctionNeuron() {
			
        	public int applyActivationFunction(int input) {return input;}
        	//return input for testing purposes, in this test output will be between -1000 and 1000 so no invars/post/pre will be triggerd
        };

	 @Test
    void testSetAndGetDependencies() {
        ActivationFunctionNeuron neuron = new ActivationFunctionNeuron() {
        	public int applyActivationFunction(int input) {return 0;}
        };

    	Neuron dummy = new Neuron(){ 
        	@Override
        	public int computeOutput(World world, Creature creature){return 0;}
        	};
        	
        ArrayList<Pair<Neuron, Integer>> testDependencies = new ArrayList<>();
        testDependencies.add(new Pair<>(dummy, 2));
        testDependencies.add(new Pair<>(dummy, 3));

        neuron.setDependencies(testDependencies);

        ArrayList<Pair<Neuron, Integer>> retrievedDependencies = neuron.getDependencies();

        assertEquals(testDependencies.size(), retrievedDependencies.size());

        for (int i = 0; i < testDependencies.size(); i++) {
            Pair<Neuron, Integer> expectedPair = testDependencies.get(i);
            Pair<Neuron, Integer> retrievedPair = retrievedDependencies.get(i);

            assertEquals(expectedPair.getFirst(), retrievedPair.getFirst());
            assertEquals(expectedPair.getSecond(), retrievedPair.getSecond());
        }
    }

	@Test
    void testConnect() {
    	ActivationFunctionNeuron neuron = new ActivationFunctionNeuron() {
           	public int applyActivationFunction(int input) {return 0;}
           };
        Neuron dummy = new Neuron(){ 
           @Override
           public int computeOutput(World world, Creature creature){return 0;}
           };
        assertTrue(neuron.connect(dummy, 2));
        assertTrue(neuron.connect(dummy, 3));
        assertTrue(neuron.connect(dummy, 4));
        assertTrue(neuron.connect(dummy, 5));
        assertTrue(neuron.connect(dummy, 6));
    	
        assertTrue(neuron.getDependencies().get(neuron.getDependencies().size() - 1).equals(new Pair<>(dummy, 6)));

        
        assertTrue(neuron.connect(dummy, 7));
        assertTrue(neuron.connect(dummy, 8));
        
        // check for silent fail
        assertFalse(neuron.connect(dummy, 9));
      
        assertEquals(7, neuron.getDependencies().size());
        assertTrue(neuron.getDependencies().get(6).equals(new Pair<>(dummy, 8)));
    }

	 @Test
    void testComputeOutput() {
    	//Make Neuron with connections
		ActivationFunctionNeuron testedNeuron = new LinearFunctionNeuron();

		HorizontalOrientationSensorNeuron n1 = new HorizontalOrientationSensorNeuron();
		VerticalOrientationSensorNeuron n2 = new VerticalOrientationSensorNeuron();
		HorizontalPositionSensorNeuron n3 = new HorizontalPositionSensorNeuron();
		VerticalPositionSensorNeuron n4 = new VerticalPositionSensorNeuron();
		FreePassageSensorNeuron n5 = new FreePassageSensorNeuron(Orientation.north());
		FreePassageSensorNeuron n6 = new FreePassageSensorNeuron(Orientation.northWest());
		FreePassageSensorNeuron n7 = new FreePassageSensorNeuron(Orientation.northEast());
		
		testedNeuron.setBias(1);
		testedNeuron.connect(n4, 100);
		testedNeuron.connect(n3, 100);
		testedNeuron.connect(n2, 100);
		testedNeuron.connect(n1, 100);
		testedNeuron.connect(n7, 100);
		testedNeuron.connect(n6, 100);
		testedNeuron.connect(n5, 100);
	
		ImmobileBehavior behavior = new ImmobileBehavior(Chromosome.createRandom());
		Point position1 = new Point(0, 0);
		Orientation orientation = Orientation.southEast();
		Creature creature1 = new Creature(behavior, position1, orientation);

		World w = new World(Constants.WSIZE,Constants.WSIZE,new Creature[] {creature1});
		
		assertEquals(126,testedNeuron.computeOutput(w, creature1));
    }


}

