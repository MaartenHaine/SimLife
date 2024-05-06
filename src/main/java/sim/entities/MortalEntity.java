package sim.entities;

import util.Logic;
import util.Orientation;
import util.Point;

/**
 * A MortalEntity is an Entity that can die.
 * Upon death, the entity removes itself from the world.
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
public abstract class MortalEntity extends Entity
{
	/**
	 * @invar | Logic.implies(dead,world==null)
	 * @invar | Logic.implies(!dead,world!=null && world.getEntities().contains(this))
	 */
    private boolean dead;
    
    /**
     *  
     *  
	 * via super: (je moet deze ook hier vermelden zie modelopl it 2 
	 * @throws IllegalArgumentException | position == null
	 * @throws IllegalArgumentException | orientation == null
	 * @throws IllegalArgumentException | moveProbability < 0 || moveProbability> 100
	 * @throws IllegalArgumentException | world != null && world.getEntityAt(position)!=null
	 * @throws IllegalArgumentException | world != null && !world.entityGrid.isValidPosition(position)
	 *  
	 * @mutates_properties | this.getWorld()
	 * also mutates world.giveEntityGrid() (this is a very slow operation and thus not documentated in a mutates)
	 *  
	 * @post | world == getWorld()
	 * @post | getPosition().equals(position)
	 * @post | getOrientation().equals(orientation)
	 * @post | getMoveProbability()==moveProbability
	 * 
	 * @post | isDead() == false
	 * @post | getWorld() == null || getWorld().getEntities().contains(this)
	 * @post | getWorld() == null ||  Point.isWithin(getPosition(),getWorld().getWidth(),getWorld().getHeight())
     */
    MortalEntity(World world, Point position, Orientation orientation, int moveProbability)
    {
        super(world, position, orientation, moveProbability);
        
        this.dead = false;
    }
    
    /**
     * @post | result == !isDead()
     */
    @Override
	boolean isAlivePkg() { return !dead; }

    /**
     * @mutates_properties | this.getWorld()
     * also mutates old(getWorld()).giveEntityGrid() (this is a very slow operation and thus not documentated in a mutates)
	 *  
     * @post | isDead()
     * @post Entity is no part of world anymore| !old(getWorld()).giveEntityStream().anyMatch(ent->ent==this)
     * @post | getWorld()==null
     */
	void diePkg() {
		//OUD
	    this.dead = true;
	    //NIEUW
	    world.removeEntityAt(getPosition());
	    world = null;
	}

	/**
	 * LEGIT
	 */
	public void performAction()
    {
        if (!this.dead)
        {
            performActionIfAlive();
        }
    }
    
    /**
     * LEGIT
     */
    public void moveForward()
    {
        if (!this.dead)
        {
            super.moveForward();
        }
    }
    
    /**
     * 
     * v entity: 
     * @post | old(getWorld()).equals(getWorld())
	 * @post | getMoveProbability()==old(getMoveProbability())
     */
    public abstract void performActionIfAlive();
    
    public boolean isDead()
    {
        return dead;
    }
    
    /**
     * LEGIT
     */
    public void die()
    {
    	diePkg();
    }
    
}
