package sim;

import static util.Logic.*;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import sim.entities.World;
import util.Orientation;
import util.Point;
import util.RandomUtil;

public class Simulation
{
	private final int inhabitantsPerShelter;
	
	/**
	 * not a representation object for performance
	 */
	private World world;


	
	private final int worldSize;
	
    private final int preyCount; // Number of preys a new world should be inhabited with
    
    private final int huntersPerShelter;

    private final int shelterCount;
    
    public Simulation(int worldSize, int shelterCount, int inhabitantsPerShelter, int huntersPerShelter)
    {
    	this.worldSize = worldSize;
    	this.shelterCount = shelterCount;
    	this.preyCount = shelterCount*inhabitantsPerShelter;
    	this.inhabitantsPerShelter = inhabitantsPerShelter;
    	this.huntersPerShelter = huntersPerShelter;
    	this.world = new World(worldSize, worldSize);
    }
    
	/**
	 * @post | result != null
	 */
	public World getWorld() {
		return this.world;
	}
	
	/**
	 * Returns a World with the appropriate number of shelters/preys/hunters.
	 * The number of inhabitants and hunters per shelter match the above private fields.
	 * Their positions/orientations etc are picked randomly.
	 * No 2 entities have the same position.
	 * 
	 * 
	 */
	private World createRandomWorldWith(ArrayList<Chromosome> chromosomes)
	{
		var world = new World(worldSize, worldSize);
		
		ArrayList<Point> positions = new ArrayList<Point>(world.givePositionStream().toList());
		RandomUtil.shuffle(positions);

		return world;
	}
    
	/**
	 * LEGIT
	 */
    private World createRandomWorld(int preyCount)
    {
    	var chromosomes = Chromosome.createRandom(preyCount);
    	return createRandomWorldWith(chromosomes);
    }
    
    /**
     * Compute the list of surviving chromosomes, the list of offspring chromosomes,
     * and returns a new world based on that latter list.
     */
    public void nextGeneration()
    {
    	var survivingChromosomes = getSurvivingChromosomes();
    	var offspringChromosomes = computeOffspring(survivingChromosomes);
    	
    	createRandomWorldWith(new ArrayList<>(offspringChromosomes));
    	
    }
    
    private ArrayList<Chromosome> getSurvivingChromosomes()
    {

    	
    	ArrayList<Chromosome> res = null; //todo
    	
    	//will display how many preys survive at each generation
    	System.out.println(String.format("%d preys survived", res.size()));
    	
    	return res;
    }
    
    /**
     * If parentGeneration is empty, returns a random list of chromosomes of size preyCount.
     */
    private ArrayList<Chromosome> computeOffspring(ArrayList<Chromosome> parentGeneration)
    {
    	//can use method below
    	return null;
    }
    
    /**
     * LEGIT
     */
    private Chromosome computeOffspring(Chromosome parent1, Chromosome parent2)
    {
    	var offspring = parent1.crossover2(parent2);
    	
    	if ( RandomUtil.integer(100) < Constants.MUT_RATE )
        {
            offspring = offspring.randomlyMutate();
        }
    	
    	return offspring;
    }
}
