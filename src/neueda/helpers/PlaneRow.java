package neueda.helpers;

import java.util.List;

/**
 * @author Kevin Niland
 * @category Helpers
 * @version 1.0
 * 
 *          PlaneRow.java - Getter and setter class for rows on the plane
 */
public class PlaneRow {
	private List<Seat> seatList;
	private StringBuilder stringBuilder = new StringBuilder();
	private int rowIndex;
	private int availableSeats;
	private int remainingSeats;
	private int totalSeats;

	/**
	 * Constructor 
	 * 
	 * @param seatList - List of seats on the plane
	 * @param rowIndex - Index of a row
	 * @param totalSeats - Total number of seats
	 * @param availableSeats - Available seats on the plane
	 */
	public PlaneRow(List<Seat> seatList, int rowIndex, int totalSeats, int availableSeats) {
		this.seatList = seatList;
		this.rowIndex = rowIndex;
		this.remainingSeats = totalSeats;
		this.totalSeats = totalSeats;
		this.availableSeats = availableSeats;
	}

	// Get seat list
	public List<Seat> getSeatList() {
		return seatList;
	}

	// Set seat list
	public void setSeatList(List<Seat> seatList) {
		this.seatList = seatList;
	}

	// Get row index
	public int getRowIndex() {
		return rowIndex;
	}

	// Set row index
	public void setRowIndex(int rowIndex) {
		this.rowIndex = rowIndex;
	}

	// Get remaining seats left on the plane
	public int getRemainingSeats() {
		return remainingSeats;
	}

	// Set remaining seats left on the plane
	public void setRemainingSeats(int remainingSeats) {
		this.remainingSeats = remainingSeats;
	}

	// Get total seats
	public int getTotalSeats() {
		return totalSeats;
	}

	// Set total seats
	public void setTotalSeats(int totalSeats) {
		this.totalSeats = totalSeats;
	}

	// Get available seats on the plane
	public int getAvailableSeats() {
		return availableSeats;
	}

	// Set available seats on the plane
	public void setAvailableSeats(int availableSeats) {
		this.availableSeats = availableSeats;
	}

	// 
	@Override
	public String toString() {
		for (Seat seat : seatList) {
			stringBuilder.append(seat.getTraveller() != null ? seat.getTraveller().getTravellerNum() : 0).append(" ");
		}

		return stringBuilder.toString();
	}
}
