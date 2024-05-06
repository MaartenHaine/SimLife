package other;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import sim.Chromosome;
import sim.entities.Prey;
import sim.entities.Shelter;
import sim.entities.World;
import util.Orientation;
import util.Point;

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
		Shelter s = world10.createShelter(new Point(5,5), Orientation.createRandom());
		Prey p = world10.createPrey(s,Chromosome.createRandom(),new Point(5,5), Orientation.createRandom());
		p.moveForward();
		
	}
	
	@Test
	void HunterFlaws() {
		
	}
	@Test
	void MortalEntityFlaws() {
		
	}
	@Test
	void PreyFlaws() {
		
	}
	
	@Test
	void ShelterFlaws() {
		
	}
	
	@Test
	void WorldFlaws() {
		
	}
	
	// =================================
	// 		END OF ENTITY FLAWS
	// =================================

}
