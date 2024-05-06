package sim;

import static util.Logic.*;


import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import sim.entities.World;
import util.Orientation;
import util.Point;
import util.RandomUtil;
import sim.Constants;
import sim.entities.Prey;
import sim.entities.Shelter;

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
    
    
    /**
	 * Initializes a new simulation with the given parameters.
	 * 
	 * @param worldSize The size of the world
	 * @param shelterCount The number of shelters
	 * @param inhabitantsPerShelter The number of preys per shelter
	 * @param huntersPerShelter The number of hunters per shelter
	 * 
	 * @pre | worldSize > 0
	 * @pre | shelterCount > 0
	 * @pre | inhabitantsPerShelter > 0
	 * @pre | huntersPerShelter > 0
	 * 
	 * @post | getWorld() != null
	 */

    public Simulation(int worldSize, int shelterCount, int inhabitantsPerShelter, int huntersPerShelter)
    {
    	this.worldSize = worldSize;
    	this.shelterCount = shelterCount;
    	this.preyCount = shelterCount*inhabitantsPerShelter;
    	this.inhabitantsPerShelter = inhabitantsPerShelter;
    	this.huntersPerShelter = huntersPerShelter;
    	this.world = createRandomWorld(preyCount);
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
	 * shelterCount shelters are created, put on random positions and having random orientations.
	 * For each shelter, inhabitantsPerShelter preys are added to the world, again with random positions and orientations.
	 * Each prey is given one of the offspring chromosomes (each offspring chromosome is given to exactly one prey).
	 * For each shelter, huntersPerShelter hunters are added to the world, with a random position and orientation.
	 * @pre | chromosomes != null
	 * @pre | chromosomes.size() == preyCount
	 * 
	 * @post | result.getPreys().size() == shelterCount*inhabitantsPerShelter
	 * @post | result.getHunters().size() == shelterCount*huntersPerShelter
	 * @post | result != null
	 */
	private World createRandomWorldWith(ArrayList<Chromosome> chromosomes)
	{
		var world = new World(worldSize, worldSize);
		
		ArrayList<Point> positions = new ArrayList<Point>(world.givePositionStream().toList());
		
		
		RandomUtil.shuffle(positions);
		System.out.println(positions.size());	
		
		for (int i = 0; i < shelterCount; i++)
		{
			Shelter shelter = world.createShelter(positions.get(i), Orientation.createRandom());
			
			//System.out.println(i);		
			
			for(int j=0; j<inhabitantsPerShelter;j++) {
				/*
				Point inhab_pos = positions.get(RandomUtil.integer(positions.size()));
				
				while(!world.isFree(inhab_pos)) {
					inhab_pos = positions.get(RandomUtil.integer(positions.size()));
				}
				*/
				world.createPrey(shelter, chromosomes.get(i*inhabitantsPerShelter+j), 
						positions.get(shelterCount+j+i*inhabitantsPerShelter), 
						Orientation.createRandom());
						//System.out.println(shelterCount+j+i*inhabitantsPerShelter);
				
			}
			
			for(int j=0; j<huntersPerShelter;j++) {
				/*
				Point hunter_pos = positions.get(RandomUtil.integer(positions.size()));
				
				while(!world.isFree(hunter_pos)) {
					hunter_pos = positions.get(RandomUtil.integer(positions.size()));
				}*/
				world.createHunter(shelter, 
						positions.get(shelterCount+shelterCount*inhabitantsPerShelter+j+i*huntersPerShelter),
						Orientation.createRandom());
						//System.out.println(shelterCount+shelterCount*inhabitantsPerShelter+j+i*inhabitantsPerShelter);		
				
			}
			
			
			/*
			boolean bool2 = true;
			boolean bool1 = true;
			for (int j = 0; j < inhabitantsPerShelter; j++)
			{
				
				while(bool1) {
					Point inhab_pos = positions.get(RandomUtil.integer(positions.size()));
					if (world.isFree(inhab_pos))
					{
						var prey = world.createPrey(shelter, chromosomes.get(i*inhabitantsPerShelter+j), inhab_pos, Orientation.createRandom());
						bool1 = false;
					}

				}
				
			}
			for (int j = 0; j < huntersPerShelter; j++)
			{
				
				while(bool2){
					Point hunter_pos = positions.get(RandomUtil.integer(positions.size()));
					if (world.isFree(hunter_pos))
					{
						var hunter = world.createHunter(shelter, hunter_pos, Orientation.createRandom());
						bool2 = false;
					}

				}
				
			}
			*/
			
		}
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
     * @inspects | getWorld()
	 * @creates | getWorld()
	 * @mutates | this
     */
    public void nextGeneration()
    {

    	var survivingChromosomes = getSurvivingChromosomes();
    	var offspringChromosomes = computeOffspring(survivingChromosomes);
    	
    	this.world = createRandomWorldWith(new ArrayList<>(offspringChromosomes));
    	
    }
    

	// will get all the chromosomes of all the preys that survived
    private ArrayList<Chromosome> getSurvivingChromosomes()
    {
		ArrayList<Chromosome> res = new ArrayList<Chromosome>();

		for (Prey prey : this.world.getPreys())
		{
			if (prey.survives()){
				res.add(prey.getChromosome());
			}
		}
    	//will display how many preys survive at each generation
    	System.out.println(String.format("%d preys survived", res.size()));
    	
    	return res;
    }
    
    /**
     * If parentGeneration is empty, returns a random list of chromosomes of size preyCount.
	 * The offspring chromosomes are computed.
	 * Two random chromosomes are picked from the pool of parent chromosomes.
	 * For simplicity’s sake we allow the same chromosome to be picked twice.
	 * The two selected parent chromosomes are crossed over.
	 * Next, there is a Constants.MUT_RATE chance that a mutation is applied.
	 * We repeat this process preyCount times.
	 * This yields preyCount offspring chromosomes.
	 * 
	 * @post | result != null
	 * @post | result.size() == preyCount
	 * @post | result.stream().allMatch(c -> c != null)
     */
    private ArrayList<Chromosome> computeOffspring(ArrayList<Chromosome> parentGeneration)
    {	
		ArrayList<Chromosome> res = new ArrayList<Chromosome>();

		if (parentGeneration.isEmpty()) {
			parentGeneration = Chromosome.createRandom(preyCount);
		}

		for (int i = 0; i < preyCount; i++)
		{
			var parent1 = parentGeneration.get(RandomUtil.integer(parentGeneration.size()));
			var parent2 = parentGeneration.get(RandomUtil.integer(parentGeneration.size()));
			var offspring = computeOffspring(parent1, parent2);
			res.add(offspring);
		}
    	//can use method below
    	return res;
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
