package neueda.utilities;

import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import neueda.helpers.PlaneRow;
import neueda.helpers.Seat;
import neueda.helpers.Traveller;
import neueda.interfaces.Allocatable;

/**
 * @author Kevin Niland
 * @category Processor
 * @version 1.0
 * 
 *          Allocator.java - Allocates travellers to a seat based on several
 *          factors such as seat preference, group size, etc.
 * 
 *          This classes utilises Streams, as defined in Java 8
 * 
 *          Reference: https://stackify.com/streams-guide-java-8/
 *
 */
public class Allocator implements Allocatable {
	private Traveller traveller;
	private Seat seatLeft, seatRight;
	private BufferedReader bufferedReader;
	private List<Traveller> windowPrefList = new ArrayList<>();
	private List<Traveller> noWindowPrefList = new ArrayList<>();
	private List<Seat> seatsPerRow;
	private List<List<Seat>> result;
	private Map<Integer, PlaneRow> seatMap = new HashMap<>();
	private boolean isWindowSeat = false;
	private String line, file;
	private String[] fileArray;
	private int availableSeats, i, j, emptyCounter, numSeatsPerRow, numRows, remainingCounter, leftCounter,
			rightCounter;
	private int counter = 0, satisfactionResult;
	private int numOfTravellers = 0;
	private int lineNum = 1;
	private int totalPassenger = -1;
	private int seatRowIndex = -1;

	public Allocator() {

	}

	/**
	 * Constructor
	 * 
	 * @param file - Input file
	 */
	public Allocator(String file) {
		this.file = file;
	}

	/**
	 * Read in input file and pass it off to process()
	 */
	@Override
	public void parse() {
		try {
			bufferedReader = new BufferedReader(new FileReader(file));

			while ((line = bufferedReader.readLine()) != null) {
				process(line);
			}
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}

	/**
	 * Read in input file and allocate seats
	 * 
	 * @return seatMap - Seat map containing information on which seat each
	 *         traveller has been allocated
	 */
	@Override
	public Map<Integer, PlaneRow> process(String line) {
		fileArray = line.split("[\\s+]");

		/*
		 * If on the first line of the file, create an empty seat plan that is to be
		 * filled based on these values. For example, if the first line reads as "4 4",
		 * then this indicates that there is 4 seats in a row and 4 rows on the plane
		 */
		if (lineNum == 1) {
			createMapping(fileArray);
		} else {
			try {
				numOfTravellers += fileArray.length;

				// https://stackoverflow.com/a/36873265/8721358
				traveller = new Traveller(Arrays.stream(fileArray).map(String::trim).filter(s -> !s.isEmpty())
						.map(s -> new Traveller().createPassenger(s)).collect(Collectors.toList()));

				if (traveller.getTravellersList() != null) {
					totalPassenger = traveller.getTravellersList().size();
				} else {
					totalPassenger = 0;
				}

				for (PlaneRow planeRow : seatMap.values()) {
					if (planeRow.getRemainingSeats() >= traveller.getTotalTravellers()) {
						if ((traveller.getTravellersList().stream().filter(p -> p.isSeatPreference()).count() > 0 ? true
								: false) && planeRow.getAvailableSeats() > 0) {
							seatRowIndex = planeRow.getRowIndex();
							break;
						} else {
							seatRowIndex = planeRow.getRowIndex();
							break;
						}
					}
				}

				if (seatRowIndex >= 0) {
					allocateGroups(seatRowIndex);
				} else if (availableSeats >= totalPassenger) {
					allocateIndividuals();
				} else {
					System.out.println("ERROR: No available seats remaining");

					return null;
				}
			} catch (IllegalArgumentException illegalArgumentException) {
				illegalArgumentException.printStackTrace();
			}
		}

		// Increment the line number of the file
		lineNum++;

		return seatMap;
	}

	/**
	 * Call parse() and return the seat map
	 * 
	 * @return seat map
	 */
	public Map<Integer, PlaneRow> getSeatMap() {
		parse();

		return seatMap;
	}

	/**
	 * Set seat map
	 * 
	 * @param seatMap
	 */
	public void setSeatMap(Map<Integer, PlaneRow> seatMap) {
		this.seatMap = seatMap;
	}

	/**
	 * This method initialises a seat map
	 * 
	 * @param numRows        - Number of the rows in the plane
	 * @param numSeatsPerRow - Number of seats per row
	 */
	@Override
	public void initialise(int numRows, int numSeatsPerRow) {
		for (i = 0; i < numRows; i++) {
			seatsPerRow = new ArrayList<>();

			for (j = 0; j < numSeatsPerRow; j++) {
				isWindowSeat = true;
				seatsPerRow.add(new Seat(isWindowSeat, i, j));
			}

			seatMap.put(i, new PlaneRow(seatsPerRow, i, 2, numSeatsPerRow));
		}
	}

	/**
	 * Create and initialise a mapping
	 *
	 * @param dimensions - The dimensions of the plane. The dimensions of the plane
	 *                   are determined by the first line of the input file
	 */
	@Override
	public void createMapping(String[] dimensions) {
		if (dimensions.length >= 2) {
			try {
				numSeatsPerRow = Integer.parseInt(dimensions[0]);
				numRows = Integer.parseInt(dimensions[1]);

				availableSeats = numSeatsPerRow * numRows;

				if (availableSeats == 0) {
					System.out.println("ERROR: No data available for number of plane seats");
					return;
				}

				initialise(numRows, numSeatsPerRow);
			} catch (NumberFormatException numberFormatException) {
				numberFormatException.printStackTrace();
			}
		} else {
			System.out.println("ERROR: Number of rows and seats can't be defined");
		}
	}

	/**
	 * Allocates individuals and any remaining passengers to a seat
	 * 
	 * @param trav
	 */
	@Override
	public void allocateIndividuals() {
		if (traveller.getTravellersList() != null
				&& (traveller.getTravellersList().stream().filter(p -> p.isSeatPreference()).count() > 0 ? true
						: false)) {
			/**
			 * For each traveller in the traveller list, check if the traveller has a seat
			 * preference. If they do, add them to the window preference list. If not, add
			 * them to the no window preference list
			 */
			for (Traveller traveller : traveller.getTravellersList()) {
				/**
				 * If the traveller has a seat preference, add them to the traveller list. Else,
				 * add the traveller to the no preference list
				 */
				if (traveller.isSeatPreference()) {
					windowPrefList.add(traveller);
				} else {
					noWindowPrefList.add(traveller);
				}
			}

			windowPrefList.addAll(noWindowPrefList);
		} else {
			windowPrefList = traveller.getTravellersList();
		}
	}

	/**
	 * Allocates groups to a row of seats
	 * 
	 * Find a way to refactor if statement
	 * 
	 * @param seatRowIndex - SeatRow index which have capacity for the passengers
	 *                     from passenger group
	 * @param trav         - The group of passengers
	 */
	@Override
	public void allocateGroups(int rowIndex) {
		List<Traveller> groupList = new ArrayList<>();
		List<Traveller> nonPreferenceList = new ArrayList<>();

		if (traveller.getTravellersList() != null
				&& (traveller.getTravellersList().stream().filter(p -> p.isSeatPreference()).count() > 0 ? true
						: false)) {
			for (Traveller traveller : traveller.getTravellersList()) {
				if (traveller.isSeatPreference()) {
					groupList.add(traveller);
				} else {
					nonPreferenceList.add(traveller);
				}
			}

			groupList.addAll(nonPreferenceList);
		} else {
			groupList = traveller.getTravellersList();
		}

		remainingCounter = groupList.size();

		List<Seat> seatList = seatMap.get(rowIndex).getSeatList();
		emptyCounter = (int) seatList.stream().filter(seat -> seat.getTraveller() == null).findFirst()
				.map(s -> s.getSeatNum()).orElse(-1);

		seatLeft = seatList.get(0);
		seatRight = seatList.get(seatMap.get(rowIndex).getTotalSeats() - 1);

		/**
		 * For each traveller in the group list, check if the traveller has a seat
		 * preference. If the left most seat is available, assign a traveller to that
		 * seat. Else if the right most seat is available, assign a traveller to that
		 * seat
		 */
		for (Traveller traveller : groupList) {
			if (traveller.isSeatPreference()) {
				if (seatLeft.getTraveller() == null) {
					seatList.get(0).setTraveller(traveller);

					emptyCounter++;
					remainingCounter--;
					counter++;

					leftCounter = seatMap.get(rowIndex).getAvailableSeats();
					seatMap.get(rowIndex).setAvailableSeats((leftCounter--));

					// Break one iteration and continue with the next iteration
					continue;
				} else if (seatRight.getTraveller() == null) {
					seatList.get(seatMap.get(rowIndex).getTotalSeats() - 1).setTraveller(traveller);

					remainingCounter--;
					counter++;

					emptyCounter = seatMap.get(rowIndex).getTotalSeats() - remainingCounter - 1;
					rightCounter = seatMap.get(rowIndex).getAvailableSeats();
					seatMap.get(rowIndex).setAvailableSeats((rightCounter--));

					continue;
				}
			}

			seatList.get(emptyCounter++).setTraveller(traveller);
			counter++;
		}

		seatMap.get(rowIndex).setRemainingSeats(seatMap.get(rowIndex).getRemainingSeats() - groupList.size());
		seatMap.put(rowIndex, seatMap.get(rowIndex));
	}

	/**
	 * Calculate satisfaction rate of travellers - this is based on whether or not
	 * travellers with seat preferences have been correctly allocated and whether or
	 * not groups of travellers have been successfully allocated to a row of seats
	 * 
	 * @return the satisfaction result
	 */
	@Override
	public int calculateSatisfaction() {
		result = seatMap.values().stream().filter(e -> e.getSeatList().size() != 0).map(e -> e.getSeatList())
				.collect(Collectors.toList());
		result.forEach(a -> {
			a.forEach(m -> {
				if (m.getTraveller() == null)
					counter--;
			});
		});

		satisfactionResult = (counter / numOfTravellers) * 100;
		return satisfactionResult;
	}
}
