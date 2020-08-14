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

/**
 * @author Kevin Niland
 * @category Processor
 * @version 1.0
 * 
 *          Allocator.java - Allocates travellers to a seat based on several
 *          factors such as seat preference, group size, etc.
 *
 */
public class Allocator {
	private Traveller traveller;
	private Seat seatLeft, seatRight;
	private BufferedReader bufferedReader;
	private List<Traveller> travellers;
	private List<Traveller> travellerList = new ArrayList<>();
	private List<Traveller> noWindowPrefList = new ArrayList<>();
	private List<Seat> seatsPerRow;
	private Map<Integer, PlaneRow> seatMap = new HashMap<>();
	private boolean isWindowSeat = false;
	private String line;
	private String file;
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
	
	public Allocator(String file) {
		this.file = file;
	}
	
	public void parse() {
		try {
			bufferedReader = new BufferedReader(new FileReader(file));
			
			while ((line = bufferedReader.readLine()) != null) {
				run(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Map<Integer, PlaneRow> getSeats() {
		return seatMap;
	}

	public void setSeats(Map<Integer, PlaneRow> seats) {
		this.seatMap = seats;
	}

	/**
	 * Read in input file and allocate seats
	 * 
	 * @param file - Input file
	 * @return seatMap - Seat map containing information on which seat each
	 *         traveller has been allocated
	 */
	public Map<Integer, PlaneRow> run(String line) {
//		try {
//			bufferedReader = new BufferedReader(new FileReader(file));

//			while ((line = bufferedReader.readLine()) != null) {
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

				if (fileArray.length >= 1) {
					travellers = Arrays.stream(fileArray).map(String::trim).filter(s -> !s.isEmpty())
							.map(s -> new Traveller().createPassenger(s)).collect(Collectors.toList());

					traveller = new Traveller(travellers);

					totalPassenger = (traveller.getTravellersList() != null ? traveller.getTravellersList().size() : 0);

					for (PlaneRow seatRow : seatMap.values()) {
						if (seatRow.getRemainingSeats() >= traveller.getTotalTravellers()) {
							if ((traveller.getTravellersList().stream().filter(p -> p.isSeatPreference()).count() > 0
									? true
									: false) && seatRow.getAvailableSeats() > 0) {
								seatRowIndex = seatRow.getRowIndex();
								break;
							} else if (!(traveller.getTravellersList().stream().filter(p -> p.isSeatPreference())
									.count() > 0 ? true : false)) {
								seatRowIndex = seatRow.getRowIndex();
								break;
							}
						}
					}

					if (seatRowIndex >= 0) {
						allocateGroups(traveller, seatRowIndex);
					} else if (availableSeats >= totalPassenger) {
						allocateIndividuals(traveller);
					} else {
						System.out.println("ERROR: No available seats remaining");

						return null;
					}
				}
			} catch (IllegalArgumentException illegalArgumentException) {
				System.out.println("ERROR: Line " + line + " contains an error");
				illegalArgumentException.printStackTrace();
			}
		}

		lineNum++;
//			}

		return seatMap;
//		} catch (IOException e) {
//			System.out.println("ERROR: Unable to open file");
//		}

//		return seatMap;
	}

	/**
	 * This method initialises a seat map
	 * 
	 * @param numRows        - Number of the rows in the plane
	 * @param numSeatsPerRow - Number of seats per row
	 */
	private void initialise(int numRows, int numSeatsPerRow) {
		for (i = 0; i < numRows; i++) {
			seatsPerRow = new ArrayList<>();

			for (j = 0; j < numSeatsPerRow; j++) {
				if (j == 0 || j == numSeatsPerRow - 1) {
					isWindowSeat = true;
				}

				seatsPerRow.add(new Seat(isWindowSeat, i, j));
			}

			seatMap.put(i, new PlaneRow(seatsPerRow, i, numSeatsPerRow, 2));
		}
	}

	/**
	 * Create and initialise a mapping
	 *
	 * @param dimensions - The dimensions of the plane. The dimensions of the plane
	 *                   are determined by the first line of the input file
	 */
	private void createMapping(String[] dimensions) {
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

//		try {
//			numSeatsPerRow = Integer.parseInt(dimensions[0]);
//			numRows = Integer.parseInt(dimensions[1]);
//
//			availableSeats = numSeatsPerRow * numRows;
//
//			if (availableSeats == 0) {
//				System.out.println("ERROR: No data available for number of plane seats");
//				return;
//			}
//
//			initialise(numRows, numSeatsPerRow);
//		} catch (NumberFormatException numberFormatException) {
//			System.out.println("ERROR: Dimensions provided are not valid");
//			numberFormatException.printStackTrace();
//		}
	}

	/**
	 * Allocates individuals and any remaining passengers to a seat
	 * 
	 * @param trav
	 */
	private void allocateIndividuals(Traveller trav) {
		if (traveller.getTravellersList() != null
				&& (traveller.getTravellersList().stream().filter(p -> p.isSeatPreference()).count() > 0 ? true
						: false)) {
			for (Traveller traveller : traveller.getTravellersList()) {
				if (traveller.isSeatPreference()) {
					travellerList.add(traveller);
				} else {
					noWindowPrefList.add(traveller);
				}
			}

			travellerList.addAll(noWindowPrefList);
		} else {
			travellerList = traveller.getTravellersList();
		}

		for (PlaneRow planeRow : seatMap.values()) {
			for (Seat seat : planeRow.getSeatList()) {
				if (seat.getTraveller() == null) {
					counter++;
				}
			}
		}
	}

	/**
	 * Allocates groups to a row of seats
	 * 
	 * @param seatRowIndex - SeatRow index which have capacity for the passengers
	 *                     from passenger group
	 * @param trav         - The group of passengers
	 */
	private void allocateGroups(Traveller trav, int rowIndex) {
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

		for (Traveller traveller : groupList) {
			if (traveller.isSeatPreference()) {
				if (seatLeft.getTraveller() == null) {
					seatList.get(0).setTraveller(traveller);

					emptyCounter++;
					remainingCounter--;
					counter++;

					leftCounter = seatMap.get(rowIndex).getAvailableSeats();
					seatMap.get(rowIndex).setAvailableSeats((leftCounter--));

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
	 * @return
	 */
	public int calculateSatisfaction() {
		List<List<Seat>> result = seatMap.values().stream().filter(e -> e.getSeatList().size() != 0)
				.map(e -> e.getSeatList()).collect(Collectors.toList());
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
