package tasks;

import cell.AllCycles;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.ProgressIndicator;
import ui.MainController;

/*
 *  @author Tomas S
 *  @author - Ryan C
 *  
 *  @Project - Final 
 *  @Professor - Dr. Micah S
 *  @Class - COMP1050
 *  
 *  @Class Description - Runs through the all the cycles and displays all the rows of the cycle one by one. 
 *  A recursion task that will display one row, finish and then run it's self again to display the next. 
 *  Once all the rows of that cycle are displayed it then goes and runs the next cycle.
 */

public class DisplayCycleTask extends Task<Void> {

	// Initializes the mainController object
	private MainController mainController;
	// Initializes the currentCycle int that holds the value of which cycle the task
	// is currently displaying
	private int currentCycle;
	// Initializes the currentRow int that holds the value of which row the task is
	// currently displaying
	private int currentRow;
	// Initializes the total row int that holds the number of rows in the grid
	private int totalRows;
	// Initializes the progressIndicator that will show the progress of how many
	// rows the cycle is currently displaying
	private ProgressIndicator currentTaskProgressIndicator;
	// Initializes the allCycles object that the task will reference to get various
	// values that relate to the sim
	private AllCycles allCycles;

	// CONSTRUCTOR
	public DisplayCycleTask(MainController mainController, int currentCycle, int currentRow, AllCycles allCycles) {
		// assigns the maincontroller to the one received in the params
		this.mainController = mainController;
		// assigns the currentcycle to the one received in the params
		this.currentCycle = currentCycle;
		// assigns the currentRow to the one received in the params
		this.currentRow = currentRow;
		// assigns the currentTaskProgressIndicator from getting the control panel
		// controller from the maincontroller and then getting the progress indicator
		// from the control panel controller
		this.currentTaskProgressIndicator = mainController.getControlPanelController().getCTPI();
		// assigns the total number of rows by getting the value from the allCycles
		// object
		this.totalRows = allCycles.getRowCount();
		// assigns the allCycles to the one received in the params
		this.allCycles = allCycles;
	}

	@Override
	protected Void call() throws Exception {
		// Everything is incased within the platform.runlater to allow for the task to
		// close the thread and the code to then be run on a outside thread the javafx
		// allows connection to. Otherwise javafx will throw an exception refusing to be
		// talked to
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				// runs method in mainController that displays the current row of the
				// currentCycle
				mainController.displayCellRowWithCycle(allCycles.getCellArrayRow(currentRow), currentRow, currentCycle);
				// assigns the currentCycle index to the allCycles int that holds it for later
				// use
				allCycles.setHoldCurrentCycleForPause(currentCycle);
				// if rows are more then count, aka not all of the rows have been displayed
				if (currentRow < totalRows) {
					// adds to 1 to the currentRow count
					currentRow++;

					// sets the progress to the progress indicator as currentrow/totalrow
					try {
						currentTaskProgressIndicator.setProgress((double) currentRow / totalRows);
					} catch (Exception e) {
						// throws exception because it's trying to set the progress but the indicator is
						// being used else where since the user regenerated the grid mid simulation,
						// therefore this thread is no longer needed too and will close below

						// Kills the current thread because the exception was thrown therefore we know
						// the user reset the grid and the old cycle thread can be closed
						Thread.currentThread().interrupt();
					}

					// if statement that checks if the currentrow is equal to the total row count
					if (currentRow == totalRows) {
						System.out.println("Cycle ended");
						// If all rows in the cycle have been displayed then starts a new
						// RunAllCyclesTask
						RunAllCyclesTask task2 = new RunAllCyclesTask(mainController, currentCycle, allCycles);
						new Thread(task2).start();
						Thread.currentThread().interrupt();
					} else {
						// starts a task of itself to display next column at current row, its actually
						// columns not row,
						// this is so that the run later stack doesnt overflow and cause lag
						DisplayCycleTask task = new DisplayCycleTask(mainController, currentCycle, currentRow,
								allCycles);
						new Thread(task).start();
						Thread.currentThread().interrupt();
					}
				}
			}
		});
		return null;
	}

}
