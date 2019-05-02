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
 *  @Class Description - Is used during the setup grid method in main.java. 
 *  Goes through and adds all the cells to the grid UI. 
 *  is done through recursion by calling itself once its done displaying one time
 */

public class DisplayEntireRowTask extends Task<Void> {

	// Initializes currentRow int that holds the currentRow value
	private int currentRow;
	// Initializes the row int that holds the total number of rows values.
	private int totalRows;
	// Initializes the mainController object that holds the mainController given by
	// params
	private MainController mainController;
	// Initializes the currentTaskProgressIndicator that displays the progress of
	// added all the cells to the UI
	private ProgressIndicator currentTaskProgressIndicator;
	// Initializes the allCycles object that holds data for the sim that the task
	// uses
	private AllCycles allCycles;

	// CONSTRUCTOR
	public DisplayEntireRowTask(MainController mainController, int currentRow, AllCycles allCycles) {
		// Assigns maincontroller from the params
		this.mainController = mainController;
		// Assigns currentRow from the params
		this.currentRow = currentRow;
		// Assigns rows from the allCycles method getRowCount()
		this.totalRows = allCycles.getRowCount();
		// gets the progress indicator from maincontroller by getting control panel from
		// the maincontroller and then getting the progressindicator from the control
		// panel
		this.currentTaskProgressIndicator = mainController.getControlPanelController().getCTPI();
		// Assigns allCycles from the params
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
				// adds the cells to the UI one row at a time through the maincontroller
				mainController.displayCellRowOnStarUp(allCycles.getCellArrayRow(currentRow));
				// adds 1 to the currentRow int value
				currentRow++;
				// if currentRow is less then the totalrows count then proceed, otherwise it
				// means the entire grid is displayed
				if (currentRow < totalRows) {
					// assigns the progress to the progressindicator by dividing the currentrow by
					// totalrows
					try {
						currentTaskProgressIndicator.setProgress((double) currentRow / totalRows);
					} catch (Exception e) {
						// throws exception because if you generate a new grid while one is already
						// being generated this will throw an exception since it's no longer valid, this
						// only occurs once until the display task is finished to where then it will no
						// longer run

						// Kills the current thread because we know the user started to generate a new
						// grid at the same time as this grid was being generated because the
						// progressindicator threw a task therefore were just going to close this thread
						// because its no longer needed
						Thread.currentThread().interrupt();
					}
					// creates a new thread of DisplayEntireRowTask to display the next row
					DisplayEntireRowTask task = new DisplayEntireRowTask(mainController, currentRow, allCycles);
					new Thread(task).start();
				} else { // if all rows are displayed then stopp and set the progress to 100%
					currentTaskProgressIndicator.setProgress(100.0);
				}
			}
		});
		return null;
	}

}
