package other;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import sim.Chromosome;
import sim.Constants;
import sim.Simulation;
import sim.entities.Entity;
import sim.entities.Hunter;
import sim.entities.MortalEntity;
import sim.entities.Prey;
import sim.entities.Shelter;
import sim.entities.World;
import sim.neuralnet.ActivationFunctionNeuron;
import sim.neuralnet.BinarySensorNeuron;
import sim.neuralnet.FreePassageSensorNeuron;
import sim.neuralnet.HunterSensor;
import sim.neuralnet.LinearFunctionNeuron;
import sim.neuralnet.NeuralNetwork;
import sim.neuralnet.Neuron;
import sim.neuralnet.SensorNeuron;
import sim.neuralnet.ShelterSensor;
import util.Chronometer;
import util.Color;
import util.Grid;
import util.Orientation;
import util.Pair;
import util.Point;
import util.RandomUtil;
import util.Vector;

class SubmissionTestSuite {
	@Test
	public void checkNeuronHierarchy()
	{
		{
			ActivationFunctionNeuron afn = new LinearFunctionNeuron();
			Neuron n = afn;
		}
		{
			BinarySensorNeuron bsn = new FreePassageSensorNeuron(Orientation.north());
			SensorNeuron sn = bsn;
			Neuron n = sn;
		}
		{
			BinarySensorNeuron bsn = new HunterSensor();
		}
		{
			BinarySensorNeuron bsn = new ShelterSensor();
		}
	}
	
	public Prey createPrey()
	{
		World world = new World(Constants.WORLD_SIZE, Constants.WORLD_SIZE);
		var shelter = world.createShelter(new Point(0, 0), Orientation.createRandom());
		var chromosome = Chromosome.createRandom();
		var prey = world.createPrey(shelter, chromosome, new Point(1, 0), Orientation.createRandom());
		
		return prey;
	}
	
	@Test
	public void checkNeuron()
	{
		var prey = createPrey();
		
		HunterSensor hs = new HunterSensor();
		int x = hs.computeOutput(prey);
	}
	
	@Test
	public void checkActivationFunctionNeuron()
	{
		ActivationFunctionNeuron afn = new LinearFunctionNeuron();
		HunterSensor hs = new HunterSensor();
		
		{
			ArrayList<Pair<Neuron, Integer>> ps = afn.getDependencies();
		}
		{
			afn.setDependencies(afn.getDependencies());
		}
		{
			afn.connect(hs, 1);
		}
		{
			afn.getBias();
			afn.setBias(0);
		}
		{
			afn.applyActivationFunction(0);
		}
	}
	
	@Test
	public void checkBinarySensorNeuron()
	{
		BinarySensorNeuron bsn = new FreePassageSensorNeuron(Orientation.north());
		var prey = createPrey();
		bsn.detect(prey);
	}
	
	@Test
	public void checkLinearFunctionNeuron()
	{
		var lfn = new LinearFunctionNeuron();
		var h = new HunterSensor();
		
		lfn.connect(h, 1);
	}
	
	@Test
	public void neuralNetwork()
	{
		var nn = new NeuralNetwork();
		nn = NeuralNetwork.fromChromosome(Chromosome.createRandom());
		
		SensorNeuron[] inputs = nn.getInputNeurons();
		ActivationFunctionNeuron[] outputs = nn.getOutputNeurons();
		ActivationFunctionNeuron output = nn.getMoveForwardNeuron();
		output = nn.getTurnNeuron();
	}

	@Test
	public void checkWorld()
	{
		{
			World world = new World(50, 40);
		}
		World world = new World(20, 20);
		
		{
			int w = world.getWidth();
		}
		{
			int h = world.getHeight();
		}
		{
			List<Entity> es = world.getEntities();
		}
		{
			ArrayList<Prey> ps = world.getPreys();
		}
		{
			long n = world.numberOfEntities();
		}
		{
			boolean b = world.isInside(new Point(0, 0));
		}
		{
			boolean b = world.isLimPos(new Point(0, 0));
		}
		{
			Entity e = world.getEntityAt(new Point(0, 0));
		}
		{
			world.step();
		}
		{
			Shelter shelter = world.createShelter(new Point(0, 0), Orientation.createRandom());
			var chromosome = Chromosome.createRandom();
			Prey prey = world.createPrey(shelter, chromosome, new Point(1, 0), Orientation.createRandom());
			Hunter hunter = world.createHunter(shelter, new Point(2, 0), Orientation.createRandom());
		}
		{
			world.hasHunterInCone(new Point(0, 0), Orientation.createRandom());
		}
		{
			Stream<Point> s = world.givePositionStream();
		}
		{
			Stream<Entity> s = world.giveEntityStream();
		}
		{
			Grid<Entity> g = world.giveEntityGrid();
		}
	}
	
	@Test
	public void checkEntityHierarchy()
	{
		World world = new World(Constants.WORLD_SIZE, Constants.WORLD_SIZE);
		var chromosome = Chromosome.createRandom();
		Shelter shelter = world.createShelter(new Point(0, 0), Orientation.createRandom());
		Prey prey = world.createPrey(shelter, chromosome, new Point(1, 0), Orientation.createRandom());
		Hunter hunter = world.createHunter(shelter, new Point(2, 0), Orientation.createRandom());
		
		{
			MortalEntity me = shelter;
			Entity e = me;
		}
		{
			Entity e = prey;
		}
		{
			Entity e = hunter;
		}
	}
	
	@Test
	public void checkEntity()
	{
		World world = new World(Constants.WORLD_SIZE, Constants.WORLD_SIZE);
		Shelter shelter = world.createShelter(new Point(0, 0), Orientation.createRandom());
		
		Entity e = shelter;
		
		{
			World w = e.getWorld();
		}
		{
			Point p = e.getPosition();
		}
		{
			Orientation o = e.getOrientation();
		}
		{
			int p = e.getMoveProbability();
		}
		{
			e.setOrientation(e.getOrientation());
		}
		{
			e.turnClockwise();
		}
		{
			e.turnCounterclockwise();
		}
		{
			Point p = e.destination();
		}
		{
			e.moveForward();
		}
		{
			e.moveForwardWithProbability();
		}
		{
			e.performAction();
		}
		{
			Color c = e.getColor();
		}
		{
			boolean b = e.isPrey();
		}
		{
			boolean b = e.isHunter();
		}
		{
			boolean b = e.isShelter();
		}
	}
	
	@Test
	public void checkMortalEntity()
	{
		World world = new World(Constants.WORLD_SIZE, Constants.WORLD_SIZE);
		Shelter shelter = world.createShelter(new Point(0, 0), Orientation.createRandom());
		MortalEntity me = shelter;
		
		{
			me.performActionIfAlive();
		}
		{
			boolean b = me.isDead();
		}
		{
			me.die();
		}
	}
	
	@Test
	public void checkPrey()
	{
		World world = new World(Constants.WORLD_SIZE, Constants.WORLD_SIZE);
		var chromosome = Chromosome.createRandom();
		Shelter shelter = world.createShelter(new Point(0, 0), Orientation.createRandom());
		Prey prey = world.createPrey(shelter, chromosome, new Point(1, 0), Orientation.createRandom());
		
		{
			Chromosome c = prey.getChromosome(); 
		}
		{
			Shelter s = prey.getShelter();
		}
		{
			boolean b = prey.survives();
		}
		{
			int d = prey.distanceSquaredToShelter();
		}
	}
	
	@Test
	public void checkShelter()
	{
		World world = new World(Constants.WORLD_SIZE, Constants.WORLD_SIZE);
		Shelter shelter = world.createShelter(new Point(0, 0), Orientation.createRandom());
		
		{
			ArrayList<Prey> ps = shelter.getInhabitants();
		}
	}
	
	@Test
	public void checkSimulation()
	{
		Simulation simulation = new Simulation(40, 5, 10, 2);
		
		{
			World w = simulation.getWorld();
		}
		{
			simulation.nextGeneration();
		}
	}
	
	@Test
	public void checkGrid()
	{
		Grid<String> grid = new Grid<String>(10, 20);
		
		{
			int w = grid.getWidth();
		}
		{
			int h = grid.getHeight();
		}
		{
			String s = grid.at(new Point(0, 0));
		}
		{
			grid.setAt(new Point(0, 0), "abc");
			grid.setAt(new Point(0, 0), null);
		}
		{
			grid.isValidPosition(new Point(111, 999));
		}
		{
			Stream<Point> stream = grid.givePositionStream();
		}
		{
			Grid<String> copy = grid.giveCopy();
		}
	}
	
	@Test
	public void checkChronometer()
	{
		Chronometer c = new Chronometer();
		long x = c.elapsedNanoseconds();
	}
	
	@Test
	public void checkColor()
	{
		Color c = new Color(0, 0, 0);
		{
			int x = c.asInteger();
		}
		{
			boolean b = Color.isValidColorComponent(0);
		}
	}

	@Test
	public void checkPair()
	{
		Pair<String, Integer> pair = new Pair<String, Integer>("a", 5);
		Pair<Integer, String> pair2 = new Pair<>(8, "aaa");
		
		{
			String s = pair.getFirst();
		}
		{
			Integer x = pair.getSecond();
		}
		{
			String s = "aa";
			pair.setFirst(s);
		}
		{
			Integer x = 1;
			pair.setSecond(x);
		}
	}

	@Test
	public void checkPoint()
	{
		Point point = Point.createRandom(100, 100);
		
		{
			Point p = new Point(1, 1);
		}
		{
			int x = point.getX();
		}
		{
			int y = point.getY();
		}
		{
			Point p = point.move(new Vector(0, 0));
		}
		{
			int x = point.distanceSquared(point);
		}
		{
			boolean b = Point.isWithin(point, 10, 10);
		}
		{
			Vector v = point.subtract(point);
		}
		{
			Vector v = point.vectorTo(point);
		}
	}
	
	@Test
	public void checkVector()
	{
		Vector vector = new Vector(1, 2);
		
		{
			int x = vector.getX();
		}
		{
			int y = vector.getY();
		}
		{
			Vector v = vector.scaleWith(1);
		}
		{
			Vector v = vector.plus(vector);
		}
		{
			Orientation o = vector.toClosestOrientation();
		}
	}
	
	@Test
	public void checkOrientation()
	{
		Orientation o = new Orientation(0);
		o = Orientation.north();
		o = Orientation.northEast();
		o = Orientation.east();
		o = Orientation.southEast();
		o = Orientation.south();
		o = Orientation.southWest();
		o = Orientation.west();
		o = Orientation.northWest();
		o = Orientation.createRandom();
		
		{
			Orientation p = o.turnClockwise(0);
		}
		{
			Orientation p = o.turnCounterclockwise(0);
		}
		{
			Orientation p = o.compose(o);
		}
		{
			List<Orientation> os = Orientation.orientations();
		}
		{
			Orientation p = Orientation.fromIndex(0);
		}
		{
			Orientation p = Orientation.fromVector(new Vector(1, 0));
		}
		{
			Vector v = o.toVector();
		}
	}
	
	@Test
	void checkChromosome() {
		int[] weights = new int[Constants.CHROM_SIZE];
		Arrays.fill(weights, Constants.GENE_MIN);
		Chromosome c1 = new Chromosome(weights);
		Chromosome c2 = Chromosome.createRandom();
		{
			ArrayList<Chromosome> cs = Chromosome.createRandom(10);
		}
		{
			Chromosome c3 = c1.crossover2(c2);
		}
		{
			Chromosome c4 = c1.mutate(0, 0);
		}
		{
			Chromosome c5 = c1.randomlyMutate();
		}

		{
			int x = c1.getGene(0);
			boolean b = Chromosome.isValidGene(0);
		}
		{
			boolean b = c1.matchesFrom(c2, 0);
		}
		{
			boolean b = c1.matchesUntil(c2, 0);
		}
		{
			boolean b = c1.onlyDiffersAt(c2, 0);
		}
	}
	
	@Test
	void checkRandomUtil()
	{
		RandomUtil.seed(10);
		
		{
			int x = RandomUtil.integer();
		}
		{
			int x = RandomUtil.integer(10);
		}
		{
			int x = RandomUtil.integer(0, 10);
		}
		{
			boolean b = RandomUtil.bool();
		}
		{
			boolean b = RandomUtil.unfairBool(20);
		}
		{
			List<String> xs = new ArrayList<String>();
			xs.add("a");
			String x = RandomUtil.pick(xs);
		}
		{
			List<String> xs = new ArrayList<String>();
			xs.add("a");
			RandomUtil.shuffle(xs);
		}
	}
}
