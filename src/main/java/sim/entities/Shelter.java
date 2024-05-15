package sim.entities;

import java.util.ArrayList;

import sim.Constants;
import util.Color;
import util.Logic;
import util.Orientation;
import util.Point;
import util.RandomUtil;

/**
 * @invar | getInhabitants() != null
 * @invar |  getInhabitants().stream().allMatch(prey-> prey==null || prey.getShelter().equals(this))
 *  
 *  MortalEntity invars
 * 
 * @invar | Logic.implies(isDead(),getWorld()==null)
 * @invar | Logic.implies(!isDead(),getWorld() !=null && getWorld().getEntities().contains(this))
 *  
 * ENTITY INVARS: 
 * @invar | getPosition()!=null
 * @invar | getOrientation() != null
 * @invar | 0 <=getMoveProbability() && getMoveProbability()  <= 100
 * @invar if an entity is in a world, the world contains that entity  
 * | getWorld() ==null ||  getWorld().getEntities().contains(this)
 * @invar Entity positie komt overeen met hun positie in world
 * | getWorld() == null || getWorld().getEntityAt(getPosition()).equals(this)
 * @invar | getWorld() == null ||  Point.isWithin(getPosition(),getWorld().getWidth(),getWorld().getHeight())
 * @invar| getColor()!=null
 */
public class Shelter extends MortalEntity
{
    /**
     * @invar | inhabitants != null
     * @invar |  inhabitants.stream().allMatch(prey-> prey==null || prey.shelter.equals(this))
	 * @peerObjects
	 * @representationObject
     */
    final ArrayList<Prey> inhabitants;

    /**
     * via super: (je moet deze ook hier vermelden zie modelopl it 2 
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
	 * @post | Logic.implies(old(world).entityGrid.at(position)==null, world == this.world)
	 * @post | getPosition().equals(position)
	 * @post | getOrientation().equals(orientation)
	 * @post | getMoveProbability()==Constants.SHELTER_MOVE_PROBABILITY
	 * 
	 * @post | this.world.entityGrid.at(position).equals(this)
	 * @post | Point.isWithin(getPosition(),this.world.getWidth(),this.world.getHeight())	
	 * 
	 * 
	 * @post | isDead() == false
	 * 
	 * shelterspecifiek:
     * @post | getInhabitants().equals(new ArrayList<>())
     */
    Shelter(World world, Point position, Orientation orientation)
    {
        super(world, position, orientation, Constants.SHELTER_MOVE_PROBABILITY);
        this.inhabitants = new ArrayList<>();
    }

    /**
     * @post | result == false
     */
    @Override
	boolean isPreyPkg()
	{
	    return false;
	}

    /**
     * @post | result == true
     */
	@Override
	boolean isShelterPkg()
	{
	    return true;
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
	 * @creates |result
	 */
	public ArrayList<Prey> getInhabitants()
    {
		/*OUD REPR EXPOSURE
        return inhabitants;
        */ 
		//SHALLOW COPY
		return new ArrayList<Prey>(inhabitants);
    }

	/**
	 * Shelter has Constants.SHELTER_TURN_PROBABILITY chance of turning. In 50% of the cases it turns clockwise, in the other 50% if turns counterclockwise.
  	 * Next, it has Constants.SHELTER_MOVE_PROBABILITY chance of moving one step forward.
  	 * @post | old(getWorld()).equals(getWorld())
	 * @post | getMoveProbability()==old(getMoveProbability())
	 */
    @Override
    public void performActionIfAlive()
    {
    	if(RandomUtil.unfairBool(Constants.SHELTER_TURN_PROBABILITY)) {
    		
    		if (RandomUtil.bool()) { 
    			turnClockwise();
    		} else{
    			turnCounterclockwise();
    		}
    	}
    	// the prob equals Constants.SHELTER_MOVE_PROBABILITY
    	moveForwardWithProbability();
    }

    /**
     * @post | result.equals(Color.BLACK)
     */
    @Override
    public Color getColor()
    {
    	/* OLD
        return null;
        */
    	return Color.BLACK;
    }

    /**
     * @post | result.equals(String.format("Shelter(position=%s)", this.getPosition()))
     */
    @Override
    public String toString()
    {
        return String.format("Shelter(position=%s)", this.getPosition());
    }
    
    @Override
    /**
     * LEGIT
     */
    public Shelter giveCopy() {
    	return new Shelter(super.world, getPosition(), getOrientation());
    }
}
