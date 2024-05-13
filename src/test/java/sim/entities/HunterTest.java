package sim.entities;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import sim.Chromosome;
import sim.Constants;
import util.Color;
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
	
		@Test
		void getColorTest() {
			Point shelterPosition = new Point(5, 5);
	        Point hunterPosition = new Point(1, 1);

	        Shelter s = world10.createShelter(shelterPosition, Orientation.north());
	        Hunter h = world10.createHunter(s,hunterPosition, Orientation.north());
		      
	        assertEquals(h.getColor(),Color.RED);
		}
		
		@Test
		void toStringTest() {
			Point shelterPosition = new Point(5, 5);
	        Point hunterPosition = new Point(1, 1);

	        Shelter s = world10.createShelter(shelterPosition, Orientation.north());
	        Hunter h = world10.createHunter(s,hunterPosition, Orientation.north());
		      
	        assertEquals(h.toString(), String.format("Hunter(position=%s)", h.getPosition()));
		}
		
		@Test
		void performActionAppetiteTest()
		{
			
			Point shelterPosition = new Point(5, 5);
	        Point hunterPosition = new Point(1, 1);
	        Point preyPosition = new Point(2, 2);
	        Point safePosition = new Point(9, 9);
	        
	        Shelter s = world10.createShelter(shelterPosition, Orientation.north());
	        Hunter h = world10.createHunter(s,hunterPosition, Orientation.north());
	        
	        // TEST THAT HUNTER CAN MAXIMALLY EAT APPETITE AMOUNT OF PREY
			
	        Prey p_that_has_to_stay_alive_to_keep_the_shelter_alive = world10.createPrey(s,Chromosome.createRandom(),safePosition,Orientation.north());
			
			
			for(int i=0; i<Constants.HUNTER_INITIAL_APPETITE;i++) {
				Prey p = world10.createPrey(s,Chromosome.createRandom(),preyPosition,Orientation.north());
				
				// ELIMINATING CHANCE THAT HUNTER DIDNT MOVE, 
				// MIND: WHEN MOVE PROB IS ZERO THIS BECOMES INFINITE LOOP
				while( !p.isDead()) {
					h.performAction();
				}
				
				h.moveForward(); // SO PREY CAN SPAWN AGAIN NEXT TO HUNTER
				
			}
			Point pos = h.getPosition();
			// p_that_has_to_stay_alive_to_keep_the_shelter_alive
			// wont be eaten because appetite is zero
		
			// ELIMNATING CHANCE
			for(int j=0; j<100;j++) {
				h.performAction();
			}
			// Didnt move
			assertEquals(pos,h.getPosition());
			
			
			
		}		
		@Test
		void performActionClosestPreyTest()
		{
			
			Point shelterPosition = new Point(5, 5);
	        Point hunterPosition = new Point(0, 0);
	        Point preyPosition = new Point(2, 2);
	        Point safePosition = new Point(9, 9);
	        
	        Shelter s = world10.createShelter(shelterPosition, Orientation.north());
	        Hunter h = world10.createHunter(s,hunterPosition, Orientation.north());
	        
	        // TEST THAT HUNTER EATS CLOSEST PREY
			
	        Prey p_that_stays_alive = world10.createPrey(s,Chromosome.createRandom(),safePosition,Orientation.north());
	        Prey p = world10.createPrey(s,Chromosome.createRandom(),preyPosition,Orientation.north());
			
			// ELIMNATING CHANCE
			while(!p.isDead() && !p_that_stays_alive.isDead()) {
				h.performAction();
			}
			
			assertFalse(p_that_stays_alive.isDead());
			assertTrue(p.isDead());
			
			
			
		}	
		
}
