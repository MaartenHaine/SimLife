package sim.entities;

import java.util.ArrayList;

import sim.Constants;
import util.Color;
import util.Logic;
import util.Orientation;
import util.Point;
import util.RandomUtil;
/**
 * 
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
     * @invar |  inhabitants.stream().allMatch(prey-> prey==null || prey.getShelter().equals(this))
	 * @peerObjects
	 * @representationObject
     */
    final ArrayList<Prey> inhabitants;

    /**
     * 
     *  
	 * via super: (je moet deze ook hier vermelden zie modelopl it 2 
	 * @throws IllegalArgumentException | position == null
	 * @throws IllegalArgumentException | orientation == null
	 * MOET DIT?  throws IllegalArgumentException | Constants.SHELTER_MOVE_PROBABILITY < 0 || Constants.SHELTER_MOVE_PROBABILITY > 100
	 * @throws IllegalArgumentException | world != null && world.getEntityAt(position)!=null
	 * @throws IllegalArgumentException | world != null && !world.entityGrid.isValidPosition(position)
	 *  
	 * @mutates_properties | this.getWorld()
	 * also mutates world.giveEntityGrid() (this is a very slow operation and thus not documentated in a mutates)
	 *   
	 * @post | world == getWorld()
	 * @post | getPosition().equals(position)
	 * @post | getOrientation().equals(orientation)
	 * @post | getMoveProbability()==Constants.SHELTER_MOVE_PROBABILITY
	 * 
	 * @post | isDead() == false
	 * @post | getWorld() == null || getWorld().getEntities().contains(this)
	 * @post | getWorld() == null ||  Point.isWithin(getPosition(),getWorld().getWidth(),getWorld().getHeight())
     * 
     * @post | getInhabitants() != null
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
     * @post | result == false
     */
	@Override
	boolean isShelterPkg()
	{
	    return false;
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
    	//prob ==Constants.SHELTER_MOVE_PROBABILITY
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
