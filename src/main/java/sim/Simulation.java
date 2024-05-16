package sim;

import sim.entities.World;
import util.Orientation;
import util.Point;
import util.RandomUtil;

import java.util.ArrayList;

import sim.entities.Prey;
import sim.entities.Shelter;

public class Simulation
{
	private final int inhabitantsPerShelter;
	
	/**

	 * not a representation object for performance
	 * @invar| worldSize>0
	 * @invar| preyCount>=0
	 * @invar| huntersPerShelter>=0
	 * @invar| shelterCount>=0
	 * @invar| world != null
	 * @invar| inhabitantsPerShelter>=0
	 * @invar| preyCount == inhabitantsPerShelter*shelterCount
	 * @invar er kunnen niet meer entities zijn dan plaats in de wereld
	 * | preyCount+huntersPerShelter*shelterCount+shelterCount<=worldSize*worldSize
	 */
	private World world;


	
	private final int worldSize;
	
    private final int preyCount; // Number of preys a new world should be inhabited with
    
    private final int huntersPerShelter;

    private final int shelterCount;
    
    
    /**
     * @throws IllegalArgumentException| worldSize<=0 || shelterCount<=0 || inhabitantsPerShelter<0 || huntersPerShelter<0
     * @throws IllegalArgumentException| worldSize*worldSize < shelterCount+shelterCount*inhabitantsPerShelter+shelterCount*huntersPerShelter
     * @post | getWorld().getHeight() == worldSize
     * @post | getWorld().getWidth() == worldSize
     * @post | getWorld().getEntities().size() - getWorld().getHunters().size() - getWorld().getPreys().size() == shelterCount
     * @post | getWorld().getHunters().size()/(getWorld().getEntities().size() - getWorld().getHunters().size() 
     * 		 | - getWorld().getPreys().size()) == huntersPerShelter
     * @post | getWorld().getPreys().size()/(getWorld().getEntities().size() - getWorld().getHunters().size() 
     * 		 | - getWorld().getPreys().size()) == inhabitantsPerShelter
     * @post | getWorld().getEntities().size() == shelterCount * (inhabitantsPerShelter + huntersPerShelter + 1)
     */
    public Simulation(int worldSize, int shelterCount, int inhabitantsPerShelter, int huntersPerShelter)
    {
    	if (shelterCount < 0 || worldSize < 0 || huntersPerShelter < 0 || inhabitantsPerShelter < 0) { throw new IllegalArgumentException(); }
    	
    	// world size is bigger than amount of creatures in it
    	if (worldSize*worldSize < shelterCount+shelterCount*inhabitantsPerShelter+shelterCount*huntersPerShelter) {
    		throw new IllegalArgumentException();
    	}
    	
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
	 * 
	 * DOES NOT HAVE TO BE DOCUMENTED -> PRIVATE
	 * 
	 */
	private World createRandomWorldWith(ArrayList<Chromosome> chromosomes)
	{
		var world = new World(worldSize, worldSize);
		
		ArrayList<Point> positions = new ArrayList<Point>(world.givePositionStream().toList());
		
		
		RandomUtil.shuffle(positions);
	
		for (int i = 0; i < shelterCount; i++)
		{
			Shelter shelter = world.createShelter(positions.get(i), Orientation.createRandom());
			
		
			for(int j=0; j<inhabitantsPerShelter;j++) {
	
				world.createPrey(shelter, chromosomes.get(i*inhabitantsPerShelter+j), 
						positions.get(shelterCount+j+i*inhabitantsPerShelter), 
						Orientation.createRandom());
					
			}
			
			for(int j=0; j<huntersPerShelter;j++) {
		
				world.createHunter(shelter, 
						positions.get(shelterCount+shelterCount*inhabitantsPerShelter+j+i*huntersPerShelter),
						Orientation.createRandom());
				
			}
			
			
			
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
     * and creates a new world based on that latter list.
     * 
	 * @creates | getWorld()
	 * @post | old(getWorld()).numberOfEntities()== getWorld().numberOfEntities()
	 * @post | old(getWorld()).getPreys().size()== getWorld().getPreys().size()
	 * @post | old(getWorld()).getHunters().size()== getWorld().getHunters().size()
	 * @post | old(getWorld()).getWidth()  == getWorld().getWidth() 
	 * @post | old(getWorld()).getHeight()  == getWorld().getHeight() 
	 * 
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
			res.addAll(parentGeneration);
		}else {

			for (int i = 0; i < preyCount; i++)
			{
				Chromosome parent1 = parentGeneration.get(RandomUtil.integer(parentGeneration.size()));
				Chromosome parent2 = parentGeneration.get(RandomUtil.integer(parentGeneration.size()));
				Chromosome offspring = computeOffspring(parent1, parent2);
				res.add(offspring);
			}
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
