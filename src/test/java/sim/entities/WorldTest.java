package sim.entities;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import sim.Chromosome;
import sim.Constants;
import util.Grid;
import util.Orientation;
import util.Point;

class WorldTest {
	
	private World world10;
	

	@BeforeEach
	void createWorld() {
		world10 = new World(10,10);
	}
	

	@Test
	void constructorTest() {
		
		 // Test invalid arguments
        assertThrows(IllegalArgumentException.class, () -> new World(0,10));
        assertThrows(IllegalArgumentException.class, () -> new World(10,0));
        
        World wereldje = new World(10,5);
        
        assertTrue(wereldje.getHunters().isEmpty());
        assertTrue(wereldje.getEntities().isEmpty());
        assertTrue(wereldje.getHeight()==5);
        assertTrue(wereldje.getWidth()==10);
        assertTrue(wereldje.numberOfEntities()==0);
		
	}
	
	@Test
	void getWidthTest() {
		World wereldje = new World(10,5);
		assertTrue(wereldje.getWidth()==10);
	}
	
	@Test
	void getHeightTest() {
		World wereldje = new World(10,5);
		assertTrue(wereldje.getHeight()==5);
	}
	
	@Test
	void getEntitiesTest() {
		
		assertTrue(world10.getEntities().isEmpty());

		Point shelterPosition = new Point(0, 0);
	    Point hunterPosition = new Point(1, 0);	 
	    Point preyPosition=new Point(5,5);
	    Shelter s = world10.createShelter(shelterPosition, Orientation.north());
	 	
	    
	    List<Entity> ents = new ArrayList<Entity>();
	    ents.add(s);
	    assertTrue(world10.getEntities().equals(ents));

		
	    
	    Chromosome c= Chromosome.createRandom();
        Prey p =world10.createPrey(s, c, preyPosition,Orientation.north());
        ents.add(p);
	    assertTrue(world10.getEntities().equals(ents));
	    
	    Hunter h = world10.createHunter(s,hunterPosition, Orientation.north());
	    ents.add(h);
		assertTrue(world10.getEntities().equals(ents));

		s.die();
		ents.remove(s);
		assertTrue(world10.getEntities().equals(ents));

		p.die();
		ents.remove(p);
		assertTrue(world10.getEntities().equals(ents));

		
	}
	
	@Test
	void getPreysTest() {
		
		assertTrue(world10.getPreys().isEmpty());

		Point shelterPosition = new Point(0, 0);
	    Point preyPosition=new Point(5,5);
	    Shelter s = world10.createShelter(shelterPosition, Orientation.north());
	 	

	    assertTrue(world10.getPreys().isEmpty());

		
	    
	    ArrayList<Prey> ents = world10.getPreys();

	    Chromosome c= Chromosome.createRandom();
        Prey p =world10.createPrey(s, c, preyPosition,Orientation.north());
        ents.add(p);
	    assertTrue(world10.getPreys().equals(ents));
	    
		
	}
	
	@Test
	void getHuntersTest() {
		
		assertTrue(world10.getHunters().isEmpty());

		Point shelterPosition = new Point(0, 0);
	    Point hunterPosition = new Point(1, 0);	 
	    Point preyPosition=new Point(5,5);
	    Shelter s = world10.createShelter(shelterPosition, Orientation.north());
	 	
		assertTrue(world10.getHunters().isEmpty());
		
	    
	   

	    Chromosome c= Chromosome.createRandom();
        Prey p =world10.createPrey(s, c, preyPosition,Orientation.north());
    	assertTrue(world10.getHunters().isEmpty());
    	
	    Hunter h = world10.createHunter(s,hunterPosition, Orientation.north());
	    
	    List<Hunter> ents = new ArrayList<Hunter>();
	    ents.add(h);

		assertTrue(world10.getHunters().equals(ents));

		
	}
	
	
	@Test
	void numberOfEntitiesTest() {
		
		assertTrue(world10.numberOfEntities()==0);

		Point shelterPosition = new Point(0, 0);
	    Point hunterPosition = new Point(1, 0);	 
	    Point preyPosition=new Point(5,5);
	    Shelter s = world10.createShelter(shelterPosition, Orientation.north());
	 	
		
		assertTrue(world10.numberOfEntities()==1);

		
	    
	    Chromosome c= Chromosome.createRandom();
        Prey p =world10.createPrey(s, c, preyPosition,Orientation.north());

    	assertTrue(world10.numberOfEntities()==2);

		
	    Hunter h = world10.createHunter(s,hunterPosition, Orientation.north());

		assertTrue(world10.numberOfEntities()==3);

		
		s.die();

		assertTrue(world10.numberOfEntities()==2);

		p.die();

		assertTrue(world10.numberOfEntities()==1);

		
	}
	
	@Test
	void isInsideTest() {
		World wereldje = new World(10,5);
		assertTrue(wereldje.isInside(new Point(0,0)));
		assertTrue(wereldje.isInside(new Point(5,2)));
		
		
		assertFalse(wereldje.isInside(new Point(10,0)));
		assertFalse(wereldje.isInside(new Point(0,5)));
		assertFalse(wereldje.isInside(new Point(-1,2)));
		assertFalse(wereldje.isInside(new Point(2,-1)));
		assertFalse(wereldje.isInside(new Point(-1,2)));
		
		assertFalse(wereldje.isInside(new Point(11,2)));
		assertFalse(wereldje.isInside(new Point(1,6)));
		
		
	}

    @Test
    void isLimPosTest() {

        // Test points inside the world
        assertTrue(world10.isLimPos(new Point(0, 5))); // On left wall
        assertTrue(world10.isLimPos(new Point(5, 0))); // On top wall
        assertTrue(world10.isLimPos(new Point(9, 5))); // On right wall
        assertTrue(world10.isLimPos(new Point(5, 9))); // On bottom wall
        
        // Test points outside the world
        assertFalse(world10.isLimPos(new Point(-1, 5))); // Outside on left side
        assertFalse(world10.isLimPos(new Point(5, -1))); // Outside on top side
        assertFalse(world10.isLimPos(new Point(10, 5))); // Outside on right side
        assertFalse(world10.isLimPos(new Point(5, 10))); // Outside on bottom side
    
    }
	
    @Test
    void getEntityAtTest() {
    	Shelter entity1 = world10.createShelter(new Point(3, 3),Orientation.createRandom());
        Prey entity2 = world10.createPrey(entity1,Chromosome.createRandom(), new Point(5, 5),Orientation.createRandom());
        Hunter entity3 = world10.createHunter(entity1, new Point(7, 7),Orientation.createRandom());


        // Test getting entities at specific points
        assertEquals(entity1, world10.getEntityAt(new Point(3, 3))); 
        assertEquals(entity2, world10.getEntityAt(new Point(5, 5))); 
        assertEquals(entity3, world10.getEntityAt(new Point(7, 7))); 

        // Test getting entity at a point with no entity
        assertNull(world10.getEntityAt(new Point(0, 0)));
        assertNull(world10.getEntityAt(new Point(9, 9))); 
        }
    
    @SuppressWarnings("unused")
    @Test
    void isFreeTest() {
        Shelter shelter = world10.createShelter(new Point(3, 3), Orientation.createRandom());
        Prey prey = world10.createPrey(shelter, Chromosome.createRandom(), new Point(5, 5), Orientation.createRandom());
        Hunter hunter = world10.createHunter(shelter, new Point(7, 7), Orientation.createRandom());

        // Test points that are free
        assertTrue(world10.isFree(new Point(0, 0))); // Free space
        assertTrue(world10.isFree(new Point(2, 2))); // Free space
        assertTrue(world10.isFree(new Point(6, 6))); // Free space

        // Test points that are occupied by entities
        assertFalse(world10.isFree(new Point(3, 3))); // Occupied by shelter
        assertFalse(world10.isFree(new Point(5, 5))); // Occupied by prey
        assertFalse(world10.isFree(new Point(7, 7))); // Occupied by hunter
    }
    
    @Test
    void stepTest() {
    	
    	// LEGIT FUNCTION: for coverage:
    	world10.step();
    }
    
    @Test
    void createPreyTest() {
    	
    	// LEGIT FUNCTION: for coverage:
    	
		Point shelterPosition = new Point(0, 0);
	    Point preyPosition=new Point(5,5);
	    Shelter s = world10.createShelter(shelterPosition, Orientation.north());
	 	Chromosome c= Chromosome.createRandom();
        world10.createPrey(s, c, preyPosition,Orientation.north());
      
    }
    
    @Test
    void createHunterTest() {
    	
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
   
        ArrayList<Hunter> hunters = new ArrayList<Hunter>();
        hunters.add(h);
        assertEquals(world10.getHunters(),h);
    }
    
    @Test
    void createShelterTest() {
    	
    	// LEGIT FUNCTION: for coverage:
    	
		Point shelterPosition = new Point(0, 0);
	    Point preyPosition=new Point(5,5);
	    Shelter s = world10.createShelter(shelterPosition, Orientation.north());
	 	Chromosome c= Chromosome.createRandom();
        world10.createPrey(s, c, preyPosition,Orientation.north());
      
    }
    
    
	@Test
	void WorldFlawGetEntities() {
		//FLAW getEntities() uses copies of entities
		
		Shelter s = world10.createShelter(new Point(1,1), Orientation.createRandom());
		
		MortalEntity ent = (MortalEntity) world10.getEntities().get(0);
		
		s.die();
		// ent is now Dead
		
		assertTrue(ent.isDead());
		
	}
		
	
	@Test
	void WorldFlawHunterArray() {
		// FLAW createHunter() did not add hunter to hunter array
		
		Shelter s2 = world10.createShelter(new Point(1,1), Orientation.createRandom());
		
		world10.createHunter(s2,new Point(5,5),Orientation.createRandom());
		
		assertEquals(world10.getHunters().size(),1);
	
	}
	@Test
	void WorldFlawGiveEntityGrid() {
		// FLAW REPRESENTATION EXPOSURE WITH giveEntityGrid()
		
		
		
		World newWorld = new World(10,10);
		
		Grid<Entity> g = newWorld.giveEntityGrid();
		
		Entity dummyEntity = world10.createShelter(new Point(5,5), Orientation.createRandom());
		
		Point punt = new Point(4,4);
		g.setAt(punt, dummyEntity);
		
		assertTrue(newWorld.giveEntityGrid().at(punt)==null);
	}

}
