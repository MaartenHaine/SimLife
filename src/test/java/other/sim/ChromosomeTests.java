package other.sim;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.Test;
import util.RandomUtil;

import sim.Chromosome;
import sim.Constants;

class ChromosomeTests {

	@Test
	void testnNullWeights()
	{
		assertThrows(IllegalArgumentException.class, () -> new Chromosome(null));
	}
	
	@Test
	void testInvalidSize()
	{
		var genes = new int[Constants.CHROM_SIZE + 1];
		Arrays.fill(genes, Constants.GENE_MIN);
		assertThrows(IllegalArgumentException.class, () -> new Chromosome(genes));
	}
		
	@Test
	void testInvalidGenes()
	{
		var genes = new int[Constants.CHROM_SIZE];
		Arrays.fill(genes, Constants.GENE_MIN-1);
		assertThrows(IllegalArgumentException.class, () -> new Chromosome(genes));
	}
	

	@Test
	void testIsEqualOnSameChromosome()
	{
		var chromosome = Chromosome.createRandom();	
		assertTrue(chromosome.isEqual(chromosome));
	}
	@Test
	void testIsEqualOnEqualChromosomes()
	{	
		//give random weights with genes interval
		int[] weights = new int[Constants.CHROM_SIZE];
		for (int i = 0; i < Constants.CHROM_SIZE; i++)
		{
			weights[i] = RandomUtil.integer(Constants.GENE_MIN, Constants.GENE_MAX + 1);
		}
		var chromosome1 = new Chromosome(weights);
		var chromosome2 = new Chromosome(weights);
			
		assertTrue(chromosome1.isEqual(chromosome2));
		assertTrue(chromosome2.isEqual(chromosome1));
	}
		
	@Test
	void testIsEqualOnChromosomesWithDifferentGenes()
	{
		int[] weights = new int[Constants.CHROM_SIZE];
		for (int i = 0; i < Constants.CHROM_SIZE; i++)
		{
			weights[i] = RandomUtil.integer(Constants.GENE_MIN, Constants.GENE_MAX + 1);
		}
		var chromosome1 = new Chromosome(weights);
		var chromosome2 = chromosome1.mutate(0, 1);
			
		assertFalse(chromosome1.isEqual(chromosome2));
		assertFalse(chromosome2.isEqual(chromosome1));
	}
		
	@Test
	void testIsEqualOnNull()
	{
		var chromosome = Chromosome.createRandom();		
		assertFalse(chromosome.isEqual(null));
	}

	@Test
	void testMatchesUntil()
	{
		var chromosome1 = Chromosome.createRandom();
		var chromosome2 = chromosome1.mutate(1, 1);
		
		assertTrue(chromosome1.matchesUntil(chromosome2, 1));
		assertFalse(chromosome1.matchesUntil(chromosome2, 2));
	}
	
	@Test
	void testMatchesFrom()
	{
		var chromosome1 = Chromosome.createRandom();
		var chromosome2 = chromosome1.mutate(0, 1);
		
		assertFalse(chromosome1.matchesFrom(chromosome2, 0));
		assertTrue(chromosome1.matchesFrom(chromosome2, 1));
	}
	
	@Test
	void testMutate()
	{
		var chromosome = Chromosome.createRandom();
		var mutated = chromosome.mutate(0, 1);
		assertNotEquals(chromosome.getGene(0), mutated.getGene(0));
	}
	
	@Test
	void testMutateMin()
	{
		var genes = new int[Constants.CHROM_SIZE];
		Arrays.fill(genes, Constants.GENE_MIN);
		var chromosome = new Chromosome(genes);
		var mutated = chromosome.mutate(0, -1);
		assertTrue(mutated.isEqual(chromosome));
	}
	
	@Test
	void testMutateMax()
	{
		var genes = new int[Constants.CHROM_SIZE];
		Arrays.fill(genes, Constants.GENE_MAX);
		var chromosome = new Chromosome(genes);
		var mutated = chromosome.mutate(0, 1);
		assertTrue(mutated.isEqual(chromosome));
	}

	@Test
	void testCrossover2() {
		ArrayList<Chromosome> c = Chromosome.createRandom(2);
		Chromosome c3 = c.get(0).crossover2(c.get(1));
		assertTrue(c3.matchesUntil(c.get(0), 6) || c3.matchesUntil(c.get(1), 6));
		assertTrue(c3.matchesFrom(c.get(0), 12) || c3.matchesFrom(c.get(1), 12));
	}

	@Test
	void testRandomlyMutate() {
		Chromosome c = Chromosome.createRandom();
		Chromosome c_new = c.randomlyMutate();
		assertFalse(c_new.isEqual(c));
	}
	
	@Test
	void testOnlyDiffersAt() {
		Chromosome c = Chromosome.createRandom();
		Chromosome c_new = c.mutate(0, 1);
		assertTrue(c.onlyDiffersAt(c_new, 0));
	}
	

}
