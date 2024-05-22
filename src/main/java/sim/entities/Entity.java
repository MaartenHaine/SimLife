package sim.entities;

import util.Color;
import util.Logic;
import util.Orientation;
import util.Point;
import util.RandomUtil;

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
 * | getWorld() == null || getWorld().getEntityAt(getPosition()).equals(this)
 * @invar | getWorld() == null || Point.isWithin(getPosition(),getWorld().getWidth(),getWorld().getHeight())
 * @invar| getColor()!=null
 */
public abstract class Entity
{


	/**
	 * @invar Entity positie komt overeen met hun positie in world
	 * | world == null || world.entityGrid.at(position)==this	
	 * @invar | position !=null
	 * @invar | world == null || world.entityGrid.isValidPosition(position)
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
	 * World mag null zijn wt peer object maar bij constructie moet world gedef zijn
	 * @throws IllegalArgumentException | world==null
	 * @throws IllegalArgumentException | position == null
	 * @throws IllegalArgumentException | orientation == null
	 * @throws IllegalArgumentException | moveProbability < 0 || moveProbability> 100
	 * @throws IllegalArgumentException | !world.entityGrid.isValidPosition(position)
	 * @throws IllegalArgumentException | world.entityGrid.at(position)!=null
	 *
	 * @mutates adds entity to the world | world
	 * 
	 * @post | getPositionPkg().equals(position)
	 * 
	 * SEE ISSUE IN FORUM
	 * Dit niet want is public en dit is pack-priv constr: getOrientation().equals(orientation)
	 * Dit niet want is public en dit is pack-priv constr:  getMoveProbability()==moveProbability
	 * @post | this.world.entityGrid.at(position).equals(this)
	 * @post | this.world==world
	 * @post | Point.isWithin(getPositionPkg(),this.world.entityGrid.getWidth(),this.world.entityGrid.getWidth())
	 */
	Entity(World world, Point position, Orientation orientation, int moveProbability)
    {
		if ( world ==null){throw new IllegalArgumentException();}
		if ( position == null ) {throw new IllegalArgumentException();}
		if ( orientation  == null) {throw new IllegalArgumentException();}
		if ( moveProbability < 0 || moveProbability> 100) {throw new IllegalArgumentException();}
		if ( !world.entityGrid.isValidPosition(position)) {throw new IllegalArgumentException();}
		if ( world.entityGrid.at(position)!=null) {throw new IllegalArgumentException();}

		/* OUD
    	this.world = null;  
    	this.position = null;
    	this.orientation = null;
    	this.moveProbability = 0;
    	*/
		
		//world niet clonen want geen repr object?
		
		/* MOET NIET, GIVE COPY WORDT NOOIT GEBRUIKT
		if ( world.entityGrid.at(position)!=null) {
			this.world = new World(world.getWidth(),world.getHeight());
		}else{
			this.world = world;
			}
		*/
		this.world = world;
		world.entityGrid.setAt(position,this);// This way invariants hold
		// DIT WORDT GEDAAN DOOR CREATE CREATURE ENZ this.world.entityGrid.setAt(position,this);
		
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
     * @mutates_properties | getOrientation()
     * @post | getOrientation().equals(orientation)
     * @post | old(getWorld()) == getWorld()
	 * @post | old(getPosition()).equals(getPosition())
	 * @post | old(getMoveProbability()) == getMoveProbability()
     */
    public void setOrientation(Orientation orientation) {
    	this.orientation = orientation;
    }
    
    /**
     * @mutates_properties | getOrientation()
     * @post | getOrientation().equals(old(getOrientation()).turnClockwise(1))
     * @post | old(getWorld()) == getWorld()
	 * @post | old(getPosition()).equals(getPosition())
	 * @post | old(getMoveProbability()) == getMoveProbability()
     */
	public void turnClockwise()
	{
	    this.orientation = this.orientation.turnClockwise(1);
	}

	/**
	 * @mutates_properties | getOrientation()
     * @post | getOrientation().equals(old(getOrientation()).turnCounterclockwise(1))
     * @post | old(getWorld()) == getWorld()
	 * @post | old(getPosition()).equals(getPosition())
	 * @post | old(getMoveProbability()) == getMoveProbability()
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
     * @post | result.equals(getPosition().move(getOrientation().toVector()))
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
     * 
     * made to ignore by compiler because this is a slow operation
     * mutates_properties  getWorld().giveEntityGrid()
     * 
     * 
     * @mutates_properties | this.getWorld().givePositionStream(), this.getPosition()
  	 * also  this.getWorld().giveEntityGrid() but this is slow to call
     * @post | old(getWorld()) == getWorld()
	 * @post | getOrientation().equals(old(getOrientation()))
	 * @post | getMoveProbability()==old(getMoveProbability())
	 * 
	 * @post | Logic.implies(getWorld().isFree(old(destination())),getPosition().equals(old(destination())))
     * 
	 * @post | Logic.implies(old(getWorld().isFree(old(destination()))),getWorld().getEntityAt(old(getPosition())) == null)
	 * @post | Logic.implies(!old(getWorld().isFree(old(destination()))),old(getPosition()).equals(getPosition()))
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
     * @mutates_properties | this.getWorld().givePositionStream(), this.getPosition()
  	 * also  this.getWorld().giveEntityGrid() but this is slow
     * 
     * @post creature moves forward (one step forward in the direction of its orientation) with proba moveProbability and if destination() is free
     * then  Logic.implies(getWorld().isFree(old(destination())),getPosition().equals(old(destination())))
     * and Logic.implies(old(getWorld().isFree(old(destination()))),getWorld().getEntityAt(old(getPosition())) == null)
	 * and  Logic.implies(!old(getWorld().isFree(old(destination()))),old(getPosition()).equals(getPosition()))
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
     * @mutates | this
     * @post | old(getWorld()).equals(getWorld())
	 * @post | getMoveProbability()==old(getMoveProbability())
     */
    public abstract void performAction();

	/**
     * Returns the color of the entity.
     * @post| Logic.implies(isPrey(),result==(Color.GREEN))
     * @post| Logic.implies(isHunter(),result==(Color.RED))
     * @post| Logic.implies(isShelter(),result==(Color.BLACK))
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
     * 
     * @post | result.getPosition().equals(getPosition())
     * @post | result.getOrientation().equals(getOrientation())
     * @post | result.getMoveProbability()==getMoveProbability()
     */
    public abstract Entity giveCopy();
    
}
