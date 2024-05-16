package other.sim;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import sim.Simulation;
import sim.Constants;
import sim.entities.World;

class SimulationTests {

	@Test
	void testConstruct() {
		Simulation sim = new Simulation(Constants.WORLD_SIZE, Constants.SHELTER_COUNT, Constants.INHABITANTS_PER_SHELTER, Constants.HUNTERS_PER_SHELTER);
		
		assertNotNull(sim.getWorld());
		
		assertThrows(IllegalArgumentException.class, ()->new Simulation(10, 10,10, 10));
	}

	@Test
	void testGetWorld() {
		Simulation sim = new Simulation(Constants.WORLD_SIZE, Constants.SHELTER_COUNT, Constants.INHABITANTS_PER_SHELTER, Constants.HUNTERS_PER_SHELTER);
		
		assertNotNull(sim.getWorld());
		assertEquals(sim.getWorld().getPreys().size(), Constants.SHELTER_COUNT*Constants.INHABITANTS_PER_SHELTER);
		assertEquals(sim.getWorld().getHunters().size(), Constants.SHELTER_COUNT*Constants.HUNTERS_PER_SHELTER);
		assertEquals(sim.getWorld().getHeight(), Constants.WORLD_SIZE);
	}

	@Test
	void testNextGeneration() {
		Simulation sim = new Simulation(Constants.WORLD_SIZE, Constants.SHELTER_COUNT, Constants.INHABITANTS_PER_SHELTER, Constants.HUNTERS_PER_SHELTER);
		for(int i = 0; i<10; i++) {
			sim.getWorld().step();
		}
		World old_world = sim.getWorld();
		assertEquals(old_world, sim.getWorld());
		
		sim.nextGeneration();
		assertEquals(sim.getWorld().getPreys().size(), Constants.SHELTER_COUNT*Constants.INHABITANTS_PER_SHELTER);
		assertEquals(sim.getWorld().getHunters().size(), Constants.SHELTER_COUNT*Constants.HUNTERS_PER_SHELTER);
		assertEquals(sim.getWorld().getHeight(), Constants.WORLD_SIZE);
		
		//check if the world has changed
		assertNotEquals(old_world, sim.getWorld());
	}


}
