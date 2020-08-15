package neueda.ui;

import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

import neueda.helpers.PlaneRow;
import neueda.utilities.Allocator;

/**
 * @author Kevin Niland
 * @category GUI
 * @version 1.0
 * 
 *          Menu - Simple menu from which the user can specify the file to
 *          create a seat map of
 */
public class Menu {
	private Allocator allocator;
	private String file;
	private Map<Integer, PlaneRow> seatMap;
	private String allocatedSeats;
	private String seperator = System.lineSeparator();
	private StringBuilder stringBuilder = new StringBuilder();
	private Scanner scanner = new Scanner(System.in);
	private boolean keepAlive = true;
	private int menuChoice;

	/**
	 * Menu -
	 * 
	 * @throws Exception
	 */
	public void menu() throws Exception {
		while (keepAlive) {
			System.out.println("\nNeueda Home Assignment - Plane Seat Mapper");
			System.out.println("=====================================================");
			System.out.println("Enter 1 to specify a file and create a seat map from it, or");
			System.out.println("Enter -1 to quit the program: ");
			menuChoice = scanner.nextInt();

			switch (menuChoice) {
			case 1:
				System.out.println("Enter name of file to create a seat map from it: ");
				file = scanner.next();
				allocator = new Allocator(file);

				try {
					seatMap = allocator.getSeatMap();

					if (seatMap != null) {
						allocatedSeats = seatMap.values().stream().map(s -> s.toString())
								.collect(Collectors.joining(seperator));

						stringBuilder.append(allocatedSeats).append(seperator)
								.append(allocator.calculateSatisfaction() + "%\n");
						System.out.print(stringBuilder);

						/**
						 * Set the length of stringbuilder to 0. Needed as the previous seat mappings
						 * will "accumulate" on subsequent file inputs
						 */
						stringBuilder.setLength(0);
					} else {
						System.out.print("ERROR: Unable to create a seat map");
					}
				} catch (Exception exception) {
					exception.printStackTrace();
				}
			default:
				break;
			}
		}
	}
}
