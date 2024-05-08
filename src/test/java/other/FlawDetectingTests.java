package other;

import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.stream.IntStream;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import sim.Constants;
import sim.Chromosome;
import sim.Simulation;
import sim.entities.*;
import sim.neuralnet.*;
import util.*;

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

	// =================================
	// 			CHROMOSOME FLAWS
	// =================================
	
	@Test
	/**
	 * FLAW1
	 * The `weights` array reference is encapsulated by Chromosome constructor
	 */
	void chromosomeEncapsIn() {
		int[] weights = new int[Constants.CHROM_SIZE];
		for (int i = 0 ; i < weights.length ; i++) {
			weights[i] = Constants.GENE_MIN;
		}
		
		Chromosome chrom = new Chromosome(weights);
		weights[0] = Constants.GENE_MAX;
		
		assertNotEquals( Constants.GENE_MAX , chrom.getGene(0));
	}
	
	// =================================
	// 		END OF CHROMOSOME FLAWS
	// =================================

	// =================================
	// 			SIMULATION FLAWS
	// =================================
	
	@Test
	/**
	 * FLAW1
	 * Simulation test if defensive is ok
	 */
	void SimulationDefensive() {
		assertThrows(IllegalArgumentException.class, () ->new Simulation(-1, 1, 1, 1) );
		assertThrows(IllegalArgumentException.class, () ->new Simulation(1, -1, 1, 1) );
		assertThrows(IllegalArgumentException.class, () ->new Simulation(1, 1, -1, 1) );
		assertThrows(IllegalArgumentException.class, () ->new Simulation(1, 1, 1, -1) );
		assertThrows(IllegalArgumentException.class, () ->new Simulation(-1, -1, 1, 1) );
		assertThrows(IllegalArgumentException.class, () ->new Simulation(1, -1, -1, 1) );
		assertThrows(IllegalArgumentException.class, () ->new Simulation(1, 1, -1, -1) );
		assertThrows(IllegalArgumentException.class, () ->new Simulation(-1, 1, 1, -1) );
		assertThrows(IllegalArgumentException.class, () ->new Simulation(-1, -1, 1, -1) );
		assertThrows(IllegalArgumentException.class, () ->new Simulation(1, -1, -1, 1) );
		assertThrows(IllegalArgumentException.class, () ->new Simulation(-1, 1, -1, 1) );
		assertThrows(IllegalArgumentException.class, () ->new Simulation(-1, 1, -1, -1) );
		assertThrows(IllegalArgumentException.class, () ->new Simulation(-1, -1, -1, -1) );
		}
	
	
	// =================================
	// 			SIMULATION FLAWS
	// =================================
	
	
	
	// ===================================
	// 			NEURALNETWORKS FLAWS
	// ===================================
	
	@Nested
	class EncapsulationNN {
		@Test
		/**
		 * FLAW1
		 * The ArrayList reference is encapsulated
		 */
		void activationFunctionNeuronEncapsOut1() {
			ActivationFunctionNeuron aneuron = new LinearFunctionNeuron();
			ArrayList<Pair<Neuron, Integer>> leak = aneuron.getDependencies();
			leak.add(new Pair<Neuron, Integer>(new FreePassageSensorNeuron(Orientation.createRandom()),500));
			assertTrue(aneuron.getDependencies().isEmpty() );
		}
		
		@Test
		/**
		 * FLAW2
		 * Each Pair reference in the array list field is encaspulated
		 */
		void activationFunctionNeuronEncapsOut2() {
			ActivationFunctionNeuron aneuron = new LinearFunctionNeuron();
			aneuron.connect(new FreePassageSensorNeuron(Orientation.createRandom()),500);
			
			assertEquals(1, aneuron.getDependencies().size());
			
			Pair<Neuron, Integer> leak = aneuron.getDependencies().get(0);
			leak.setSecond(600);
			
			assertNotEquals(600, aneuron.getDependencies().get(0).getSecond());
		}
		
		@Test
		/**
		 * FLAW3
		 * Similar to FLAW1 but now using the chromosome itself that has been put in instead of the on that gets copied
		 */
		void activationFunctionNeuronEncapsIn() {
			ActivationFunctionNeuron afn = new LinearFunctionNeuron();
			SensorNeuron hon = new FreePassageSensorNeuron(Orientation.createRandom());
			
			var arlist = new ArrayList<Pair<Neuron, Integer>>();
			arlist.add(new Pair<Neuron, Integer>(hon, 0));
			
			afn.setDependencies(arlist);
			
			assertEquals(1, afn.getDependencies().size());
			arlist.clear();
			assertEquals(1, afn.getDependencies().size());
			
		}
		
		@Test
		/**
		 * FLAW4
		 * Same as FLAW2 But using the input itself similar to FLAW3
		 */
		void activationFunctionNeuronEncapsIn2() {
			ActivationFunctionNeuron afn = new LinearFunctionNeuron();
			SensorNeuron hon = new FreePassageSensorNeuron(Orientation.createRandom());
			var arlist = new ArrayList<Pair<Neuron, Integer>>();
			arlist.add(new Pair<Neuron, Integer>(hon, 0));
			
			afn.setDependencies(arlist);
			
			Pair<Neuron, Integer> p = arlist .get(0);
			p.setSecond(50);
			
			assertNotEquals(50, afn.getDependencies().get(0).getSecond());
		}
	
	
		
		@Test
		/**
		 * FLAW5
		 * The array reference NeuralNetwork.inputLayerNeurons must be encapsulated
		 */
		void neuralNetworkEncapsOut() {
			NeuralNetwork nnet = new NeuralNetwork();
			SensorNeuron[] inputNeurons = nnet.getInputNeurons();
			assertTrue(inputNeurons.length > 0);
			assertNotNull(inputNeurons[0]);
			
			inputNeurons[0] = null;
			assertNotNull(nnet.getInputNeurons()[0]);
		}
	}
	
	@Nested
	class DefensiveNN {
		
		@Test
		/**
		 * FLAW6
		 */
		void freePassageSensorNeuronDefensive() {
			assertThrows(IllegalArgumentException.class, () ->new FreePassageSensorNeuron(null) );
		}
	}
	
	@Test
	/**
	 * FLAW7
	 * 
	 * Connecting with a Neuron when the list is full should have no effect and return false
	 * 
	 * flaw: return true anyway
	 */
	void activationFunctionNeuronConnectUpholdsInvar() {
		ActivationFunctionNeuron afn = new LinearFunctionNeuron();
		SensorNeuron hon = new FreePassageSensorNeuron(Orientation.createRandom());
		var arlist = new ArrayList<Pair<Neuron, Integer>>();
		for (int i = 0 ; i < 7  ; i++) {
			arlist.add(new Pair<Neuron, Integer>(hon, i));
		}
		
		afn.setDependencies(arlist);
		
		assertEquals(false, afn.connect(hon, 7));
		for (int i = 0 ; i < 7 ; i++) {
			int val = afn.getDependencies().get(i).getSecond();
			assertEquals(i , val);
		}
	}
	// ======================================
	// 		END OF NEURALNETWORKS FLAWS
	// ======================================



}
