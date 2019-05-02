package ui;

import cell.Cell;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

/*
 *  @author Tomas S
 *  @author - Ryan C
 *  
 *  @Project - Final 
 *  @Professor - Dr. Micah S
 *  @Class - COMP1050
 *  
 *  @Class Description - The mainController for the simulation that is tied to the
 *   UserInterface.fxml which is the primary stage of the program
 */

public class MainController {
	// Initializes and grabs the control objects from the FXML file. The @FXML
	// reference's the controller's FXML file

	@FXML
	private GridPane grid; // The gridPane where all the cells are added
	@FXML
	private Pane gridPane; // The Main pane for the UI
	@FXML

	// Initializes a boolean that represents whether the simulation is currently
	// displaying a cycle
	private boolean isCycleCurrentlyRunning = false;
	// Initializes a boolean that represents where the simulation is currently
	// paused
	private boolean pause = false;
	// Initializes a boolean that represents whether the grid has been reset since
	// the last
	private boolean hasGridBeenReset = false;
	// Initializes a boolean that represents whether the cycle should be paused at
	// the end
	private boolean pauseAfterCycle = false;
	// Initializes a boolean that represents whether the pause btn is active and
	// being displayed
	private boolean pauseBtnActive = false;
	// Initializes a boolean that represents whether the unpuase btn is active and
	// being displayed
	private boolean unPauseBtnActive = false;
	// Initializes a boolean that represents whether the simulation is on it's first
	// run
	private boolean firstRun = true;
	// Initializes a boolean that represents whether all the cycles have been
	// generated for the sim in the background
	private boolean isAllTheCyclesGenerated = false;
	// Initializes a boolean that represents whether the cycle is currently running
	// forward or backwards
	private boolean forwardCycle = false;

	// Initializes the Control Panel Controller to allow for reference to it
	private ControlPanelController controlPanelController;
	// Initializes the stats panel controller to allow for reference to it
	private StatsPanelController statsPanelController;

	// Initializes the cellArray which holds all the current cells on the grid
	private Cell[][] cellArray;

	@FXML // CONSTRUCTOR
	void initialize() {
	}

	// GETTERS
	public GridPane returnGrid() {
		return grid; // returns the grid pain
	}

	public Pane returnGridPane() {
		return gridPane; // returns the pane that the grid pane is on
	}

	public boolean getIsCycleCurrentlyRunning() {
		return isCycleCurrentlyRunning; // returns whether the cycle is currently running boolean
	}

	public boolean getPause() {
		return pause; // returns whether the sim is currently paused
	}

	public boolean getHasGridBeenReset() {
		return hasGridBeenReset; // returns whether the gird has been reset
	}

	public boolean isForwardCycle() {
		return forwardCycle; // returns whether the cycle is currently moving forward or backwards
	}

	public boolean isPauseAfterCycle() {
		return pauseAfterCycle; // returns whether to pause the sim after the cycle is done displaying
	}

	public boolean isPauseBtnActive() {
		return pauseBtnActive; // returns whether the pause btn is active or inactive
	}

	public boolean isUnPauseBtnActive() {
		return unPauseBtnActive; // returns whether the unpause btn is currently active or inactive
	}

	public boolean isFirstRun() {
		return firstRun; // returns the boolean that represents whehther it's the sim's first run
	}

	public boolean isAllTheCyclesGenerated() {
		return isAllTheCyclesGenerated; // returns whether the all the cycles have been generated
	}

	public ControlPanelController getControlPanelController() {
		return controlPanelController; // returns the control Panel controller
	}

	public StatsPanelController getStatsPanelController() {
		return statsPanelController; // returns the stats panel controller
	}

	// SETTERS

	public void setCellArray(Cell[][] cellArrayC) {
		cellArray = cellArrayC; // sets the cellArray to the one passed in the params
	}

	public void setHasGridBeenReset(boolean state) {
		hasGridBeenReset = state; // sets whether the grid has been reset
	}

	public void setPause(boolean state) {
		pause = state; // sets whether the sim is currently paused
	}

	public void setIsCycleCurrentlyRunning(boolean state) {
		isCycleCurrentlyRunning = state; // sets whether the sim is currently running
	}

	public void setFirstRun(boolean firstRun) {
		this.firstRun = firstRun; // sets the boolean to the one given in the params
	}

	public void setUnPauseBtnActive(boolean unPauseBtnActive) {
		this.unPauseBtnActive = unPauseBtnActive; // sets whether the unpause btn is active or inactive depending on the
													// value given in the params
	}

	public void setPauseBtnActive(boolean pauseBtnActive) {
		this.pauseBtnActive = pauseBtnActive; // sets the pause button state to the one given in the params
	}

	public void setPauseAfterCycle(boolean pauseAfterCycle) {
		this.pauseAfterCycle = pauseAfterCycle; // sets the value of pause After Cycle to the one given in paramas
	}

	public void setForwardCycle(boolean state) {
		this.forwardCycle = state; // sets the forward cycle boolean to the value given in the params
	}

	public void setAllTheCyclesGenerated(boolean isAllTheCyclesGenerated) {
		this.isAllTheCyclesGenerated = isAllTheCyclesGenerated; // sets whether all the cycles have been generated to
																// the value given in params
	}

	public void setStatsPanelController(StatsPanelController statsPanelController) {
		this.statsPanelController = statsPanelController; // sets the stats panel controller to the one given in params
	}

	public void setControlPanelController(ControlPanelController controlPanelController) {
		this.controlPanelController = controlPanelController; // sets the control paenl controller to the one given in
																// params
	}

	// MUTATORS

	// Adds all the cells onto the grid, column by column
	public void displayCellRowOnStarUp(Cell[] cellArrayC) {
		// takes in the column by params and then runs through it, adding the rectangle
		// object to the grid at the x, y position
		for (int i = 0; i < cellArrayC.length; i++) {
			// adding the rectangle at the x,y position
			grid.add(cellArrayC[i].getCell(), cellArrayC[i].getX(), i);
		}
	}

	// Updates the colors and state of the cells in the object according to the new
	// version of them given in params
	public void displayCellRowWithCycle(Cell[] cellArrayC, int row, int currentCycle) throws IndexOutOfBoundsException {
		// Initializes the displacement int which holds a value that changes depending
		// on the direction of the cycle. This is needed because the method looks at the
		// cell's next state to see whether it has changed if moving forward, if moving
		// backwards then it looks at the state before the current cycle.
		int displacementForDirectionOfCycle;

		// Assigns a value to the variable depending on the direction
		if (forwardCycle) {
			displacementForDirectionOfCycle = -1;
		} else {
			displacementForDirectionOfCycle = 1;
		}

		// for loops through the entire column thats given in the params
		for (int i = 0; i < cellArrayC.length; i++) {
			// if a cell from the new column does not have the same state as the cell in the
			// same x, y position then it will go one to update it's state and color since
			// the cell changed
			if (cellArrayC[i].getStateFromAllStateAtCycle(currentCycle) != cellArray[row][i]
					.getStateFromAllStateAtCycle(currentCycle + displacementForDirectionOfCycle)) {

				// updates the state and color on the grid
				cellArray[row][i].setColorByState(cellArray[row][i].getStateFromAllStateAtCycle(currentCycle));
			}
		}
	}

	// removes all the cells from the grid
	public void removeAllFromGrid() {
		grid.getChildren().clear();
	}
}