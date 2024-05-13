package sim.entities;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import sim.Constants;
import util.Orientation;
import util.Point;

class HunterTest {

	private World world10;
	

	@BeforeEach
	void createWorld() {
		world10 = new World(10,10);
	}
	
	@Test
    void constructor_DefensiveProgramming() {
     
			Point shelterPosition = new Point(5, 5);
	        Point validPosition = new Point(1, 1);
	        Point invalidPosition = new Point(-1, -1);
	        Shelter s = world10.createShelter(shelterPosition, Orientation.north());
			

	        //USING CREATION OF SHELTER AS ENTITY DUMMY
	        // Test null arguments
	        assertThrows(IllegalArgumentException.class, () -> world10.createHunter(null,validPosition, Orientation.createRandom()));
	        assertThrows(IllegalArgumentException.class, () -> world10.createHunter(s,null, Orientation.createRandom()));
	        assertThrows(IllegalArgumentException.class, () -> world10.createHunter(s,validPosition, null));
	        // ! UNTESTABLE THAT WORLD IS NULL OR MOVEPROBABILTY WRONG VALUE
	        
	        
	        // Test invalid position
	        assertThrows(IllegalArgumentException.class, () ->world10.createHunter(s,invalidPosition, Orientation.createRandom()));
	        
	        // Test position already occupied
	      	assertThrows(IllegalArgumentException.class, () -> world10.createHunter(s,shelterPosition, Orientation.createRandom()));

	      	
	        Hunter h = world10.createHunter(s,validPosition, Orientation.north());
	        // Test validity constructor 
	        assertEquals(world10, h.getWorld());
	        assertEquals(validPosition, h.getPosition());
	        assertEquals(Orientation.north(), h.getOrientation());
	        assertEquals(Constants.HUNTER_MOVE_PROBABILITY, h.getMoveProbability());
	        assertEquals(h, world10.getEntityAt(validPosition));
	        assertTrue(Point.isWithin(h.getPosition(), world10.getWidth(), world10.getHeight()));
	    }
		
}
