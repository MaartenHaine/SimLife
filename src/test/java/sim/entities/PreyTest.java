package sim.entities;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import sim.Chromosome;
import sim.Constants;
import util.Orientation;
import util.Point;

class PreyTest {

	private World world10;
	

	@BeforeEach
	void createWorld() {
		world10 = new World(10,10);
	}
	
	@Test
    void constructor_DefensiveProgramming() {
       
        Point validPosition = new Point(1, 1);
        Point shelterPosition = new Point(5, 5);
        Point invalidPosition = new Point(-1, -1);
        Shelter s = world10.createShelter(shelterPosition, Orientation.north());
		Chromosome c= Chromosome.createRandom();

        Shelter sdead = world10.createShelter(new Point(2, 2), Orientation.north());
		
        sdead.die();
        //USING CREATION OF SHELTER AS MORTAL ENTITY DUMMY
        // Test null arguments
        assertThrows(IllegalArgumentException.class, () -> world10.createPrey(null, Chromosome.createRandom(), validPosition,Orientation.createRandom()));
        assertThrows(IllegalArgumentException.class, () -> world10.createPrey(s, null, validPosition,Orientation.createRandom()));
        assertThrows(IllegalArgumentException.class, () -> world10.createPrey(s, Chromosome.createRandom(), null,Orientation.createRandom()));
        assertThrows(IllegalArgumentException.class, () -> world10.createPrey(s, Chromosome.createRandom(), validPosition,null));
        assertThrows(IllegalArgumentException.class, () -> world10.createPrey(sdead, Chromosome.createRandom(), validPosition,Orientation.createRandom()));

        // ! UNTESTABLE THAT WORLD IS NULL OR MOVEPROBABILTY WRONG VALUE
        
        
        // Test invalid position
        assertThrows(IllegalArgumentException.class, () -> world10.createShelter(invalidPosition, Orientation.createRandom()));
        
        // Test position already occupied
        Prey p =world10.createPrey(s, c, validPosition,Orientation.north());
        assertThrows(IllegalArgumentException.class, () -> world10.createPrey(s, c, validPosition,Orientation.north()));
       
        // Test validity constructor 
        assertEquals(world10, p.getWorld());
        assertEquals(false, p.isDead());
        assertEquals(validPosition, p.getPosition());
        assertEquals(Orientation.north(), p.getOrientation());
        assertEquals(Constants.PREY_MOVE_PROBABILITY, p.getMoveProbability());
        assertEquals(p, world10.getEntityAt(validPosition));
        assertTrue(Point.isWithin(p.getPosition(), world10.getWidth(), world10.getHeight()));
    
        assertTrue( p.getChromosome().equals(c));
        assertTrue( p.getShelter().equals(s));
    	
	}
	
	@Test
	void survivesTest() {
		
		World bigWorld = new World(2*Constants.SHELTER_SURVIVAL_DISTANCE,2*Constants.SHELTER_SURVIVAL_DISTANCE);
		Point farAwayPosition = new Point(2*Constants.SHELTER_SURVIVAL_DISTANCE-1, 2*Constants.SHELTER_SURVIVAL_DISTANCE-1);
	    Point shelterPosition = new Point(0, 0);
	    Point nextToShelterPosition = new Point(1, 0);
	    Shelter s = bigWorld.createShelter(shelterPosition, Orientation.north());
	 	Chromosome c= Chromosome.createRandom();
        Prey p =bigWorld.createPrey(s, c, farAwayPosition,Orientation.north());
        Prey p2 =bigWorld.createPrey(s, c, nextToShelterPosition,Orientation.north());
        
	    // prey has zero score
	    assertFalse(p.survives());
	    p.performActionIfAlive();  
	    // now prey has negative score
	    assertFalse(p.survives());
	    
        // prey has zero score
        assertFalse(p2.survives());
	    p2.performActionIfAlive();  
	    // now prey has positive score
	    assertTrue(p2.survives());
	    
	    p2.die();
	    assertFalse(p2.survives());
		
	}
	@Test
	void die() {
		Point shelterPosition = new Point(5, 5);
		Point preyPosition = new Point(0, 0);
		Point prey2Position = new Point(3, 3);
        Shelter s = world10.createShelter(shelterPosition, Orientation.north());
	 	Chromosome c= Chromosome.createRandom();
        Prey p =world10.createPrey(s, c, preyPosition,Orientation.north());
        Prey p2 =world10.createPrey(s, c, prey2Position,Orientation.north());
        
        
        p.die();
        assertEquals(true, p.isDead());
        assertTrue(null==world10.getEntityAt(preyPosition));
        assertTrue(null==p.getWorld());
        assertFalse(p2.siblings.contains(p));
        assertTrue(p.siblings.isEmpty());
        assertFalse(s.getInhabitants().contains(p));
        assertTrue(p.getShelter()==null);
	
        
        p2.die();
        // now shelter also dies'
        
        assertTrue(s.isDead());
	}
	@Test
	void getChromosomeTest() {
		Point shelterPosition = new Point(5, 5);
		Point preyPosition = new Point(0, 0);
		Shelter s = world10.createShelter(shelterPosition, Orientation.north());
	 	Chromosome c= Chromosome.createRandom();
        Prey p =world10.createPrey(s, c, preyPosition,Orientation.north());

        assertEquals(p.getChromosome(),c);
        
	}
	
	@Test
	void getShelterTest() {
		Point shelterPosition = new Point(5, 5);
		Point preyPosition = new Point(0, 0);
		Shelter s = world10.createShelter(shelterPosition, Orientation.north());
	 	Chromosome c= Chromosome.createRandom();
        Prey p =world10.createPrey(s, c, preyPosition,Orientation.north());

        assertEquals(p.getShelter(),s);
        
	}
	
	@Test 
	void performActionIfAliveTest()
    {
		Point shelterPosition = new Point(5, 5);
		Point preyPosition = new Point(1, 1);
		Shelter s = world10.createShelter(shelterPosition, Orientation.north());
		
		int[] weights = {331,-193,449,841,-78,-873,106,-970,906,458,417,-673};
        
		Chromosome c= new Chromosome(weights);
		
		 Prey p =world10.createPrey(s, c, preyPosition,Orientation.north());
         p.performAction();
	         
		// p.performAction();

    	assertEquals(p.getOrientation(),Orientation.northEast());
        assertEquals(p.getPosition(),new Point(2,0));
		p.die();
		/* use this code to find genes
		Chromosome c= Chromosome.createRandom();
        boolean cond=true;
        while(cond) {
        	c= Chromosome.createRandom();
            Prey p =world10.createPrey(s, c, preyPosition,Orientation.north());
            p.performAction();
            
            cond=!(p.getOrientation().equals(Orientation.northEast()) && p.getPosition().equals(new Point(2,0)));
            p.die();
        }
        for (int i=0; i<Constants.CHROM_SIZE;i++) {
        	System.out.print(c.getGene(i));
        	System.out.print(",");
        }*/

    }
	
	@Test 
	void isEqualTest()
    {
		World otherWorld = new World(10,10);
		
		Point shelterPosition = new Point(5, 5);
		Point otherShelterPosition = new Point(6, 6);
		Point prey1Position = new Point(0, 0);
		Point prey2Position = new Point(1,1);

		
		Shelter s = world10.createShelter(shelterPosition, Orientation.north());
	 	
		Shelter s2 = world10.createShelter(otherShelterPosition, Orientation.north());
	 	
		Chromosome c= Chromosome.createRandom();
		Chromosome c2;
		
		c2= Chromosome.createRandom();
		// make sure chromosomes differ
		while(c.isEqual(c2)) {
			c2= Chromosome.createRandom();
		}
		Prey p1 =world10.createPrey(s, c, prey1Position,Orientation.north());
       
        assertFalse(p1.isEqual(null));
        
        
        Prey p2 =world10.createPrey(s, c, prey2Position,Orientation.north());
        // pos diff
        assertFalse(p1.isEqual(p2));
        p2.die();
        
        // orient diff (world doenst matter, otherwise illegal arg exc
        Prey p3 =otherWorld.createPrey(s, c, prey1Position,Orientation.east());
        
        assertFalse(p1.isEqual(p3));
        p3.die();
        // shelter diff
        Prey p4 =otherWorld.createPrey(s2, c, prey1Position,Orientation.north());
        
        //WHY DOENST SHELTER MATTER
        assertTrue(p1.isEqual(p4));
        p4.die();
        // chromo diff
        Prey p5 =otherWorld.createPrey(s, c2, prey1Position,Orientation.north());
        
        
        assertFalse(p1.isEqual(p5));
        p5.die();
        // SAME
        Prey p6 =otherWorld.createPrey(s, c, prey1Position,Orientation.north());
        

        assertTrue(p1.isEqual(p6));
        
    
    }
	@Test
	void distanceSquaredToShelterTest() {
		
		Point shelterPosition = new Point(0, 0);
	    Point nextToShelterPosition = new Point(1, 0);
	    Point otherPos=new Point(5,5);
	    Shelter s = world10.createShelter(shelterPosition, Orientation.north());
	 	Chromosome c= Chromosome.createRandom();
        Prey p =world10.createPrey(s, c, otherPos,Orientation.north());
        Prey p2 =world10.createPrey(s, c, nextToShelterPosition,Orientation.north());
       
        assertEquals(p.distanceSquaredToShelter(),50);
        assertEquals(p2.distanceSquaredToShelter(),1);
		
	}
	@Test 
	void toStringTest()
    {
		Point shelterPosition = new Point(5, 5);
		Point preyPosition = new Point(0, 0);
		Shelter s = world10.createShelter(shelterPosition, Orientation.north());
	 	Chromosome c= Chromosome.createRandom();
        Prey p =world10.createPrey(s, c, preyPosition,Orientation.north());
    
        assertEquals(p.toString(), String.format("Prey(position=%s)", p.getPosition()));
    }
    
}
