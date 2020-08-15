package neueda.test;

import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import neueda.helpers.PlaneRow;
import neueda.utilities.Allocator;

/**
 * @author Kevin Niland
 * @category Testing
 * @version 1.0
 * 
 *          RunnerTest.java
 *
 */
public class RunnerTest {
	private static Map<Integer, PlaneRow> seatMap;
	private String input = "input.txt";
	private String testInput1 = "testInput1.txt";
	private String testInput2 = "testInput2.txt";
	private Allocator allocator;

	/**
	 * Test the satisfaction level of all travellers is 100%
	 */
	@Test
	public void Calculate_Satisfaction_100() {
		allocator = new Allocator(input);
		seatMap = allocator.getSeatMap();

		// Assert that the seat map is not null
		Assert.assertNotNull(seatMap);

		/**
		 * Assert the calculated traveller satisfaction equals 100. The file used in
		 * this test case is the provided file (from the problem sheet) and as such, the
		 * traveller satisfaction should be 100%
		 */
		Assert.assertEquals(100, allocator.calculateSatisfaction());
	}

	/**
	 * Test the satisfaction level of all travellers is, at the very least, not 100%
	 */
	@Test
	public void Calculate_Satisfaction_0() {
		Allocator allocatorTest1 = new Allocator(testInput1);
		Allocator allocatorTest2 = new Allocator(testInput2);
		
		seatMap = allocatorTest1.getSeatMap();
		seatMap = allocatorTest2.getSeatMap();

		// Assert that the seat map is not null
		Assert.assertNotNull(seatMap);

		Assert.assertNotEquals(100, allocatorTest1.calculateSatisfaction());
		Assert.assertNotEquals(100, allocatorTest2.calculateSatisfaction());
	}
}
