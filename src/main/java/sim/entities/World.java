package sim.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import sim.Chromosome;
import sim.Constants;
import util.Grid;
import util.Logic;
import util.Orientation;
import util.Point;


/**
 * @invar alle bestaande entities in de grid zitten in de wereld 
 * | getEntities().stream().allMatch(ent-> ent.getWorld()==this)
 * @invar Entity posities komen overeen met hun posities in world
 * | getEntities().stream().allMatch(ent-> getEntityAt(ent.getPosition())==ent)
 * @invar | getHunters() != null
 * @invar the shelter that hunter is focused on is in World
 * @invar Every hunter is in the entityGrid 
 * | getHunters().stream().allMatch(h ->  getEntities().contains( h))
 */
public class World
{

	/**
	 * LEGIT
	 */
	private Stream<Entity> giveEntityStreamPriv()
	{
		return this.entityGrid.givePositionStream().map(pos -> this.entityGrid.at(pos))
				.filter(entity -> entity != null).toList().stream();
	}

	/**
	 * 
	 * @invar alle bestaande entities in de grid zitten in de wereld
	 * | entityGrid.givePositionStream().map(pos -> entityGrid.at(pos)).allMatch(ent-> ent==null ||ent.getWorld()==this)
	 * @invar Entity posities komen overeen met hun posities in world
	 * | entityGrid.givePositionStream().allMatch(pos -> entityGrid.at(pos)==null ||entityGrid.at(pos).getPosition().equals(pos) )
	 * @representationObject
	 * @peerObjects
	 */
	final Grid<Entity> entityGrid;
	
	/**
	 * @invar | hunters != null
	 * @invar | hunters.stream().allMatch(hunt -> hunt ==null || this.entityGrid.givePositionStream().map(pos -> this.entityGrid.at(pos)).anyMatch(ent-> ent ==null || ent.equals(hunt.shelter)))
	 * @invar Every hunter is in the entityGrid | hunters.stream().allMatch(h -> entityGrid.givePositionStream().map(pos -> entityGrid.at(pos)).anyMatch(e-> e==null || e.equals(h)))
	 * @invar | hunters.stream().allMatch(h->h.world ==this)
	 * @representationObject
	 * @peerObjects
	 */
	final List<Hunter> hunters;

	/**
	 * LEGIT
	 * just a helper method.
	 */
	void removeEntityAt(Point position)
	{
		this.entityGrid.setAt(position, null);
	}

	/**
	 * Creates a new world with zero entities in it.
	 * @throws IllegalArgumentException
	 *   | width <= 0 || height <= 0
	 *   
	 * @post | getWidth() == width
	 * @post giveEntityGrid().getWidth() == width (this takes long to execute thus made informally to be ignored by compiler)
	 * 
	 * @post | getHeight() == height
	 * @post giveEntityGrid().getHeight() == height (this takes long to execute thus made informally to be ignored by compiler)
	 * @post | getHunters().isEmpty()
	 * @post | getEntities().isEmpty()
	 * @post | numberOfEntities() == 0
	 */
	public World(int width, int height)
	{
		/* OLD
		this.entityGrid = null;
		this.hunters = null;
		*/
		// The grid checks the width and height
		this.entityGrid = new Grid<Entity>(width,height);
		
		this.hunters = new ArrayList<Hunter>();
		
	}

	/**
	 * 
	 * @post | result>0
	 */
	public int getWidth()
	{
		//OLD return 0;
		return entityGrid.getWidth();
	}

	/**
	 * 
	 * @post | result>0
	 */
	public int getHeight()
	{
		//OLD return 0;
		return entityGrid.getHeight();
	}

	/**
	 * Returns all entities inhabiting the world.
	 * 
	 * @creates | result
	 * @post| result!=null
	 * @post | result.stream().allMatch(ent1-> getEntityAt(ent1.getPosition()).equals(ent1))
	 * @post | result.stream().allMatch(ent1-> ent1!=null)
	 */
	public List<Entity> getEntities()
	{
		ArrayList<Entity> res = new ArrayList<Entity>();
		res.addAll(
		   // FLAWED? giveEntityStreamPriv().map(ent -> ent.giveCopy()).toList()
	        giveEntityStreamPriv().toList()
		   );
	    return res;
	}

	/**
	 * LEGIT
	 */
	public ArrayList<Prey> getPreys()
	{
	
		return new ArrayList<> (giveEntityStreamPriv().filter(e -> e.isPrey()).map(e -> (Prey) e).toList());
	}
	
	/**
	 * LEGIT
	 */
	public ArrayList<Hunter> getHunters() {
		return new ArrayList<>(hunters);
	}

	/**
	 * Number of entities currently in the world
	 * @post | result == giveEntityStream().count()
	 */
	public long numberOfEntities()
	{
		//OLD return 0;
		return giveEntityStreamPriv().count();
	}


	/**
	 * @pre | position != null
	 * @post | result == Point.isWithin(position,getWidth(),getHeight())
	 * this is very slow: giveEntityGrid().isValidPosition(position)
	 */
	public boolean isInside(Point position)
	{
		return this.entityGrid.isValidPosition(position);
	}

	/**
	 * LEGIT
	 * 
	 * Returns true iff pos is 1 (simulation) pixel away from a wall (and inside the
	 * world)
	 *
	 */
	public boolean isLimPos(Point pos)
	{
		return isInside(pos) && (0 == pos.getX() || pos.getX() == getWidth() - 1 || pos.getY() == 0
				|| pos.getY() == getHeight() - 1);
	}

	/**
	 * @pre | position!=null
	 * @pre | isInside(position)
	 * 
	 * @post | result==null  || result.getPosition().equals(position)
	 */
	public Entity getEntityAt(Point position)
	{
		// old
		//return null;
		return this.entityGrid.at(position);
	}

	/**
	 * true iff position is inside the world and no creature sits there
	 *
	 * @pre | position != null
	 * @post | result == (this.isInside(position) && getEntityAt(position)==null)
	 */
	public boolean isFree(Point position)
	{	// old
		//return true;
		return this.isInside(position) && getEntityAt(position)==null;
	}

	/**
	 * LEGIT
	 */
	public void step()
	{
		this.giveEntityStreamPriv().forEach(e -> e.performAction());
	}

	
	/**
	 * LEGIT
	 * 
	 */
	public Prey createPrey(Shelter shelter, Chromosome chromosome, Point position, Orientation orientation)
	{
		var prey = new Prey(this, shelter, chromosome, position, orientation);
		this.entityGrid.setAt(position, prey);
		return prey;
	}



	/**
	 * @throws IllegalArgumentException | shelter == null || shelter.isDead()
	 *  via super: (je moet deze ook hier vermelden zie modelopl it 2 
	 *
	 * @throws IllegalArgumentException | position == null
	 * @throws IllegalArgumentException | orientation == null
	 * @throws IllegalArgumentException | !this.giveEntityGrid().isValidPosition(position)
	 * @throws IllegalArgumentException | this.getEntityAt(position)!=null
	 *
	 *  mutates_properties | this.giveEntityStream(), this.getHunters()
	 * This is also mutated but not included because its slow:  this.giveEntityGrid()
	 *
	 * @creates | result
	 * 
	 * @post | result.getPosition().equals(position)
	 * @post | result.getPosition().equals(position)
	 * @post | result.getOrientation().equals(orientation)
	 * @post | result.getMoveProbability()==Constants.HUNTER_MOVE_PROBABILITY
	 * @post | result.getWorld().giveEntityGrid().at(position).equals(result)
	 * @post | Point.isWithin(result.getPosition(),this.getWidth(),this.getHeight())
	 * * @post The hunter will have Constants.HUNTER_INITIAL_APPETITE appetite
	 */
	public Hunter createHunter(Shelter shelter, Point position, Orientation orientation)
	{
		/*OLD
		var hunter = new Hunter(this, shelter, position, orientation);
		this.entityGrid.setAt(position, hunter);
		return hunter;
		*/
		var hunter = new Hunter(this, shelter, position, orientation);
		this.entityGrid.setAt(position, hunter);
		hunters.add(hunter); //THIS WAS FLAWED IN OLD IMPL
		return hunter;
	}


	

    /**
     * via super: (je moet deze ook hier vermelden zie modelopl it 2 
	 *
	 * @throws IllegalArgumentException | position == null
	 * @throws IllegalArgumentException | orientation == null
	 * @throws IllegalArgumentException | !this.giveEntityGrid().isValidPosition(position)
	 * @throws IllegalArgumentException | this.getEntityAt(position)!=null
	 *
	 * @creates | result
	 * also mutates world.giveEntityGrid() (this is a very slow operation and thus not documentated in a mutates)
	 *   
	 * @post | Logic.implies(old(this).giveEntityGrid().at(position)==null, this == result.getWorld())
	 * @post | result.getPosition().equals(position)
	 * @post | result.getOrientation().equals(orientation)
	 * @post | result.getMoveProbability()==Constants.SHELTER_MOVE_PROBABILITY
	 * @post | result.getWorld().giveEntityGrid().at(position).equals(result)
	 * @post | Point.isWithin(result.getPosition(),getWidth(),getHeight())	
	 * 
	 * 
	 * @post | result.isDead() == false
	 * 
	 * shelterspecifiek:
     * @post | result.getInhabitants().equals(new ArrayList<>())
     */
	public Shelter createShelter(Point position, Orientation orientation)
	{
		//OLD
		//return null;
		Shelter shelter = new Shelter(this,position,orientation);
		this.entityGrid.setAt(position, shelter);
		
		return shelter;
	}

	/**
	 * LEGIT
	 */
	public boolean hasHunterInCone(Point top, Orientation orientation)
	{
		for ( var hunter : this.hunters )
		{
			var hunterPosition = hunter.getPosition();
			
			if ( hunterPosition.equals(top))
			{
				return true;
			}
			
			if ( orientation.isEqual(top.vectorTo(hunterPosition).toClosestOrientation()) )
			{
			    return true;
			}
		}
		
		return false;
	}
	
	/**
	 * LEGIT
	 */
	public Stream<Point> givePositionStream() {
		return entityGrid.givePositionStream();
	}
	
	/**
	 * LEGIT
	 */
	public Stream<Entity> giveEntityStream() {
		return giveEntityStreamPriv();
	}
	
	/**
	 * @creates | result 
	 * 
	 * @post | result.getWidth() == this.getWidth()
	 * @post | result.getHeight() == this.getHeight()
	 * @post | result != null
	 * @post the result equals the grid that the world uses 
	 * @post all entities are also brought into the grid| getEntities().stream().allMatch(ent-> result.at(ent.getPosition())==ent)
	 */
	public Grid<Entity> giveEntityGrid() {
		// OLD
		//return entityGrid;
		
		//Shallow copy
		return entityGrid.giveCopy();
	}
}
