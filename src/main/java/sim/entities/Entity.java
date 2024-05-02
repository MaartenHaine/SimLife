package sim.entities;

import util.Color;
import util.Logic;
import util.Orientation;
import util.Point;
import util.RandomUtil;
import static util.Logic.*
;


/**
 * Supertype for all entities.
 * 
 * An entity resides in a world, has a position, an orientation and a move probability.
 * An entity has a color.
 * 
 * @invar | getPosition()!=null
 * @invar | getOrientation() != null
 * @invar | 0 <=getMoveProbability() && getMoveProbability()  <= 100
 * @invar if an entity is in a world, the world contains that entity  
 * | getWorld() ==null ||  getWorld().getEntities().contains(this)
 * @invar Entity positie komt overeen met hun positie in world
 * | getWorld() == null || getWorld().giveEntityGrid().at(this.getPosition())==this
 * @invar| getColor()!=null
 */
public abstract class Entity
{


	/**
	 * @invar Entity positie komt overeen met hun positie in world
	 * | world == null || world.entityGrid.at(position)==this	
	 * @invar | position !=null
	 */
    private Point position;
    
    /**
	 * @invar | orientation !=null
	 */
    private Orientation orientation;
    
    /**
     * @invar | 0 <= moveProbability && moveProbability <= 100
     */
    private final int moveProbability;


    /**
     *  
     * @invar if an entity is in a world, the world contains that entity 
     * | world == null || 
     * | world.entityGrid.givePositionStream().map(pos -> world.entityGrid.at(pos)).anyMatch(ent -> ent ==this)
	 * @peerObject
	 */
	World world;

	/**
	 * 
	 * World mag null zijn wt peer object
	 * @throws IllegalArgumentException | position == null
	 * @throws IllegalArgumentException | orientation == null
	 * @throws IllegalArgumentException | moveProbability < 0 || moveProbability> 100
	 * 
	 * @post | world == getWorld()
	 * @post | getPosition().equals(position)
	 * @post | getOrientation().equals(orientation)
	 * @post | getMoveProbability()==moveProbability
	 * 
	 */
	Entity(World world, Point position, Orientation orientation, int moveProbability)
    {

		if (position == null) {throw new IllegalArgumentException();}
		if (orientation  == null) {throw new IllegalArgumentException();}
		if (moveProbability < 0 || moveProbability> 100) {throw new IllegalArgumentException();}
		
		/* OUD
    	this.world = null;
    	this.position = null;
    	this.orientation = null;
    	this.moveProbability = 0;
    	*/
		//world niet clonen want geen repr object?
		this.world = world;
    	this.position = position;
    	this.orientation = orientation;
    	this.moveProbability = moveProbability;
		
    }
    
    boolean isAlivePkg() { return true; }

	abstract boolean isHunterPkg();

	abstract boolean isPreyPkg();

	abstract boolean isShelterPkg();

	/**
	 * LEGIT
	 */
	Point getPositionPkg() {
		return position;
	}

	/**
	 * MOET JE WORLD COPYEN?
     * Returns the world which this entity inhabits.
     * @peerObject
     */
    public World getWorld()
    {
    	/*OLD
    	return null;
    	*/
    	return world;
    }
    
    /**
     * Current position of this entity in the world.
     * 
     */
    public Point getPosition() {
    	/* OUD
    	return null;
    	*/
    	return position;
    }
    
    /**
     * Current orientation of this entity.
     */
    public Orientation getOrientation() {
    	/* OUD
    	return null;
    	*/
    	return orientation;
    }

    /**
     * Probability (integer between 0 and 100) that the entity moves at each step.
     */
    public int getMoveProbability() {
    	/* OUD
    	return 0;
    	*/
    	return moveProbability;
    }
    
    /**
     * Changes the orientation of the entity.
     * @pre | orientation != null
     * @mutates | getOrientation()
     * @post | getOrientation().equals(orientation)
     */
    public void setOrientation(Orientation orientation) {
    	this.orientation = orientation;
    }
    
    /**
     * @mutates | getOrientation()
     * @post | getOrientation().equals(old(getOrientation()).turnClockwise(1))
     */
	public void turnClockwise()
	{
	    this.orientation = this.orientation.turnClockwise(1);
	}

	/**
	 * @mutates | getOrientation()
     * @post | getOrientation().equals(old(getOrientation()).turnCounterclockwise(1))
     */
	public void turnCounterclockwise()
	{
	    this.orientation = this.orientation.turnCounterclockwise(1);
	}

	/**
     * Computes the position where the entity would arrive if it were to move one step
     * forward in its current orientation. This does not take into account whether
     * this position is free or not.
     * 
     * @creates nieuw punt wordt gereturned | result
     * @post | getPosition().equals(old(getPosition()).move(getOrientation().toVector()))
     */
    public Point destination()
    {
    	/*OUD
    	return null;
    	*/
    	return position.move(orientation.toVector());
    }
    
    /**
     * The current position is set (if there is room) to current pos + current orientation.
     * Note: this method is not probabilistic
     * @mutates_properties | getWorld().giveEntityGrid()
     * @post | Logic.implies(getWorld().isFree(destination()),getPosition().equals(destination()))
     * 
     * @post | old(getWorld()) == getWorld()
	 * @post | getOrientation().equals(old(getOrientation()))
	 * @post | getMoveProbability()==old(getMoveProbability())
     */
    public void moveForward()
    {
        var oldPosition = this.position;
        var newPosition = destination();

        if ( world.isFree(newPosition) )
        {
            this.position = newPosition;
            
            //DIT HIERONDER IS NIEUW, om bidirectionele associatie te bewaren
            world.removeEntityAt(oldPosition);
            world.entityGrid.setAt(newPosition,this);
        }
    }
    
    /**
     * Samples using moveProbability and attempts to move if the latter result is true
     * See RandomUtil.unfairBool
     * 
     * @post creature moves forward (one step forward in the direction of its orientation) with proba moveProbability and if destination() is free
     * 
     * @post | old(getWorld()) == getWorld()
	 * @post | getOrientation().equals(old(getOrientation()))
	 * @post | getMoveProbability()==old(getMoveProbability())
     */
    public void moveForwardWithProbability()
    {
    	//OUD: geen implementatie
    	if(RandomUtil.unfairBool(moveProbability)) {moveForward();}
    }
    /**
     * @post | old(getWorld()) == getWorld()
	 * @post | getMoveProbability()==old(getMoveProbability())
     */
    public abstract void performAction();

	/**
     * Returns the color of the entity.
     */
    public abstract Color getColor();
    
    /**
     * Checks whether this entity is a prey.
     */
    public boolean isPrey()
    {
        return isPreyPkg();
    }
    
    /**
     * Checks whether this entity is a hunter.
     */
    public boolean isHunter()
    {
        return isHunterPkg();
    }
    
    /**
     * Checks whether this entity is a shelter.
     */
    public boolean isShelter()
    {
        return isShelterPkg();
    }
    
    /**
     * Hint: only flawed methods use this method (or its children).
     * In our implementation this method is never used.
     */
    public abstract Entity giveCopy();
    
}
