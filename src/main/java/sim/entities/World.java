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
import static util.Logic.*
;

/**
 * @invar alle bestaande entities in de grid zitten in de wereld 
 * | giveEntityGrid().givePositionStream().map(pos -> giveEntityGrid().at(pos)).allMatch(ent-> ent==null ||ent.getWorld()==this)
 * @invar Entity posities komen overeen met hun posities in world
 * | giveEntityGrid().givePositionStream().allMatch(pos -> giveEntityGrid().at(pos)==null ||giveEntityGrid().at(pos).getPosition().equals(pos))
 * @invar | getHunters() != null
 * @invar the shelter that hunter is focused on is in World
 * @invar Every hunter is in the entityGrid 
 * | getHunters().stream().allMatch(h ->  giveEntityGrid().givePositionStream().map(pos ->  giveEntityGrid().at(pos)).anyMatch(e-> e.equals(h)))
 * 
 */
public class World
{

	/**
	 * LEGIT
	 */
	private Stream<Entity> giveEntityStreamPriv()
	{
		return this.entityGrid.givePositionStream().map(pos -> this.entityGrid.at(pos))
				.filter(entity -> entity != null);
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
	 * @invar | hunters.stream().allMatch(hunt -> hunt ==null || this.entityGrid.givePositionStream().map(pos -> this.entityGrid.at(pos)).anyMatch(ent-> ent.equals(hunt.shelter)))
	 * @invar Every hunter is in the entityGrid | hunters.stream().allMatch(h -> entityGrid.givePositionStream().map(pos -> entityGrid.at(pos)).anyMatch(e-> e.equals(h)))
	 * 
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
	 * @post | giveEntityGrid().getWidth() == width && getWidth() == width
	 * @post | giveEntityGrid().getHeight() == height && getHeight() == height
	 * @post | givePositionStream().allMatch(p -> giveEntityGrid().at(p) == null)
	 */
	public World(int width, int height)
	{
		/* OLD
		this.entityGrid = null;
		this.hunters = null;
		*/
		this.entityGrid = new Grid<Entity>(width,height);
		this.hunters = new ArrayList<Hunter>();
		
	}

	public int getWidth()
	{
		//OLD return 0;
		return entityGrid.getWidth();
	}


	public int getHeight()
	{
		//OLD return 0;
		return entityGrid.getWidth();
	}

	/**
	 * Returns all entities inhabiting the world.
	 * 
	 * !this copies every entity
	 * @creates | result
	 * @creates | result,... result
	 * @post| result!=null
	 * @post | result.stream().allMatch(ent1-> giveEntityStream().anyMatch(ent2->ent2.equals(ent1)))
	 */
	public List<Entity> getEntities()
	{
		ArrayList<Entity> res = new ArrayList<Entity>();
		res.addAll(
		   giveEntityStreamPriv().map(ent -> ent.giveCopy()).toList()
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
	 * @post | result == giveEntityGrid().isValidPosition(position)
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
	 * @pre | this.giveEntityGrid().isValidPosition(position)
	 * 
	 * @post | result.equals(this.giveEntityGrid().at(position))
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
	 * @post | result == this.isInside(position) && getEntityAt(position)==null
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
	 * @pre| shelter != null
	 * 
	 * @pre| position != null
	 * @pre| orientation != null
	 * @pre positie is niet vrij | this.getEntityAt(position)!=null
	 * @pre | ! this.giveEntityGrid().isValidPosition(position)
	 *  ??MOET DIT ERBIJ: throws IllegalArgumentException | Constants.HUNTER_MOVE_PROBABILITY < 0 || Constants.HUNTER_MOVE_PROBABILITY> 100
	 *
	 * @creates | result
	 * @mutates_properties | this.giveEntityStream(), this.getHunters(), this.giveEntityGrid()
	 *
	 * @post | result.getWorld() == this
	 * @post | result.getPosition().equals(position)
	 * @post | result.getOrientation().equals(orientation)
	 * @post | result.getMoveProbability()==Constants.HUNTER_MOVE_PROBABILITY
	 * @post | result.getWorld().getEntities().contains(result)
	 * @post | result.getWorld().giveEntityGrid().isValidPosition(result.getPosition())
	 * 
	 * @post The hunter will have Constants.HUNTER_INITIAL_APPETITE appetite
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
	 * @pre | position != null
	 * @pre | orientation != null
	 * MOET DIT?  throws IllegalArgumentException | Constants.SHELTER_MOVE_PROBABILITY < 0 || Constants.SHELTER_MOVE_PROBABILITY > 100
	 * @pre |  this.getEntityAt(position)!=null
	 * @pre |  !this.giveEntityGrid().isValidPosition(position)
	 * 
	 * @creates | result
	 * @mutates_properties | this.giveEntityStream(), this.giveEntityGrid()
	 *
	 * @post | result.getInhabitants() != null
     * @post | this == result.getWorld()
	 * @post | result.getPosition().equals(position)
	 * @post | result.getOrientation().equals(orientation)
	 * @post | result.getMoveProbability()== Constants.SHELTER_MOVE_PROBABILITY
	 * @post | result.isDead() == false
	 * @post | result.getWorld().getEntities().contains(result)
	 * @post | result.getWorld().giveEntityGrid().isValidPosition(result.getPosition())
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
			
			if ( orientation == top.vectorTo(hunterPosition).toClosestOrientation() )
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
	 * @post | result.givePositionStream().equals(givePositionStream())
	 * @post | result.givePositionStream().map(pos -> result.at(pos)).filter(entity -> entity != null).equals(giveEntityStream())
	 */
	public Grid<Entity> giveEntityGrid() {
		// OLD
		//return entityGrid;
		
		//Shallow copy
		return entityGrid.giveCopy();
	}
}
