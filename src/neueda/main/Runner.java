package neueda.main;

import java.util.Map;
import java.util.stream.Collectors;

import neueda.helpers.PlaneRow;
import neueda.utilities.Allocator;

/**
 * @author Kevin Niland
 * @category Runner
 * @version 1.0
 *
 */
public class Runner {
	private static Map<Integer, PlaneRow> seatMap;
	private static final String FILEPATH = "input.txt";
	private static String allocatedSeats;
	private static StringBuilder stringBuilder = new StringBuilder();
	
	/**
	 * Entry point into application
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			seatMap = new Allocator(FILEPATH).getSeats();

			if (seatMap != null) {
				allocatedSeats = seatMap.values().stream().map(s -> s.toString())
						.collect(Collectors.joining(System.lineSeparator()));

				stringBuilder.append(allocatedSeats).append(System.lineSeparator())
						.append(new Allocator().calculateSatisfaction() + "%").toString();
				System.out.print(stringBuilder);
			} else {
				System.out.print("ERROR: Unable to create a seat map");
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
}
