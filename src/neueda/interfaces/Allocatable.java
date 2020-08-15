package neueda.interfaces;

import java.util.Map;

import neueda.helpers.PlaneRow;

/**
 * @author Kevin Niland
 * @category Interface
 * @version 1.0
 *
 *          Allocatable.java - Interface containing methods implemented by
 *          Allocator.java
 */
public interface Allocatable {
	public abstract void parse();

	public abstract Map<Integer, PlaneRow> process(String line);

//	public abstract Map<Integer, PlaneRow> getSeatMap();
//
//	public abstract void setSeatMap(Map<Integer, PlaneRow> seatMap);

	public abstract void initialise(int numRows, int numSeatsPerRow);

	public abstract void createMapping(String[] dimensions);

	public abstract void allocateIndividuals(/* Traveller trav */);

	public abstract void allocateGroups(/* Traveller trav, */int rowIndex);

	public abstract int calculateSatisfaction();
}
