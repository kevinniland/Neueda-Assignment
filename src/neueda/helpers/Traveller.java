package neueda.helpers;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Kevin Niland
 * @category Helpers
 * @version 1.0
 *
 *          Traveller.java - Getter and setter class for a traveller
 */
public class Traveller {
	private static Matcher matcher;
	private List<Traveller> travellersList;
	private boolean seatPreference;
	private int travellerNum, seatNum;

	/**
	 * Constructor
	 * 
	 * Empty constructor used to call createPassenger() from Allocator.java
	 */
	public Traveller() {

	}

	/**
	 * Constructor
	 * 
	 * Constructor that takes in one parameter, travellersList.
	 * 
	 * @param travellersList - List of travellers
	 */
	public Traveller(List<Traveller> travellersList) {
		this.travellersList = travellersList;
	}

	/**
	 * Constructor
	 * 
	 * @param seatPreference - boolean to determine if a traveller has a seat
	 *                       preference or not
	 * @param travellerNum   - Traveller's number
	 * @param seatNum        - Seat number
	 */
	public Traveller(boolean seatPreference, int travellerNum, int seatNum) {
		this.seatPreference = seatPreference;
		this.travellerNum = travellerNum;
		this.seatNum = seatNum;
	}

	/**
	 * Create traveller from passenger string
	 * 
	 * @param traveller - Traveller string
	 * 
	 * @return a new traveller instance
	 */
	public Traveller createPassenger(String traveller) {
		matcher = Pattern.compile("([0-9]*)(W)?").matcher(traveller);

		if (!matcher.matches()) {
			throw new IllegalArgumentException("ERROR: Data format is not correct");
		}

		return new Traveller(matcher.group(2) != null, Integer.valueOf(matcher.group(1)), 0);
	}

	// Get whether or not a traveller has a seat preference
	public boolean isSeatPreference() {
		return seatPreference;
	}

	// Set whether or not a traveller has a seat preference
	public void setSeatPreference(boolean seatPreference) {
		this.seatPreference = seatPreference;
	}

	// Get traveller number
	public int getTravellerNum() {
		return travellerNum;
	}

	// Set traveller number
	public void setTravellerNum(int passengerNum) {
		this.travellerNum = passengerNum;
	}

	// Get seat number
	public int getSeatNum() {
		return seatNum;
	}

	// Set seat number
	public void setSeatNum(int seatNum) {
		this.seatNum = seatNum;
	}

	// Get the list of travellers in a group
	public List<Traveller> getTravellersList() {
		return travellersList;
	}

	/**
	 * Get total travellers
	 * 
	 * @return the size of the traveller list if the traveller list isn't null. Otherwise return 0
	 */
	public int getTotalTravellers() {
		if (travellersList != null) {
			return travellersList.size();
		} else {
			return 0;
		}
		
//		return travellersList != null ? travellersList.size() : 0;
	}
}
