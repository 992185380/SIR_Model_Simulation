package tasks;

import java.util.Random;

import cell.AllCycles;
import cell.Cell;
import javafx.concurrent.Task;

/*
 *  @author Tomas S
 *  @author - Ryan C
 *  
 *  @Project - Final 
 *  @Professor - Dr. Micah S
 *  @Class - COMP1050
 *  
 *  @Class Description - Runs through the grid and performs the cycle evolution by 
 *  looking at the cells states and calculating a probability that they will become more 
 *  infected a probability which increases as they become more infected until they become 
 *  recovered which then they are longer infectious. Also susceptible are not infectious 
 *  can be infected through the probability. Recovered are not infectious and cannot be infected
 */

public class ReturnNewGridTask extends Task<Cell[][]> {
	// Initializes cellArray that holds all the cells in the grid
	private Cell[][] cellArray;
	// Initializes chanceToBecomeInfected which a random gen gets a number between
	// 1-10 which from there if an infected cell's state is greater then the number
	// generated then the cell next to it becomes infected.
	private int chanceToBecomeInfected = 10;
	// Initializes allCycles object
	private AllCycles allCycles;

	// CONSTRUCTOR that takes in allCycles object
	public ReturnNewGridTask(AllCycles allCycles) {
		this.cellArray = allCycles.getCellArray();
		this.allCycles = allCycles;
	}

	@Override
	protected Cell[][] call() throws Exception {
		// Initializes the ints that hold infected and recovered count
		int infected = 0;
		int recovered = 0;
		// Creates a clone of the cellArray so that the for loops can run through
		// cellArray and make changes to the cellArrayClone without the changes
		// affecting the cycle process
		Cell[][] cellArrayClone = new Cell[allCycles.getGridDemensionsX()][allCycles.getGridDemensionsY()];

		// runs through and creates a CellArrayClone by duplicating the object by
		// grabbing the original data and constructing new cells
		for (int i = 0; i < allCycles.getGridDemensionsX(); i++) {
			for (int k = 0; k < allCycles.getGridDemensionsY(); k++) {
				cellArrayClone[i][k] = new Cell(cellArray[i][k].getX(), cellArray[i][k].getY(),
						cellArray[i][k].getColor(), cellArray[i][k].getState(), allCycles.getCellDemensions(),
						allCycles.getCellDemensions());
			}
		}
		// runs through and finds a 1, if 1 then changes all values surrounding it (8
		// values) to 1
		// generates new grid for the cycle
		// 0 - 0 - 0
		// 0 - 1 - 0
		// 0 - 0 - 0
		for (int i = 0; i < allCycles.getGridDemensionsX(); i++) {
			for (int k = 0; k < allCycles.getGridDemensionsY(); k++) {
				Random gen = new Random();
				// 1 is not infected. 2-3 is infected but not contagious and
				// simply grows. 4-9 is infected and spreads setting the sells next to it to 2.
				// or if they are already infected it does nothing
				// 10 is recovered and cannot infected

				// counts number of recovered values
				if (cellArray[i][k].getState() == 10) {
					recovered++;
				}

				// check if the state is 2-3 if so then runs a probability chance of it becoming
				// more infected
				if (cellArray[i][k].getState() >= 2 && cellArray[i][k].getState() <= 3) {
					infected++;
					if (gen.nextInt(chanceToBecomeInfected) < cellArray[i][k].getState()) {
						cellArrayClone[i][k].updateState();
					}

				} // check if the state is 4-9 and if so then it will run a probability to see if
					// it will become more infected
				else if (cellArray[i][k].getState() >= 4 && cellArray[i][k].getState() <= 9) {
					infected++; // adds to the infected count
					if (cellArray[i][k].getState() == 9) { // if state is 9 then runs a probability to update the state,
															// this is here to change the probability to a different
															// value rather then 10 like the rest of the cell grid
						if (gen.nextInt(10) < cellArray[i][k].getState()) {
							cellArrayClone[i][k].updateState(); // updateState() method adds one value to the current
																// state
						}
					} else if (gen.nextInt(chanceToBecomeInfected) < cellArray[i][k].getState()) { // runs probability
																									// to see if the
																									// cell becomes more
																									// infected
						cellArrayClone[i][k].updateState();
					}

					// code below is in a try catch because it's trying to get items from an array
					// that might not exist in the array
					// EX:
					// 0 - 0 - # Hashes represent the edge of the border, the code still
					// 0 - 1 - # tries to reference those items and as a result will throw
					// 0 - 0 - # an out of bounds exception, which are then caught and
					// ignored.
					try {
						// holds the state of the current cell being examined
						int holdCellState = cellArray[i][k].getState();

						// runs a probability where if the number generated is below or equal to the
						// current state of the cell then the cell in that position will +1 state
						if (gen.nextInt(chanceToBecomeInfected) <= holdCellState) {
							cellArrayClone[i - 1][k - 1].updateStateToBecomeInfected();
							// * is current cell being looked at
							// 0 - 0 - 0
							// 0 - 1 - 0
							// * - 0 - 0
						}

						// runs a probability where if the number generated is below or equal to the
						// current state of the cell then the cell in that position will +1 state
						if (gen.nextInt(chanceToBecomeInfected) <= holdCellState) {
							cellArrayClone[i][k - 1].updateStateToBecomeInfected();
							// * is current cell being looked at
							// 0 - 0 - 0
							// 0 - 1 - 0
							// 0 - * - 0
						}

						// runs a probability where if the number generated is below or equal to the
						// current state of the cell then the cell in that position will +1 state
						if (gen.nextInt(chanceToBecomeInfected) <= holdCellState) {
							cellArrayClone[i + 1][k - 1].updateStateToBecomeInfected();
							// * is current cell being looked at
							// 0 - 0 - 0
							// 0 - 1 - 0
							// 0 - 0 - *
						}

						// runs a probability where if the number generated is below or equal to the
						// current state of the cell then the cell in that position will +1 state
						if (gen.nextInt(chanceToBecomeInfected) <= holdCellState) {
							cellArrayClone[i - 1][k].updateStateToBecomeInfected();
							// * is current cell being looked at
							// 0 - 0 - 0
							// * - 1 - 0
							// 0 - 0 - 0
						}

						// runs a probability where if the number generated is below or equal to the
						// current state of the cell then the cell in that position will +1 state
						if (gen.nextInt(chanceToBecomeInfected) <= holdCellState) {
							cellArrayClone[i + 1][k].updateStateToBecomeInfected();
							// * is current cell being looked at
							// 0 - 0 - 0
							// 0 - 1 - *
							// 0 - 0 - 0
						}

						// runs a probability where if the number generated is below or equal to the
						// current state of the cell then the cell in that position will +1 state
						if (gen.nextInt(chanceToBecomeInfected) <= holdCellState) {
							cellArrayClone[i - 1][k + 1].updateStateToBecomeInfected();
							// * is current cell being looked at
							// * - 0 - 0
							// 0 - 1 - 0
							// 0 - 0 - 0
						}

						// runs a probability where if the number generated is below or equal to the
						// current state of the cell then the cell in that position will +1 state
						if (gen.nextInt(chanceToBecomeInfected) <= holdCellState) {
							cellArrayClone[i][k + 1].updateStateToBecomeInfected();
							// * is current cell being looked at
							// 0 - * - 0
							// 0 - 1 - 0
							// 0 - 0 - 0
						}

						// runs a probability where if the number generated is below or equal to the
						// current state of the cell then the cell in that position will +1 state
						if (gen.nextInt(chanceToBecomeInfected) <= holdCellState) {
							cellArrayClone[i + 1][k + 1].updateStateToBecomeInfected();
							// * is current cell being looked at
							// 0 - 0 - *
							// 0 - 1 - 0
							// 0 - 0 - 0
						}

					} catch (Exception e) {
						// e.printStackTrace();
					}
				}
			}
		}

		// For loop that clones the states back over to the original cellArray
		for (int i = 0; i < allCycles.getGridDemensionsX(); i++) {
			for (int k = 0; k < allCycles.getGridDemensionsY(); k++) {
				cellArray[i][k].setStateList(cellArrayClone[i][k].getState());
			}
		}

		// Adds the recovered count to the stats list of recovered count
		allCycles.addToRecoveredCellCountList(recovered);
		// Adds the infected count to the stats list of infected count
		allCycles.addToInfectedCellCountList(infected);
		// Adds the susceptible count to the stats list of susceptible count by
		// subtracting recovered and infected from total
		allCycles.addToSusceptibleCellCountList(
				(allCycles.getGridDemensionsX() * allCycles.getGridDemensionsY()) - (recovered + infected));
		// prints out the total of each into console
		allCycles.printCount();

		// returns the cellArray back to where it's used
		return cellArray;
	}
}
