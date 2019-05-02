package tasks;

import java.util.Random;

import cell.AllCycles;
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
 *  @Class Description - Task that goes and runs through the already displayed grid and resets their state values and their colors to mimic a new grid
 */

public class ResetGridTask extends Task<Void> {
	// Initializes allCycles object thats used throughout grid
	private AllCycles allCycles;

	// CONSTRUCTOR
	public ResetGridTask(AllCycles allCycles) {
		this.allCycles = allCycles;
	}

	@Override
	protected Void call() throws Exception {
		// Initializes the random generator
		Random gen = new Random();

		// Initializes count int to hold the current count of cells replaced to update
		// the progress indicatpr
		int count = 0;
		final int N_ITERATIONS = (allCycles.getGridDemensionsX() * allCycles.getGridDemensionsY()); // total number of
																									// cells

		for (int i = 0; i < allCycles.getGridDemensionsX(); i++) {
			for (int k = 0; k < allCycles.getGridDemensionsY(); k++) {
				// Holds the random generated number
				int holdRandom = gen.nextInt(allCycles.getChance());
				// updates for the progressIndicator
				updateProgress(count++, N_ITERATIONS);

				// gets the current cell and clears it's allStates array list so it can generate
				// a new cycle list
				allCycles.getCell(i, k).clearStateList();

				// takes the random generated number and if 1 then becomes infected, therefore
				// chance was 1/allCycles.getChance
				if (holdRandom == 1) { // if holdRandom == 1
					allCycles.getCell(i, k).setColor(Color.web("7afe7c"));
					allCycles.getCell(i, k).setState(1);
					allCycles.getCell(i, k).setInfectedCell();
				} else if (holdRandom % 2 == 0) { // if holdRandom is evem
					allCycles.getCell(i, k).setColor(Color.web("00006f"));
					allCycles.getCell(i, k).setState(0);
				} else {// else if holdrandom is odd
					allCycles.getCell(i, k).setColor(Color.web("0000cc"));
					allCycles.getCell(i, k).setState(1);
				}
			}
		}
		return null;
	}
}
