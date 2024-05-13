package sim.entities;

import java.util.ArrayList;

import sim.Chromosome;
import sim.Constants;
import sim.neuralnet.NeuralNetwork;
import util.Color;
import util.Logic;
import util.Orientation;
import util.Point;
import static util.Logic.*;

/**
 * @invar | getChromosome() !=null
 * 
 * Geen getter voor siblings
 * @invar the siblings arrayList of a prey is not null, however it can be empty
 * @invar every sibling in the siblings arrayList has this as sibling as well
 * 
 * @invar | getShelter() == null  || getShelter().getInhabitants().contains(this) 
 * 
 *  MortalEntity invars
 * @invar | Logic.implies(isDead(),getWorld()==null)
 * @invar | Logic.implies(!isDead(),getWorld() !=null && getWorld().getEntities().contains(this))
 *  
 *  
 * ENTITY INVARS 
 * @invar | getPosition()!=null
 * @invar | getOrientation() != null
 * @invar | 0 <=getMoveProbability() && getMoveProbability()  <= 100
 * @invar if an entity is in a world, the world contains that entity  
 * | getWorld() ==null ||  getWorld().getEntities().contains(this)
 * @invar Entity positie komt overeen met hun positie in world
 * | getWorld() == null || getWorld().getEntityAt(this.getPosition()).equals(this)
 * @invar | getWorld() == null ||  Point.isWithin(getPosition(),getWorld().getWidth(),getWorld().getHeight())
 * @invar| getColor()!=null
 */
public class Prey extends MortalEntity
{
	
	/**
	 * @invar | chromosome !=null
	 */
	private final Chromosome chromosome;

	/**
	 * @representationObject
	 * @invar | neuralNetwork != null
	 */
	private final NeuralNetwork neuralNetwork;

	private int score;

	/**
	 * LEGIT
	 */
	private void updateScore()
	{
		if ( distanceSquaredToShelter() < Constants.SHELTER_SURVIVAL_DISTANCE * Constants.SHELTER_SURVIVAL_DISTANCE )
		{
			score += 1;
		}
		else
		{
			score -= 1;
		}
	}

	private void performTurn()
	{
		/*if it outputs a value less than `-333`, the prey turns clockwise.
		 *if it outputs a value greater than `333`, the prey turns counterclockwise.
		 *for other values, the prey does not turn.
		 */
		int valTurn = neuralNetwork.getTurnNeuron().computeOutput(this);
		if(valTurn<-333) {
			super.turnClockwise();
		}else if(valTurn>333){
			super.turnCounterclockwise();		
		}
	}

	private void performMove()
	{
		//moveForward checks if the position is free
		if (neuralNetwork.getMoveForwardNeuron().computeOutput(this)>0) {moveForward();}
	}
	


	/**
	 * @invar | siblings != null
	 * @invar | siblings.stream().allMatch(ent-> ent==null || ent.siblings.contains(this))
	 * 
	 * @peerObjects
	 * @representationObject
	 */
    final ArrayList<Prey> siblings;

    /**
     * @invar | shelter == null  || shelter.inhabitants.contains(this)
     * @peerObject
     */
    Shelter shelter;
   
    /**
     * @post | result == true
     */
    @Override
	boolean isPreyPkg()
	{
	    return true;
	}
	/**
	 * 
	 * @throws IllegalArgumentException | chromosome == null
	 * @throws IllegalArgumentException | shelter == null
	 * 
     * mutates_properties | shelter.getInhabitants(), this.getShelter()
	 * 
	 * @post | getChromosome().equals(chromosome)
	 * @post | getChromosome() != null
	 * @post | getShelter()==shelter
	 * @post the neural Network of prey is created based upon the chromosome
	 * @post score of the prey is zero
	 * @post the siblings arrayList of a prey is not null, however it can be empty
	 * @post every sibling in the siblings arrayList has this as sibling as well
	 * @post | this.shelter.inhabitants.contains(this) 
	 *  
	 * 
	 * 
	 * ENTITY ALGEMEEN
	 * 
	 * @throws IllegalArgumentException | world==null
	 * @throws IllegalArgumentException | position == null
	 * @throws IllegalArgumentException | orientation == null
	 * @throws IllegalArgumentException | !world.entityGrid.isValidPosition(position)
	 * @throws IllegalArgumentException | world.entityGrid.at(position)!=null
	 *
	 * mutates_properties | this.getWorld()
	 * also mutates world.giveEntityGrid() (this is a very slow operation and thus not documentated in a mutates)
	 *  
	 *  
	 * @post | Logic.implies(old(world).entityGrid.at(position)==null, world == this.world)
	 * @post | getPosition().equals(position)
	 * @post | getOrientation().equals(orientation)
	 * @post | getMoveProbability()==Constants.PREY_MOVE_PROBABILITY
	 * @post | this.world.entityGrid.at(position).equals(this)
	 * post   Point.isWithin(getPosition(),this.world.getWidth(),this.world.getHeight())	
	 * 
	 * @post | isDead() == false
	 */
	Prey(World world, Shelter shelter, Chromosome chromosome, Point position, Orientation orientation)
    {
        super(world, position, orientation, Constants.PREY_MOVE_PROBABILITY);


        if ( chromosome == null || world == null || shelter == null || position == null
        		|| orientation == null )
        {
            throw new IllegalArgumentException();
        }
        
        this.shelter = shelter;
        this.chromosome = chromosome;
        this.neuralNetwork = NeuralNetwork.fromChromosome(chromosome);
        
        //FLAWED this.siblings = this.shelter.inhabitants;
        
        this.score = 0;

        //NEW
        

        // makes copy of inhabitants array
        this.siblings = new ArrayList<Prey>(this.shelter.inhabitants);
        
        
        for (Prey sib : siblings) {
        	sib.siblings.add(this);
        }
        /*
        int range = siblings.size();
        ArrayList<Prey> clone = new ArrayList<Prey>(siblings);
	    for(int i=0; i< range;i++) {
	    	Prey sib = clone.get(i);
	    	sib.siblings.add(this);
	    }
        */
        shelter.inhabitants.add(this);
    }
	/**
     * @post | result == false
     */
    @Override
	boolean isHunterPkg()
	{
	    return false;
	}
	/**
     * @post | result == false
     */
	@Override
	boolean isShelterPkg()
	{
	    return false;
	}

	
    /**
     * @mutates_properties | this.getWorld()
     * also mutates old(getWorld()).giveEntityGrid() (this is a very slow operation and thus not documentated in a mutates)
	 *  
     * 
     * @mutates_properties | old(shelter).getInhabitants(), this.getShelter()
     * @mutates_properties siblings field of this and siblings field of the siblings that were in this' siblings field
     * 
     * @post no occurences of died entity in siblings 
     * | !old(siblings).stream().anyMatch(ent -> ent.equals(this))
     * @post no occurences of died entity in shelter inhabitants 
     * | !old(shelter).inhabitants.stream().anyMatch(ent -> ent.equals(this))
     * @post shelter reference is removed
     * | shelter == null
     * @post siblings list is cleared
     * |  siblings.stream().allMatch(sib->sib==null)
     * 
     * @post | old(getChromosome()).equals(getChromosome())
     * 
     * SUPER:
     * @post | isDead()
     * @post Entity is no part of world anymore | !old(this.world).giveEntityStream().anyMatch(ent->ent==this)
     * @post | this.world==null
     */
	@Override
	void diePkg()
	{
	    super.diePkg();
	    
	    //remove this from the siblings list of the siblings
	    
	    for (Prey sib : siblings) {
        	sib.siblings.remove(this);
        	}
	    
	    /*int range = siblings.size();
	    
	    
	    ArrayList<Prey> clone = new ArrayList<Prey>(siblings);
	    for(int i=0; i< range;i++) {
	    	Prey sib = clone.get(i);
	    	sib.siblings.remove(this);
	    }*/
	    
	    
	    //remove this from the inhabitants list of the shelter
	    
	    shelter.inhabitants.remove(this);
	    if(shelter.inhabitants.isEmpty()) {
	    	shelter.die();
	    	}
	    
	    //clear the siblings and shelter fields
	    this.siblings.clear();
	    shelter = null;
	    
	}
	

	public Chromosome getChromosome()
    {
        return this.chromosome;
    }

	/**
	 * @post | result.equals(Color.GREEN)
	 */
    public Color getColor()
    {
        return Color.GREEN;
    }
    
    public Shelter getShelter()
    {
    	return this.shelter;
    }


    /**
     * @post if the turn neuron outputs a value higher that 333 the prey will turn counter clockwise, if it returns a value lower than -333 the prey will turn clockwise
     *  else it will not move
     * @post if the move forward neuron outputs a value strictly greater than zero, the prey will move forward in the direction of its orientation if possibke
     * @post if the preys distance of the shelter is bigger than Constants.SHELTER_SURVIVAL_DISTANCE, the prey's score will decrease by one else it will increase by one
     * @post | old(getWorld()).equals(getWorld())
	 * @post | getMoveProbability()==old(getMoveProbability())
     */
    @Override
    public void performActionIfAlive()
    {
        performTurn();
        performMove();
        updateScore();
    }
    
    /**
     * LEGIT
     * 
     * true iff same position and orient and chromosome and behavior type The
     * getClass method can be used to retrieve the runtime type of an object.
     * 
     * @inspects | other
     */
    public boolean isEqual(Prey other)
    {
        return (other != null) && (this.getPosition().equals(other.getPosition()))
                && (this.getOrientation().isEqual(other.getOrientation()))
                && (this.getChromosome().isEqual(other.getChromosome()));
    }

    /**
     * 
     * @post returns true when this score is strictly greater than zero and th creature is alive
     */
    public boolean survives()
    {
    	/* OLD
    	return false;
    	*/
    	
    	return score>0 && isAlivePkg();
    }

    /**
     * 
     * @post | result==getShelter().getPosition().distanceSquared(this.getPosition())
     */
    public int distanceSquaredToShelter()
    {
        return shelter.getPosition().distanceSquared(this.getPosition());
    }

    /**
     * @post | result.equals(String.format("Prey(position=%s)", this.getPosition()))
     */
    @Override
    public String toString()
    {
        return String.format("Prey(position=%s)", this.getPosition());
    }
    
    @Override
    /**
     * LEGIT
     */
    public Prey giveCopy() {
    	return new Prey(super.world, shelter, chromosome, getPosition(), getOrientation());
    }
}
