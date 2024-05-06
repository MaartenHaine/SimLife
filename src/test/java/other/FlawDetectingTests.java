package other;

import static org.junit.jupiter.api.Assertions.*;

import java.util.stream.IntStream;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import sim.Chromosome;
import sim.entities.Hunter;
import sim.entities.MortalEntity;
import sim.entities.Prey;
import sim.entities.Shelter;
import sim.entities.World;
import util.Grid;
import util.Orientation;
import util.Point;
import sim.entities.Entity;

/**
 * All flaw detecting tests should go in here
 * ELKE TEST HIER MOET (!!!) FALEN OP DE OUDE IMPLEMENTATIE
 */
class FlawDetectingTests {

	private World world10;
	

	@BeforeEach
	void createWorld() {
		world10 = new World(10,10);
	}
	
	// =================================
	// 			ENTITY FLAWS
	// =================================
	
	
	@Test
	void EntityFlaws() {
		// FLAW IN MOVE FORWARD 
		Shelter s = world10.createShelter(new Point(1,1), Orientation.createRandom());
		Prey p = world10.createPrey(s,Chromosome.createRandom(),new Point(5,5), Orientation.createRandom());
		p.moveForward();
		world10.getPreys().get(0).getPosition();
		
		assertTrue(world10.getPreys().get(0).getPosition().equals(p.getPosition()));
		
		
	}
	
	@Test
	void HunterFlaws() {
		// CONSTRUCTOR WASNT DEFENSIVE
		assertThrows(IllegalArgumentException.class,
				()-> {
					world10.createHunter(null,new Point(5,5),Orientation.createRandom());
				});
		// it is not possible to check that the constructor also throws an illegal argument exception
		// when appetite is zero or lower
		
	}
	@Test
	void MortalEntityFlaws() {
		// DIEpkg was flawed
		Shelter s = world10.createShelter(new Point(1,1), Orientation.createRandom());
		
		Point preyPos= new Point(5,5);
		Prey p = world10.createPrey(s,Chromosome.createRandom(),preyPos, Orientation.createRandom());
		
		p.die();
		
		assertTrue(world10.getEntityAt(preyPos)==null);
		assertTrue(p.getWorld()==null);
	}
	@Test
	void PreyFlaws() {
		// FLAW in constructor: REPRESENTATION EXPOSURE
		
		Shelter s = world10.createShelter(new Point(1,1), Orientation.createRandom());

		Prey p1 = world10.createPrey(s,Chromosome.createRandom(),new Point(5,5), Orientation.createRandom());
		Prey p2 = world10.createPrey(s,Chromosome.createRandom(),new Point(6,6), Orientation.createRandom());
		// in old impl they would share the same siblings array, this cannot be directly tested though
		
		assertTrue(s.getInhabitants().contains(p1));
		assertTrue(s.getInhabitants().contains(p2));
		
		p2.die();
		
		assertTrue(s.getInhabitants().contains(p1));
		assertFalse(s.getInhabitants().contains(p2));
		
	}
	
	@Test
	void ShelterFlaws() {
		//FLAW REPRESENTATION EXPOSURE FOR getInHabitants
		Shelter s = world10.createShelter(new Point(1,1), Orientation.createRandom());

		Prey p1 = world10.createPrey(s,Chromosome.createRandom(),new Point(5,5), Orientation.createRandom());
		
		assertTrue(s.getInhabitants().size()==1);
		s.getInhabitants().add(p1);
		assertTrue(s.getInhabitants().size()==1);
		
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
	
	// =================================
	// 		END OF ENTITY FLAWS
	// =================================

}
