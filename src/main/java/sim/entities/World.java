package sim.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import sim.Chromosome;
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
	 * 
	 */
	public World(int width, int height)
	{
		this.entityGrid = null;
		this.hunters = null;
	}

	public int getWidth()
	{
		return 0;
	}


	public int getHeight()
	{
		return 0;
	}

	/**
	 * Returns all entities inhabiting the world.
	 * 
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
	 * 
	 */
	public long numberOfEntities()
	{
		return 0;
	}


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

	
	public Entity getEntityAt(Point position)
	{
		return null;
	}

	/**
	 * true iff position is inside the world and no creature sits there
	 *
	 * @pre | position != null
	 */
	public boolean isFree(Point position)
	{
		return true;
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


	public Hunter createHunter(Shelter shelter, Point position, Orientation orientation)
	{
		var hunter = new Hunter(this, shelter, position, orientation);
		this.entityGrid.setAt(position, hunter);
		return hunter;
	}


	public Shelter createShelter(Point position, Orientation orientation)
	{
		return null;
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
	
	public Grid<Entity> giveEntityGrid() {
		return entityGrid;
	}
}
