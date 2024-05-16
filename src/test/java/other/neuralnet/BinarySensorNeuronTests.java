package other.neuralnet;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import sim.Chromosome;
import sim.Constants;
import sim.entities.World;
import sim.neuralnet.*;
import util.Orientation;
import util.Point;

class BinarySensorNeuronTests {

	@Test
	void testConstruct() {
		BinarySensorNeuron neuron = new HunterSensor();
		
		assertNotEquals(neuron, null);
	}

	@Test
	void testHunterSensor() {
		//generate a prey and a hunter next to eachother
		World world = new World(Constants.WORLD_SIZE, Constants.WORLD_SIZE);
		var shelter = world.createShelter(new Point(0, 0), Orientation.createRandom());
		var chromosome = Chromosome.createRandom();
		var prey = world.createPrey(shelter, chromosome, new Point(1, 1), Orientation.southEast());
		var prey_free = world.createPrey(shelter, chromosome, new Point(15, 15), Orientation.south());
		var hunter = world.createHunter(shelter, new Point(2, 2), Orientation.northWest());

		//create a hunter sensor
		BinarySensorNeuron neuron = new HunterSensor();
		assertTrue(neuron.detect(prey));

		assertFalse(neuron.detect(prey_free));
	}

	@Test
	void testShelterSensor() {
		//generate a prey with orientation towards shelter
		World world = new World(Constants.WORLD_SIZE, Constants.WORLD_SIZE);
		var shelter = world.createShelter(new Point(1, 1), Orientation.createRandom());
		var chromosome = Chromosome.createRandom();
		var prey1 = world.createPrey(shelter, chromosome, new Point(1, 0), Orientation.south());
		var prey2 = world.createPrey(shelter, chromosome, new Point(10, 10), Orientation.north());
		//create a shelter sensor
		BinarySensorNeuron neuron = new ShelterSensor();
		assertTrue(neuron.detect(prey1));
		assertFalse(neuron.detect(prey2));
	}

	@Test 
	void testFreePassageSensorNeuron() {
		//generate preys so that the location are free
		World world = new World(Constants.WORLD_SIZE, Constants.WORLD_SIZE);
		var shelter = world.createShelter(new Point(1, 2), Orientation.createRandom());
		var chromosome = Chromosome.createRandom();
		var prey = world.createPrey(shelter, chromosome, new Point(1, 0), Orientation.south());
		//create a free passage sensor
		BinarySensorNeuron neuron = new FreePassageSensorNeuron(Orientation.north());
		assertTrue(neuron.detect(prey));

		//now create a prey so that the location is not free
		var prey_block1 = world.createPrey(shelter, chromosome, new Point(1, 1), Orientation.north());
		assertFalse(neuron.detect(prey));
	}

	@Test
	void testComputeOutput() {
		World world = new World(Constants.WORLD_SIZE, Constants.WORLD_SIZE);
		var shelter = world.createShelter(new Point(0, 1), Orientation.createRandom());
		var chromosome = Chromosome.createRandom();
		var prey = world.createPrey(shelter, chromosome, new Point(1, 0), Orientation.south());
		var prey_free = world.createPrey(shelter, chromosome, new Point(15, 15), Orientation.south());

		//first check if the hunter sensor works
		var hunter = world.createHunter(shelter, new Point(1, 1), Orientation.createRandom());

		BinarySensorNeuron hunterSensor = new HunterSensor();
		assertEquals(hunterSensor.computeOutput(prey), 750);
		assertEquals(hunterSensor.computeOutput(prey_free), -750);

		//now check if the shelter sensor works
		BinarySensorNeuron shelterSensor = new ShelterSensor();
		assertEquals(shelterSensor.computeOutput(prey), -750);
		assertEquals(shelterSensor.computeOutput(prey_free), -750);

		//now check if the free passage sensor works
		BinarySensorNeuron freePassageSensor = new FreePassageSensorNeuron(Orientation.north());
		assertEquals(freePassageSensor.computeOutput(prey_free), 750);
		assertEquals(freePassageSensor.computeOutput(prey), -750);
	}

}
