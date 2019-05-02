package tasks;

import cell.AllCycles;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
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
 *  @Class Description - This task is the main task for the simulation. 
 *  It determines whether to go forward, backwards, or stop. 
 *  The task is called at the beginning of the sim and then runs through
 *  conditions that determine what to do.
 *  If forward or backwards then it takes the correct cycle and runs
 *  it through the display row task that then uses recursion and then runs the 
 *  next row until all of them have been displayed. 
 *  and then it calls this task again to continue the same process
 */

public class RunAllCyclesTask extends Task<Void> {

	// Initializes MainController object that is used to reference elements on the
	// UI
	private MainController mainController;
	// Initializes currentCycle int that holds the value of the currentCycle that is
	// being displayed
	private int currentCycle;
	// Initializes allCycles object that holds the cells and crucial data related to
	// the sim
	private AllCycles allCycles;
	// Initializes progressIndicator that displays the cycle progress
	private ProgressIndicator allTasksProgressIndicator;
	// Initializes pieChart that displays the count for the S.I.R. model
	private PieChart pieChartSIR;
	// Initializes linechart that displays the change in the S.I.R. model
	private LineChart<Number, Number> lineChartSIR;

	@SuppressWarnings("unchecked")
	// CONSTRUCTOR
	public RunAllCyclesTask(MainController mainController, int currentCycle, AllCycles allCycles) {
		this.mainController = mainController;
		this.currentCycle = currentCycle;
		this.allCycles = allCycles;
		// Grabs the progressindicator by having mainCOntroller return the
		// ControlPanelController and then have that controller return the
		// progressindicator
		this.allTasksProgressIndicator = mainController.getControlPanelController().getATPI();
		// Grabs the piechart by having mainController return the statspanelcontroller
		// and then have that controller return the piechart
		this.pieChartSIR = mainController.getStatsPanelController().getPieChartSIR();
		// Grabs the linechart by having mainController return the statspanelcontroller
		// and then have that controller return the linechart
		this.lineChartSIR = mainController.getStatsPanelController().getLineChartSIR();
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

				// checks if the simulation is going to be paused
				if (mainController.getPause()) {
					// if so then set whether the cycle is currently running as false
					mainController.setIsCycleCurrentlyRunning(false);
				}

				System.out.println("CurrentCYcle " + currentCycle);

				// if statement that checks boolean in the maincontroller that determines
				// whether the cycle is running the sim in the forward or backward direction
				if (mainController.isForwardCycle()) {
					// if forward then add count to the cycle making the cycle run forward
					currentCycle++;
				} else {
					// otherwise remove from the count so that the cycle goes backwards
					currentCycle--;
				}

				// Below are if statements that determine whether the program is running
				// forward, backward, being paused, ending, etc.

				// if the currentCycle count is less then the total cycle (which is given by
				// getting a random cell and checking it's arraylist count) AND if the
				// mainController boolean of pause is not true (meaning the player does not want
				// a pause AND the cycle is currently moving forward
				if (currentCycle < allCycles.getCell(0, 0).getSizeOfAllStates() && !mainController.getPause()
						&& mainController.isForwardCycle()) {
					// GOING FORWARD

					// Updates the Stats Panel
					updateStatsPanel(currentCycle);

					// progress indicator
					allTasksProgressIndicator.setProgress((double) currentCycle / allCycles.getTotalCycles());

					// sets the mainController's boolean of going forward to true so that the sim
					// knows to continue moving forward
					mainController.setForwardCycle(true);

					// prints into console the current progress of the simulation
					System.out.println("Current F Cycle: " + currentCycle + " Out Of: "
							+ allCycles.getCell(0, 0).getSizeOfAllStates());

					// Launches a new task that will start to display the rows one by one through
					// recursion. This thread then closes and will eventually launch a new thread
					DisplayCycleTask task = new DisplayCycleTask(mainController, currentCycle, 0, allCycles);
					new Thread(task).start();

					// Kills the current thread after launching the task above.
					Thread.currentThread().interrupt();

					// If the currentCycle is greater then 0 AND the sim is not running forward (aka
					// running backwards) AND the sim is not paused. Then the sim will start going
					// backwards where the currentcycle lowers by 1 every time hence why the
					// currentcycle has to be larger then 0
				} else if (currentCycle > 0 && !mainController.isForwardCycle() && !mainController.getPause()) {
					// GOING BACKWARDS

					// Updates the Stats Panel
					updateStatsPanel(currentCycle);

					// progress indicator
					allTasksProgressIndicator.setProgress(1 - ((double) currentCycle / allCycles.getTotalCycles()));

					// prints into console current progress of displaying all cycles
					System.out.println("Current B Cycle: " + currentCycle + " Out Of: "
							+ allCycles.getCell(0, 0).getSizeOfAllStates());

					// Launches a new task that will start to display the rows one by one through
					// recursion. This thread then closes and will eventually launch a new thread
					DisplayCycleTask task = new DisplayCycleTask(mainController, currentCycle, 0, allCycles);
					new Thread(task).start();

					// Kills the current thread after launching the task above.
					Thread.currentThread().interrupt();

					// IF not forward cycle and if not paused.
				} else if (!mainController.isForwardCycle() && !mainController.getPause()) {
					// BACKWARDS IS DONE

					// progress indicator set to 100% to ensure the DONE animation is run
					allTasksProgressIndicator.setProgress(100.0);

					// if the "Pause after cycle" checkbox is selected
					if (mainController.isPauseAfterCycle()) {
						// if currentCycle == -1 or -2 then start running the simulation again. This
						// will only occur when after the sim is paused due to the checkbox and then the
						// user presses the unpause button. THis is because when the sim ends the
						// currentCycle will be equal to 0 then the user presses the unpause button
						// causing the if statements at the beginning of the task to lower the value of
						// the currentCycle by 1 causing it to be -1. Also when the sim if first run the
						// currentCycle is -1 before the if statements at the beginning which then lower
						// the value by 1 again causing it to be 2 Overall this statement looks for when
						// the unpause button has been pressed after the sim has been paused due to the
						// "pause after cycle" checkbox and when the sim first starts up
						if (currentCycle == -1 || currentCycle == -2) {
							// sets the sim into pause mode
							mainController.setPause(false);
							// updates the button's text to "pause"
							mainController.getControlPanelController().getBtnRS().setText("Pause");
							// sets the pause button as active
							mainController.setPauseBtnActive(true);
							// and the unpause button as unactive
							mainController.setUnPauseBtnActive(false);

							// start going forwards
							System.out.println("Backwards ended");

							// sets the forward cycle as true
							mainController.setForwardCycle(true);

							// initalizes it's own task again to reset the sim's cycle run recurssion
							RunAllCyclesTask task = new RunAllCyclesTask(mainController, -1, allCycles);
							new Thread(task).start();

							// Kills the current thread after launching the task above.
							Thread.currentThread().interrupt();
						} else { // if the sim just ended and is being forced paused by the "pause after cycle"
									// checkbox being active
							// puts the sim into pause mode
							mainController.setPause(true);
							// sets the pause button text to unpause
							mainController.getControlPanelController().getBtnRS().setText("UnPause");
							// sets the pause button as inactive
							mainController.setPauseBtnActive(false);
							// sets the unpause button as active
							mainController.setUnPauseBtnActive(true);

							// assigns the currentCycle index to the allCycles int that holds it for later
							// use
							allCycles.setHoldCurrentCycleForPause(currentCycle);
						}

					} else { // if the "pause after cycle" checkbox is not active then continue the sim by
								// reversing the direction
						System.out.println("Backwards ended");

						// sets the direction as forward
						mainController.setForwardCycle(true);

						// launches it's own task to start the recurssion processes of displaying all
						// the tasks
						RunAllCyclesTask task = new RunAllCyclesTask(mainController, -1, allCycles);
						new Thread(task).start();

						// Kills the current thread after launching the task above.
						Thread.currentThread().interrupt();
					}

				} else if (!mainController.getPause() && mainController.isForwardCycle()) {
					// FORWARDS IS DONE

					// progress indicator set to 100% to ensure the DONE animation is run
					allTasksProgressIndicator.setProgress(100.0);

					// if the "Pause after cycle" checkbox is selected
					if (mainController.isPauseAfterCycle()) {
						// If the currentCycle value is one greter then the total number of cycles. This
						// would only happen if the cycle was paused due to the pause after cycle
						// checkbox and then the user clicked the unpause button. The value increases by
						// the if statements at the beginning of the task that determine whether to add
						// or remove value depending on the direction of the simulation.
						if (currentCycle == (allCycles.getCell(0, 0).getSizeOfAllStates() + 1)) {
							// takes sim out of pause mode
							mainController.setPause(false);
							// sets the button text to "pause"
							mainController.getControlPanelController().getBtnRS().setText("Pause");
							// sets the pause btn as being active
							mainController.setPauseBtnActive(true);
							// sets the unpause btn as being unactive
							mainController.setUnPauseBtnActive(false);

							// start going backwards
							System.out.println("Forward ended");

							// sets the direction as backwards
							mainController.setForwardCycle(false);

							// launches it's own task to start the recurssion processes of displaying all
							// the tasks
							RunAllCyclesTask task = new RunAllCyclesTask(mainController, allCycles.getTotalCycles() - 1,
									allCycles);
							new Thread(task).start();

							// Kills the current thread after launching the task above.
							Thread.currentThread().interrupt();
						} else {// if the sim just ended and is being forced paused by the "pause after cycle"
								// checkbox being active
							// puts the sim into pause mode
							mainController.setPause(true);
							// sets the pause button text to unpause
							mainController.getControlPanelController().getBtnRS().setText("UnPause");
							// sets the pause button as inactive
							mainController.setPauseBtnActive(false);
							// sets the unpause button as active
							mainController.setUnPauseBtnActive(true);

							// assigns the currentCycle index to the allCycles int that holds it for later
							// use
							allCycles.setHoldCurrentCycleForPause(currentCycle);
						}
					} else { // if the "pause after cycle" checkbox is not active then continue the sim by
								// reversing the direction
						System.out.println("Forwards ended");

						// sets the direction as forward
						mainController.setForwardCycle(false);

						// launches it's own task to start the recurssion processes of displaying all
						// the tasks
						RunAllCyclesTask task = new RunAllCyclesTask(mainController, allCycles.getTotalCycles() - 1,
								allCycles);
						new Thread(task).start();

						// Kills the current thread after launching the task above.
						Thread.currentThread().interrupt();
					}

				} else {
					// The thread closes due to various reasons, but simply means the sim is no
					// longer running
					System.out.println("DONE");

					// sets the boolean hasGridBeenReset to false to indicate that the grid hasn't
					// been cleaned
					mainController.setHasGridBeenReset(false);
					// sets the isCycleCurrentlyRunning to false to indicate that the sim is no
					// longer running
					mainController.setIsCycleCurrentlyRunning(false);
				}
			}
		});
		return null;
	}

	// this updates the values in the statspanel's piechart and linechart. Moved to
	// a method to save space and remove repeatitve code.
	public void updateStatsPanel(int currentCycle) {

		// --------
		// ( the series are in order of S.I.R.)
		// --------

		// PieChart Code

		// Initializes an ObservableList whos type is PieChart.data that holds the
		// current series of the piechart in a list
		ObservableList<PieChart.Data> pieChartData = pieChartSIR.getData();
		// The susceptible series is taken and the value is updated through the values
		// in the allCycles stored list.
		pieChartData.get(0).setPieValue(allCycles.getSusceptibleCellCountAtIndex(currentCycle));
		// The infected series is taken and the value is updated through the values in
		// the allCycles stored list.
		pieChartData.get(1).setPieValue(allCycles.getInfectedCellCountAtIndex(currentCycle));
		// The recovered series is taken and the value is updated through the values in
		// the allCycles stored list.
		pieChartData.get(2).setPieValue(allCycles.getRecoveredCellCountAtIndex(currentCycle));

		// Line Chart

		// if the cycle is moving forward
		if (mainController.isForwardCycle()) {
			// if the currentcycle is a multiple of 10, this is done to remove the number of
			// points on the graph to allow for a more legible graph
			if (currentCycle % 10 == 0) {
				// Grabs the data of the linechart and throws it into an observable list of
				// series whos axis format is number, number aka (x, y)
				ObservableList<Series<Number, Number>> series = lineChartSIR.getData();
				// grabs the first series and creates a new XYChart object which is a point in
				// the graph and then assigns it's x value to the currentcycle and the y value
				// from the allCycles list of the susceptible count
				series.get(0).getData()
						.add(new XYChart.Data<>(currentCycle, allCycles.getSusceptibleCellCountAtIndex(currentCycle)));

				// grabs the first series and creates a new XYChart object which is a point in
				// the graph and then assigns it's x value to the currentcycle and the y value
				// from the allCycles list of the infected count
				series.get(1).getData()
						.add(new XYChart.Data<>(currentCycle, allCycles.getInfectedCellCountAtIndex(currentCycle)));

				// grabs the first series and creates a new XYChart object which is a point in
				// the graph and then assigns it's x value to the currentcycle and the y value
				// from the allCycles list of the recovered count
				series.get(2).getData()
						.add(new XYChart.Data<>(currentCycle, allCycles.getRecoveredCellCountAtIndex(currentCycle)));
			}

		} else { // otherwise it's moving backwards
					// if the currentcycle is a multiple of 10, this is done to remove the number of
					// points on the graph to allow for a more legible graph
			if (currentCycle % 10 == 0) {
				// Grabs the data of the linechart and throws it into an observable list of
				// series whos axis format is number, number aka (x, y)
				ObservableList<Series<Number, Number>> series = lineChartSIR.getData();
				// Grabs all three of the series from the linchart and then grabs their last
				// point by using the total number of points - 1 to then remove it from the
				// graph, this causes the graph to go backwards
				series.get(0).getData().remove((series.get(0).getData().size() - 1));
				series.get(1).getData().remove((series.get(1).getData().size() - 1));
				series.get(2).getData().remove((series.get(2).getData().size() - 1));
			}
		}

	}
}
