package tasks;

import java.util.Random;

import cell.AllCycles;
import cell.Cell;
import javafx.concurrent.Task;
import javafx.scene.paint.Color;

/*
 *  @author Tomas S
 *  @author - Ryan C
 *  
 *  @Project - Final 
 *  @Professor - Dr. Micah S
 *  @Class - COMP1050
 *  
 *  @Class Description - Task that sets up the grid by generating all the cells for the cellArray 
 */

public class SetUpGridTask extends Task<Cell[][]> {
	// Initializes the allCycles object
	private AllCycles allCycles;

	// CONSTRUCTOR
	public SetUpGridTask(AllCycles allCycles) {
		this.allCycles = allCycles;
	}

	@Override
	protected Cell[][] call() throws Exception {
		// Initializes that cellArray that holds all the cells in the x, y grid
		Cell[][] cellArray = new Cell[allCycles.getGridDemensionsX()][allCycles.getGridDemensionsY()];

		// Initializes the random generator that will run probability
		Random gen = new Random();

		// Initializes the count int variable that holds the number of cells that have
		// been generated for the progress indicator
		int count = 0;

		// Initializes the total count variable that holds the total number of cells in
		// the grid x, y for the progress Indicator
		final int N_ITERATIONS = (allCycles.getGridDemensionsX() * allCycles.getGridDemensionsY());

		// Runs through every possible cell in the grid using the x, y variables
		for (int i = 0; i < allCycles.getGridDemensionsX(); i++) {
			for (int k = 0; k < allCycles.getGridDemensionsY(); k++) {

				// Initializes the cell object
				Cell cell;

				// Initializes holdRandom int which holds a random int between 1-and the chance
				// that the user inputted
				int holdRandom = gen.nextInt(allCycles.getChance());

				// Updates the progress indicator
				updateProgress(count++, N_ITERATIONS);

				// if the holdRandom is 1 then it becomes infected giving an infected cell a
				// chance of 1/USERINPUTEDCHANCE
				if (holdRandom == 1) {
					// Creates a new cell object
					cell = new Cell(i, k, Color.web("7afe7c"), 5, allCycles.getCellDemensions(),
							allCycles.getCellDemensions());
				} else if (holdRandom % 2 == 0) { // else if the holdrandom is even then the state is 0, this is simply
													// done to allow for different colors in the grid for visual affects
					// Creates a new cell object
					cell = new Cell(i, k, Color.web("00006f"), 0, allCycles.getCellDemensions(),
							allCycles.getCellDemensions());
				} else {// else if the holdrandom is even then the state is 1, this is simply
					// done to allow for different colors in the grid for visual affects
					// Creates a new cell object
					cell = new Cell(i, k, Color.web("0000cc"), 1, allCycles.getCellDemensions(),
							allCycles.getCellDemensions());
				}
				// assigns the cell object to the x, y position on the grid in the cellArray
				cellArray[i][k] = cell;
			}
		}

		// Assigns the total number of cells by multiplying x, y and then prints it
		allCycles.setTotalNumberOfCells((allCycles.getGridDemensionsX() * allCycles.getGridDemensionsY()));
		System.out.println("TOTAL: " + (allCycles.getGridDemensionsX() * allCycles.getGridDemensionsY()));

		// returns the cellArray back to main.java
		return cellArray;
	}

}
