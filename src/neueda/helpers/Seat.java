package neueda.helpers;

/**
 * @author Kevin Niland
 * @category Helpers
 * @version 1.0
 *
 *          Seat.java - Getter and setter class for seats on the plane
 */
public class Seat {
	private Traveller traveller;
	private boolean isWindowSeat;
	private int rowNum, seatNum;

	/**
	 * Constructor
	 * 
	 * @param isWindowSeat - Determines whether a seat is a window seat or not
	 * @param rowNo - Row number
	 * @param seatNo - Seat number
	 */
	public Seat(boolean isWindowSeat, int rowNo, int seatNo) {
		this.isWindowSeat = isWindowSeat;
		this.rowNum = rowNo;
		this.seatNum = seatNo;
	}

	// Get traveller
	public Traveller getTraveller() {
		return traveller;
	}

	// Set traveller
	public void setTraveller(Traveller traveller) {
		this.traveller = traveller;
	}

	// Get whether or not a seat is a window seat
	public boolean isWindowSeat() {
		return isWindowSeat;
	}

	// Set whether or not a seat is a window seat
	public void setWindowSeat(boolean isWindowSeat) {
		this.isWindowSeat = isWindowSeat;
	}

	// Get row number
	public int getRowNum() {
		return rowNum;
	}

	// Set row number
	public void setRowNum(int rowNum) {
		this.rowNum = rowNum;
	}

	// Get seat number
	public int getSeatNum() {
		return seatNum;
	}

	// Set seat number
	public void setSeatNum(int seatNum) {
		this.seatNum = seatNum;
	}
}
