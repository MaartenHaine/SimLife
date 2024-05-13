package sim.entities;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import sim.Chromosome;
import util.Orientation;
import util.Point;

class EntityTest {

	
	private World world10;
	

	@BeforeEach
	void createWorld() {
		world10 = new World(10,10);
	}
	
	@Test
	void test() {
		Shelter s = world10.createShelter(new Point(1,1), Orientation.createRandom());
		Prey p = world10.createPrey(s,Chromosome.createRandom(),new Point(5,5), Orientation.createRandom());
		p.moveForward();
		world10.getPreys().get(0).getPosition();
		
		assertTrue(world10.getPreys().get(0).getPosition().equals(p.getPosition()));
		
		
	}

}
