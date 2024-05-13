package sim.entities;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import sim.Constants;
import util.Orientation;
import util.Point;

class MortalEntityTest {

	
	private World world10;
	

	@BeforeEach
	void createWorld() {
		world10 = new World(10,10);
	}
	@Test
    void constructor_DefensiveProgramming() {
       
        Point validPosition = new Point(1, 1);
        Point invalidPosition = new Point(-1, -1);


        //USING CREATION OF SHELTER AS MORTAL ENTITY DUMMY
        // Test null arguments
        assertThrows(IllegalArgumentException.class, () -> world10.createShelter(null, Orientation.createRandom()));
        assertThrows(IllegalArgumentException.class, () -> world10.createShelter(validPosition, null));
        // ! UNTESTABLE THAT WORLD IS NULL OR MOVEPROBABILTY WRONG VALUE
        
        
        // Test invalid position
        assertThrows(IllegalArgumentException.class, () -> world10.createShelter(invalidPosition, Orientation.createRandom()));
        
        // Test position already occupied
        Shelter s = world10.createShelter(validPosition, Orientation.north());
		assertThrows(IllegalArgumentException.class, () -> world10.createShelter(validPosition, Orientation.createRandom()));

        // Test validity constructor 
        assertEquals(world10, s.getWorld());
        assertEquals(false, s.isDead());
        assertEquals(validPosition, s.getPosition());
        assertEquals(Orientation.north(), s.getOrientation());
        assertEquals(Constants.SHELTER_MOVE_PROBABILITY, s.getMoveProbability());
        assertEquals(s, world10.getEntityAt(validPosition));
        assertTrue(Point.isWithin(s.getPosition(), world10.getWidth(), world10.getHeight()));
    }
	
	@Test
	void performActionTest() {
		Point shelterPosition = new Point(5, 5);

        Shelter s = world10.createShelter(shelterPosition, Orientation.north());
        
        
        // HARD TO TEST BC RANDOMNESS
        //assertEquals(s.getPosition(),new Point(5, 4));
        
        s.die();
        // no change
        s.performAction();
        assertEquals(s.getPosition(),shelterPosition);
       
	}
	
	@Test
	void moveForwardTest() {
		Point shelterPosition = new Point(5, 5);

        Shelter s = world10.createShelter(shelterPosition, Orientation.north());
        
        s.moveForward();
        assertEquals(s.getPosition(),new Point(5, 4));
        
        s.die();
        // no change
        s.moveForward();
        assertEquals(s.getPosition(),new Point(5, 4));
       
	}
	
	@Test
	void isDeadTest() {
		Point shelterPosition = new Point(5, 5);

        Shelter s = world10.createShelter(shelterPosition, Orientation.north());
        assertEquals(false, s.isDead());
        
        s.die();
        assertEquals(true, s.isDead());
        
	
	}
	@Test
	void dieTest() {
		Point shelterPosition = new Point(5, 5);

        Shelter s = world10.createShelter(shelterPosition, Orientation.north());

        
        s.die();
        assertEquals(true, s.isDead());
        assertTrue(null==world10.getEntityAt(shelterPosition));
        assertTrue(null==s.getWorld());
        
        
	
	}
	
}
