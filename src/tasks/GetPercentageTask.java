package tasks;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import cell.AllCycles;
import javafx.concurrent.Task;

/*
 *  @author Tomas S
 *  @author - Ryan C
 *  
 *  @Project - Final 
 *  @Professor - Dr. Micah S
 *  @Class - COMP1050
 *  
 *  @Class Description - Gets the percentage of recovered cells in the grid and returns the percentage
 */

public class GetPercentageTask extends Task<Double> {

	// Initializes the allCycles object that uses it to get the needed values
	private AllCycles allCycles;

	// CONSTRUCTOR
	public GetPercentageTask(AllCycles allCycles) {
		// Assigns the allCycles object from params
		this.allCycles = allCycles;
	}

	@Override
	protected Double call() throws Exception {
		// Creates format object that will be used to format the decimals up to 4 places
		NumberFormat formatter = new DecimalFormat("#0.0000");
		// Initializes count vairables
		int count = 0;
		// for loops through entire grid via x, y dimensions
		for (int i = 0; i < allCycles.getGridDemensionsX(); i++) {
			for (int k = 0; k < allCycles.getGridDemensionsY(); k++) {
				// if the cell's state is equal to 10 then it adds to the count
				if (allCycles.getCell(i, k).getState() == 10) {
					count++;
				}
			}
		}

		// gets percentage and turns it into double then coverts it into a string using
		// decimalformatter and then back into double to remove decimal points
		System.out.println(Double.parseDouble(
				formatter.format((double) count / (allCycles.getGridDemensionsX() * allCycles.getGridDemensionsY())))
				* 100 + "%");
		// returns value
		return Double.parseDouble(
				formatter.format((double) count / (allCycles.getGridDemensionsX() * allCycles.getGridDemensionsY())));
	}

}
