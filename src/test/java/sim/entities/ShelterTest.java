package sim.entities;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import sim.Chromosome;
import sim.Constants;
import util.Color;
import util.Orientation;
import util.Point;

class ShelterTest {

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
    
        assertTrue( s.getInhabitants().equals(new ArrayList<>()));
	
	}
	
	@Test
	void getInhabitantsTest() {
		//FLAW REPRESENTATION EXPOSURE FOR getInHabitants
		Shelter s = world10.createShelter(new Point(1,1), Orientation.createRandom());

		Prey p1 = world10.createPrey(s,Chromosome.createRandom(),new Point(5,5), Orientation.createRandom());
		
		assertTrue(s.getInhabitants().size()==1);
		s.getInhabitants().add(p1);
		assertTrue(s.getInhabitants().size()==1);
		
		Prey p2 = world10.createPrey(s,Chromosome.createRandom(),new Point(6,6), Orientation.createRandom());
		ArrayList<Prey> AL = new ArrayList<Prey>();
		AL.add(p1);
		AL.add(p2);
		assertTrue(s.getInhabitants().equals(AL));
		
		p1.die();
		AL.remove(0);
		assertTrue(s.getInhabitants().equals(AL));
		
	
	}
	
	@Test
	void performActionIfAliveTest() {
		Point shelterPosition = new Point(5, 5);

        Shelter s = world10.createShelter(shelterPosition, Orientation.north());
        

        s.performActionIfAlive();
        // NO ASSERT BECAUSE RANDOMNESS
      
	}
	
	@Test
	void getColorTest() {
		Point shelterPosition = new Point(5, 5);

        Shelter s = world10.createShelter(shelterPosition, Orientation.north());
        
        assertEquals(s.getColor(),Color.BLACK);
	}
	
	@Test
	void toStringTest() {
		Point shelterPosition = new Point(5, 5);

        Shelter s = world10.createShelter(shelterPosition, Orientation.north());
        
        assertEquals(s.toString(),String.format("Shelter(position=%s)", s.getPosition()));
	}
}
