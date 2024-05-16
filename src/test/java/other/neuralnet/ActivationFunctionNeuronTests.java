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
import util.Orientation;
import util.Pair;
import util.Point;
import sim.Constants;

class ActivationFunctionNeuronTests {
	
	@Test
	void testConstruct() {
		ActivationFunctionNeuron neuron = new LinearFunctionNeuron();

		assertEquals(neuron.getBias(), 0);
		assertEquals(neuron.getDependencies().size(), 0);
	}

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

	@Test
	void testSetDependencies() {
		ActivationFunctionNeuron neuron = new LinearFunctionNeuron();
		ArrayList<Pair<Neuron, Integer>> deps = new ArrayList<Pair<Neuron, Integer>>();
		deps.add(new Pair<Neuron, Integer>(new FreePassageSensorNeuron(Orientation.north()), 1));
		deps.add(new Pair<Neuron, Integer>(new FreePassageSensorNeuron(Orientation.northWest()), 1));
		deps.add(new Pair<Neuron, Integer>(new FreePassageSensorNeuron(Orientation.northEast()), 1));
		deps.add(new Pair<Neuron, Integer>(new FreePassageSensorNeuron(Orientation.south()), 1));
		deps.add(new Pair<Neuron, Integer>(new FreePassageSensorNeuron(Orientation.southWest()), 1));
		neuron.setDependencies(deps);

		assertEquals(neuron.getDependencies().size(), 5);
	}

	@Test
	void testGetDependencies() {
		ActivationFunctionNeuron neuron = new LinearFunctionNeuron();
		ArrayList<Pair<Neuron, Integer>> deps = new ArrayList<Pair<Neuron, Integer>>();
		deps.add(new Pair<Neuron, Integer>(new FreePassageSensorNeuron(Orientation.north()), 1));
		deps.add(new Pair<Neuron, Integer>(new FreePassageSensorNeuron(Orientation.northWest()), 1));
		deps.add(new Pair<Neuron, Integer>(new FreePassageSensorNeuron(Orientation.northEast()), 1));
		deps.add(new Pair<Neuron, Integer>(new FreePassageSensorNeuron(Orientation.south()), 1));
		deps.add(new Pair<Neuron, Integer>(new FreePassageSensorNeuron(Orientation.southWest()), 1));
		neuron.setDependencies(deps);

		assertEquals(neuron.getDependencies().get(0).getFirst().getClass(), FreePassageSensorNeuron.class);
		assertEquals(neuron.getDependencies().get(0).getSecond(), 1);
	}

	@Test
	void testGetBias() {
		ActivationFunctionNeuron neuron = new LinearFunctionNeuron();
		assertEquals(neuron.getBias(), 0);
	}

	@Test
	void testSetBias() {
		ActivationFunctionNeuron neuron = new LinearFunctionNeuron();
		neuron.setBias(1);
		assertEquals(neuron.getBias(), 1);
	}

	@Test
	void testConnect() {
		ActivationFunctionNeuron neuron = new LinearFunctionNeuron();
		Neuron dep = new FreePassageSensorNeuron(Orientation.north());
		assertTrue(neuron.connect(dep, 1));
		assertEquals(neuron.getDependencies().size(), 1);

		//geneerate more dependencies so that the limit is reached
		dep = new FreePassageSensorNeuron(Orientation.northWest());
		neuron.connect(dep, 1);
		dep = new FreePassageSensorNeuron(Orientation.northEast());
		neuron.connect(dep, 1);
		dep = new FreePassageSensorNeuron(Orientation.south());
		neuron.connect(dep, 1);
		dep = new FreePassageSensorNeuron(Orientation.southWest());
		neuron.connect(dep, 1);
		dep = new FreePassageSensorNeuron(Orientation.southEast());
		assertFalse(neuron.connect(dep, 1));

	}

	@Test
	void testComputeOutput() {
		World world = new World(Constants.WORLD_SIZE, Constants.WORLD_SIZE);
		var shelter = world.createShelter(new Point(0, 0), Orientation.createRandom());
		var chromosome = Chromosome.createRandom();
		var prey = world.createPrey(shelter, chromosome, new Point(1, 0), Orientation.createRandom());
		
		ActivationFunctionNeuron neuron = new LinearFunctionNeuron();
		Neuron dep = new FreePassageSensorNeuron(Orientation.north());
		neuron.connect(dep, 1);
		dep = new FreePassageSensorNeuron(Orientation.northWest());
		neuron.connect(dep, 1);

		neuron.setBias(100000);
		//apply activation and check if change
		neuron.computeOutput(prey);

		//because we set bias high we will always return 1000
		assertEquals(neuron.computeOutput(prey), 1000);

		//if we set bias back to 1 we will return a random integer depending on the randomness
		neuron.setBias(1);
		assertTrue(neuron.computeOutput(prey) >= -1000 && neuron.computeOutput(prey) <= 1000);
	}

	
}

