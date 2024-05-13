package sim.entities;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import sim.Chromosome;
import sim.Constants;
import util.Color;
import util.Orientation;
import util.Point;

class EntityTest {

	
	private World world10;
	

	@BeforeEach
	void createWorld() {
		world10 = new World(10,10);
	}
	
	@Test
    void constructor_DefensiveProgramming() {
       
        Point validPosition = new Point(1, 1);
        Point invalidPosition = new Point(-1, -1);


        //USING CREATION OF SHELTER AS ENTITY DUMMY
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
        assertEquals(validPosition, s.getPosition());
        assertEquals(Orientation.north(), s.getOrientation());
        assertEquals(Constants.SHELTER_MOVE_PROBABILITY, s.getMoveProbability());
        assertEquals(s, world10.getEntityAt(validPosition));
        assertTrue(Point.isWithin(s.getPosition(), world10.getWidth(), world10.getHeight()));
    }
	
	@Test
	void getPositionTest() {
		 Point topLeftPos = new Point(0, 0);
		 Point bottomRightPos= new Point(9, 9);
		 Point centerPos = new Point(5,5);
		 
		 Shelter sTL = world10.createShelter(topLeftPos, Orientation.north());
		 assertEquals(topLeftPos, sTL.getPosition());
		 
		 Shelter sBR = world10.createShelter(bottomRightPos, Orientation.north());
		 assertEquals(bottomRightPos, sBR.getPosition());
		 
		 Shelter sC = world10.createShelter(centerPos, Orientation.north());
		 assertEquals(centerPos, sC.getPosition());

	}
	
	@Test
	void getOrientationTest() {

		 Orientation or = Orientation.north();
		 
		 Shelter s= world10.createShelter(new Point(5,5), or);
		 assertEquals(or, s.getOrientation());

	}

	@Test
	void getMoveProbabilityTest() {

		 Point shelterPos = new Point(0, 0);
		 Point hunterPos = new Point(5,5);
		
		 Shelter s= world10.createShelter(shelterPos, Orientation.createRandom());
		 
		 assertEquals(Constants.SHELTER_MOVE_PROBABILITY, s.getMoveProbability());
	      
		 Hunter h = world10.createHunter(s, hunterPos, Orientation.createRandom());

		 assertEquals(Constants.HUNTER_MOVE_PROBABILITY, h.getMoveProbability());
	      
	}
	
	@Test
	void setOrientationTest() {

		 Orientation orN = Orientation.north();
		 Orientation orS = Orientation.south();
		 
		 Shelter s= world10.createShelter(new Point(5,5), orN);
		 assertEquals(orN, s.getOrientation());
		 
		 s.setOrientation(orS);
		 assertEquals(orS, s.getOrientation());

	}

	
	@Test
	void turnClockwiseTest() {

		 Orientation orN = Orientation.north();
		 Orientation orNE = Orientation.northEast();
		 Shelter s= world10.createShelter(new Point(5,5), orN);
		 assertEquals(orN, s.getOrientation());
		 
		 s.turnClockwise();
		 assertEquals(orNE, s.getOrientation());

		 for(int i=0;i<7;i++) {
			 
			 s.turnClockwise();
		 }
		 assertEquals(orN, s.getOrientation());
	}
	
	@Test
	void turnCounterClockwiseTest() {

		 Orientation orN = Orientation.north();
		 Orientation orNW = Orientation.northWest();
		 Shelter s= world10.createShelter(new Point(5,5), orN);
		 assertEquals(orN, s.getOrientation());
		 
		 s.turnCounterclockwise();
		 assertEquals(orNW, s.getOrientation());
		 
		 for(int i=0;i<7;i++) {
			 
			 s.turnCounterclockwise();
		 }
		 assertEquals(orN, s.getOrientation());


	}
	
	@Test
	void destination() {
		 Orientation orN = Orientation.north();
		 Orientation orNE = Orientation.northEast();
		 Orientation orE = Orientation.east();
		 Orientation orSE = Orientation.southEast();
		 Orientation orS = Orientation.south();
		 Orientation orSW = Orientation.southWest();
		 Orientation orW = Orientation.west();
		 Orientation orNW = Orientation.northWest();
		 
		 Point pos = new Point(0,0);
		 Point posN = new Point(0,-1);
		 Point posNE = new Point(1,-1);
		 Point posE = new Point(1,0);
		 Point posSE = new Point(1,1);
		 Point posS = new Point(0,1);
		 Point posSW = new Point(-1,1);
		 Point posW = new Point(-1,0);
		 Point posNW = new Point(-1,-1);
		 
		 
		 Shelter s= world10.createShelter(pos, orN);
			
		 assertEquals(posN, s.destination());

		 s.setOrientation(orNE);
		 assertEquals(posNE, s.destination());

		 s.setOrientation(orE);
		 assertEquals(posE, s.destination());

		 s.setOrientation(orSE);
		 assertEquals(posSE, s.destination());

		 s.setOrientation(orS);
		 assertEquals(posS, s.destination());

		 s.setOrientation(orSW);
		 assertEquals(posSW, s.destination());

		 s.setOrientation(orW);
		 assertEquals(posW, s.destination());

		 s.setOrientation(orNW);
		 assertEquals(posNW, s.destination());

	}
	@Test
	void moveForward() {
	    // Define orientations
	    Orientation orN = Orientation.north();
	    Orientation orNE = Orientation.northEast();
	    Orientation orE = Orientation.east();
	    Orientation orSE = Orientation.southEast();
	    Orientation orS = Orientation.south();
	    Orientation orSW = Orientation.southWest();
	    Orientation orW = Orientation.west();
	    Orientation orNW = Orientation.northWest();
	    
	    // Define initial positions
	    Point startPos = new Point(5, 5);
	    Point posN = new Point(5, 4);
	    Point posNE = new Point(6, 4);
	    Point posE = new Point(6, 5);
	    Point posSE = new Point(6, 6);
	    Point posS = new Point(5, 5);
	    Point posSW = new Point(5, 5);
	    Point posW = new Point(5, 5);
	    Point posNW = new Point(5, 5);
	    
	    // Create Shelter object with initial orientation (north) at startPos
	    Shelter s = world10.createShelter(startPos, orN);
	    
	    // Assert initial position
	    assertEquals(startPos, s.getPosition());
	    
	    // Test moveForward for each orientation
	    // Note: Make sure to check that no position becomes negative after moving forward
	    s.moveForward();
	    assertEquals(posN, s.getPosition());
	    
	    s.setOrientation(orS);
	    s.moveForward();
	    assertEquals(posS, s.getPosition());
	  
	    s.setOrientation(orNE);
	    s.moveForward();
	    assertEquals(posNE, s.getPosition());
	    
	    
	    s.setOrientation(orSW);
	    s.moveForward();
	    assertEquals(posSW, s.getPosition());
	    
	    
	    s.setOrientation(orE);
	    s.moveForward();
	    assertEquals(posE, s.getPosition());
	    

	    s.setOrientation(orW);
	    s.moveForward();
	    assertEquals(posW, s.getPosition());

	    
	    s.setOrientation(orSE);
	    s.moveForward();
	    assertEquals(posSE, s.getPosition());
	    
	    s.setOrientation(orNW);
	    s.moveForward();
	    assertEquals(posNW, s.getPosition());
	    
	    s.setOrientation(orN);
	    
	    // go to border
	    for(int i=0;i<5;i++) {
			 
			 s.moveForward();
		}
	 
	    //now pos will be out of border so no change
	    
	    Point p = s.getPosition();
	    //Pos 5,0
	    
	    s.moveForward();
	    
	    assertEquals(p,s.getPosition());
	    System.out.println(p.toString());
	    
	    Point nextToS1= new Point(5,1);
	    
	    Shelter s2 = world10.createShelter(nextToS1, orN);
	    
	    //s2 can t move northward
    
	    s2.moveForward();	    
	    assertEquals(nextToS1,s2.getPosition());
	    
		   
	}
	
	
	@Test
	void moveForwardWithProbabilityTest() {
		
		// hard to test
	    Point startPos = new Point(5, 5);
	    Orientation orN = Orientation.north();
	    Point posN = new Point(5, 4);
		 
	    Shelter s = world10.createShelter(startPos, orN);
		 
	    s.moveForwardWithProbability();
	    
	    assertTrue(s.getPosition().equals(posN) || s.getPosition().equals(startPos));
		   
		
	}
	
	@Test
	void isPreyTest() {

		 
		 Point shelterPos = new Point(0, 0);
		 Point hunterPos = new Point(5,5);
		 Point preyPos = new Point(9,9);
		 
		 Shelter s= world10.createShelter(shelterPos, Orientation.createRandom()); 
		 Hunter h = world10.createHunter(s, hunterPos, Orientation.createRandom());
		 Prey p = world10.createPrey(s, Chromosome.createRandom() ,preyPos, Orientation.createRandom());
		 
		
		 assertTrue(p.isPrey());
		 assertFalse(h.isPrey());
		 assertFalse(s.isPrey());
	
	}
	
	@Test
	void isHunterTest() {

		 
		 Point shelterPos = new Point(0, 0);
		 Point hunterPos = new Point(5,5);
		 Point preyPos = new Point(9,9);
		 
		 Shelter s= world10.createShelter(shelterPos, Orientation.createRandom()); 
		 Hunter h = world10.createHunter(s, hunterPos, Orientation.createRandom());
		 Prey p = world10.createPrey(s, Chromosome.createRandom() ,preyPos, Orientation.createRandom());
		 
		
		 assertFalse(p.isHunter());
		 assertTrue(h.isHunter());
		 assertFalse(s.isHunter());
	
	}
	
	@Test
	void isShelterTest() {

		 
		 Point shelterPos = new Point(0, 0);
		 Point hunterPos = new Point(5,5);
		 Point preyPos = new Point(9,9);
		 
		 Shelter s= world10.createShelter(shelterPos, Orientation.createRandom()); 
		 Hunter h = world10.createHunter(s, hunterPos, Orientation.createRandom());
		 Prey p = world10.createPrey(s, Chromosome.createRandom() ,preyPos, Orientation.createRandom());
		 
		
		 assertFalse(p.isShelter());
		 assertFalse(h.isShelter());
		 assertTrue(s.isShelter());
	
	}
	@Test
	void getColorTest() {

		 
		 Point shelterPos = new Point(0, 0);
		 Point hunterPos = new Point(5,5);
		 Point preyPos = new Point(9,9);
		 
		 Shelter s= world10.createShelter(shelterPos, Orientation.createRandom()); 
		 Hunter h = world10.createHunter(s, hunterPos, Orientation.createRandom());
		 Prey p = world10.createPrey(s, Chromosome.createRandom() ,preyPos, Orientation.createRandom());
		 
		
		 assertEquals(s.getColor(),Color.BLACK);
		 assertEquals(h.getColor(),Color.RED);
		 assertEquals(p.getColor(),Color.GREEN);

			
			
	
	}
	
	
	
}
