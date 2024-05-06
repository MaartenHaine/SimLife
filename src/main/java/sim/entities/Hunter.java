package sim.entities;

import java.util.ArrayList;

import sim.Constants;
import util.Color;
import util.Logic;
import util.Orientation;
import util.Point;
import util.RandomUtil;
/**
 * An instance of this class represents a Hunter entity thats chases preys and can eat them.
 * Hunters are shown as red pixels
 * 
 * @invar | getColor().equals(Color.RED)
 * 
 * ENTITY INVARS
 * @invar | getPosition()!=null
 * @invar | getOrientation() != null
 * @invar | 0 <=getMoveProbability() && getMoveProbability()  <= 100
 * @invar if an entity is in a world, the world contains that entity  
 * | getWorld() ==null ||  getWorld().getEntities().contains(this)
 * @invar Entity positie komt overeen met hun positie in world
 * | getWorld() == null || getWorld().getEntityAt(getPosition()).equals(this)
 * @invar | getWorld() == null || Point.isWithin(getPosition(),getWorld().getWidth(),getWorld().getHeight())
 * @invar| getColor()!=null
 * 
 */
public class Hunter extends Entity
{
	/**
	 * @invar | appetite>=0
	 */
	private int appetite;

	//moet ik dit documenteren?
	private Prey findClosestPrey(ArrayList<Prey> preys)
	{
		Prey closestPrey = null;
		int closestDistanceSquared = Integer.MAX_VALUE;
	
		for (var prey : preys)
		{
			var distanceSquared = this.getPosition().distanceSquared(prey.getPosition());
	
			if (distanceSquared < closestDistanceSquared)
			{
				closestPrey = prey;
				closestDistanceSquared = distanceSquared;
			}
		}
	
		return closestPrey;
	}

	/**
	 * @invar | shelter != null
	 * SHELTER KAN NIET NAAR HUNTER WIJZEN?
	 * @invar | shelter.world.hunters.contains(this)
	 * @peerObject
	 */
	final Shelter shelter;
	
	
	
	/**
	 * @throws IllegalArgumentException | shelter == null
	 * 
	 *  via super: (je moet deze ook hier vermelden zie modelopl it 2 
	 *
	 * @throws IllegalArgumentException | world==null
	 * @throws IllegalArgumentException | position == null
	 * @throws IllegalArgumentException | orientation == null
	 * @throws IllegalArgumentException | !world.entityGrid.isValidPosition(position)
	 *
	 * @mutates_properties | this.getWorld(), world.giveEntityGrid()
	 *
	 * @post | Logic.implies(old(world).entityGrid.at(position)==null, world == this.world)
	 * @post | getPosition().equals(position)
	 * @post | getOrientation().equals(orientation)
	 * @post | getMoveProbability()==Constants.HUNTER_MOVE_PROBABILITY
	 * @post | this.world.entityGrid.at(position).equals(this)
	 * @post | Point.isWithin(getPosition(),this.world.getWidth(),this.world.getHeight())
	* 
	 * @post The hunter will have Constants.HUNTER_INITIAL_APPETITE appetite
	 * 
	 */
	Hunter(World world, Shelter shelter, Point position, Orientation orientation)
	{
		
		this(world, shelter, position, orientation, Constants.HUNTER_INITIAL_APPETITE);
	}

	/**
	 * @throws IllegalArgumentException | shelter == null
	 * @throws IllegalArgumentException | appetite <= 0
	 * 	
	 *  via super: (je moet deze ook hier vermelden zie modelopl it 2 
	 *
	 * @throws IllegalArgumentException | world==null
	 * @throws IllegalArgumentException | position == null
	 * @throws IllegalArgumentException | orientation == null
	 * @throws IllegalArgumentException | !world.entityGrid.isValidPosition(position)
	 *
	 * @mutates_properties | this.getWorld(), world.giveEntityGrid()
	 *
	 * @post | Logic.implies(old(world).entityGrid.at(position)==null, world == this.world)
	 * @post | getPosition().equals(position)
	 * @post | getOrientation().equals(orientation)
	 * @post | getMoveProbability()==Constants.HUNTER_MOVE_PROBABILITY
	 * @post | this.world.entityGrid.at(position).equals(this)
	 * post Point.isWithin(getPosition(),this.world.getWidth(),this.world.getHeight())
	* 
	 * @post The hunter will have an appetite as is given as argument
	 */
	Hunter(World world, Shelter shelter, Point position, Orientation orientation, int appetite)
	{
		super(world, position, orientation, Constants.HUNTER_MOVE_PROBABILITY);
		
		//---- NIEUW ----
		if(shelter==null) {throw new IllegalArgumentException();}
		if(appetite <= 0) {throw new IllegalArgumentException();}
		//----       ----
		this.shelter = shelter;
		this.appetite = appetite;
	}

	/**
	 * @post | result ==false
	 */
	@Override
	boolean isPreyPkg()
	{
		return false;
	}
	/**
	 * @post | result ==true
	 */
	@Override
	boolean isHunterPkg()
	{
		return true;
	}
	/**
	 * @post | result ==false
	 */
	@Override
	boolean isShelterPkg()
	{
		return false;
	}

	@Override
	public Color getColor()
	{
		return Color.RED;
	}
	/**
	 * @post | result.equals(String.format("Hunter(position=%s)", this.getPosition()))
	 */
	@Override
	public String toString()
	{
		return String.format("Hunter(position=%s)", this.getPosition());
	}
    /**
     * DIT MOET NOG UITGEBREID WORDEN 
     * 
     * @post | old(getWorld()) == getWorld()
	 * @post | getMoveProbability()==old(getMoveProbability())
     */
	@Override
	public void performAction()
	{
		//NO IMPL IN OLD version thus no flaws. ER STOND WEL DIT
		// //to face the found prey
		// var targetDirection = this.getPosition().vectorTo(target.getPosition()); 
		// var newOrientation = Orientation.fromVector(targetDirection);
		// this.setOrientation(newOrientation);
		
		//  //if one is 100% sure that thing is a Prey one may use subcasting
		//  var prey = (Prey) thing;
		
		
		//OPDRACHT: 
		
		/*
  First, it determines whether it will actually move or not.
    At each step, it has a chance of Constants.HUNTER_MOVE_PROBABILITY to move.
  If the hunter decides to move, it will look for the prey closest to it.
    Only preys inhabiting the shelter the hunter is targetting are considered here: other preys might as well not exist.
  Once the closest prey is found, it will turn so as to face it.
    The hunter can turn an arbitrary amount in a single step: it doesn’t have to turn in increments of 45 degrees.
  The hunter then prepares to move forward.
    
      If moving forward lets the hunter reaches a prey of the correct shelter, this prey gets eaten.
        The hunter then moves to the position that was previously occupied by the prey.
        With “reaching the prey” we mean that the hunter ends up on exactly the same location as the prey.
      If the hunter bumps into a prey of the wrong shelter, nothing happens: the prey is left alone, and the hunter does not move forward.
        The same is true if the hunter runs into another hunter, or into a shelter.
      If the positon in front of the hunter is free, then the hunter simply moves forward.
    
If no more preys inhabiting the right shelter remain, the hunter will remain immobile for the rest of its life.*/
		//System.out.print("App: ");
		//System.out.println(appetite);
		
		//System.out.print("Shelter: ");
		//System.out.println(shelter.toString());
		
		
		if(RandomUtil.unfairBool(getMoveProbability()) && appetite >0) {
			
			Prey target = findClosestPrey(shelter.getInhabitants());

			//System.out.println(shelter.getInhabitants());
			if(target!=null) { // thus no preys, the hunter will stay immobile
				//to face the found prey
				var targetDirection = this.getPosition().vectorTo(target.getPosition()); 
				var newOrientation = Orientation.fromVector(targetDirection);
				this.setOrientation(newOrientation);
				
				if (destination().equals(target.getPosition())) {
					target.die(); // prey verdwijnt instantly uit de world grid dus move forward is mogelijk						
					appetite = appetite-1;
				}
				moveForward(); // wanneer niet free zal hunter niet bewegen
			}
		}


	}
	
	@Override
	/**
	 * LEGIT
	 */
	public Hunter giveCopy() {
		return new Hunter(super.world, shelter, getPosition(), getOrientation(), this.appetite);
	}
}
