package cell;

import java.util.ArrayList;

import javafx.scene.paint.Color;

/*
 *  @author Tomas S
 *  @author - Ryan C
 *  
 *  @Project - Final 
 *  @Professor - Dr. Micah S
 *  @Class - COMP1050
 *  
 *  @Class Description - Main object that holds cruical data about the simulation.
 *   The object is passed around throughout the application to different tasks etc that require the variables below.
 */

public class AllCycles {

	// CellArray holds all the cell objects in an x,y grid that represent thir
	// actual position on the grid.
	private Cell[][] cellArray;
	// gridDimensions are the x,y values of the grid once the actual size has been
	// divided by the cellDimensions size
	private int gridDemensionsX;
	private int gridDemensionsY;
	// Holds the cell Dimensions size based on the user's input
	private int cellDemensions;
	// cell infected chance that holds the value based on the user's input
	private int chance;
	// holds that index of the last cycle that ran so once user unpauses the thread
	// can be relaunched and continue from the position they were at
	private int holdCurrentCycleForPause;

	// Variables that hold statistics
	// holdds the total number of cells, x * y = total
	private int totalNumberOfCells;
	// List that holds the number of susceptible cells for every cycle .
	private ArrayList<Integer> susceptibleCellCount;
	// list that holds the number of infected cells for every cycle
	private ArrayList<Integer> infectedCellCount;
	// list that holds the number of recovered cells for every cycle
	private ArrayList<Integer> recoveredCellCount;

	// CONSTRUCTOR
	// Takes in values through param once the object is made, it's done in the setUp
	// method in Main.java
	public AllCycles(int gridDemensionsX, int gridDemensionsY, int cellDemensions, int chance) {
		// creates the cellArray, an 2d array that represents the x,y grid
		this.cellArray = new Cell[gridDemensionsX][gridDemensionsY];
		this.gridDemensionsX = gridDemensionsX;
		this.gridDemensionsY = gridDemensionsY;
		this.cellDemensions = cellDemensions;
		this.chance = chance;

		// sets total number of cells to 0 to simply initializes it
		totalNumberOfCells = 0;
		// Initializes the lists for the different states of the cells
		susceptibleCellCount = new ArrayList<Integer>();
		infectedCellCount = new ArrayList<Integer>();
		recoveredCellCount = new ArrayList<Integer>();
	}

	// GETTERS

	public Cell getCell(int x, int y) {
		return cellArray[x][y]; // returns cell at x, y position
	}

	public Cell[] getCellArrayRow(int x) {
		return cellArray[x]; // returns row of cells at x position
	}

	public Cell[][] getCellArray() {
		return cellArray; // returns entire grid
	}

	public int getRowCount() {
		return cellArray.length; // gets number of rows in the grid
	}

	public int getCellDemensions() {
		return cellDemensions; // returns the cell Dimensions
	}

	public int getGridDemensionsY() {
		return gridDemensionsY; // returns grid's y value
	}

	public int getGridDemensionsX() {
		return gridDemensionsX; // returns grid's x value
	}

	public int getChance() {
		return chance; // returns cell infected chance
	}

	public int getHoldCurrentCycleForPause() {
		return holdCurrentCycleForPause; // returns index of the last run cyclce
	}

	public int getTotalCycles() {
		return cellArray[0][0].getSizeOfAllStates(); // gets the total number of cycle by grabbing the first cell and
														// checking the size of the arraylist they have which holds
														// every single state of theirs throughout all the cycles
	}

	public int getSusceptibleCellCountAtIndex(int currentCycle) {
		return susceptibleCellCount.get(currentCycle); // gets count of the susceptible at x cycle
	}

	public int getInfectedCellCountAtIndex(int currentCycle) {
		return infectedCellCount.get(currentCycle); // gets count of the infected at x cycle
	}

	public int getRecoveredCellCountAtIndex(int currentCycle) {
		return recoveredCellCount.get(currentCycle); // gets count of the recovered at x cycle
	}

	public int getTotalNumberOfCells() {
		return totalNumberOfCells; // returns the total number of cells in the grid
	}

	public Color getColorByState(int state) {
		// A switch statement that takes the state and runs through all the possible
		// states and then returns the javafx color of the that state. this method is
		// used for the gif generator, the cell's object has their own method similar to
		// this that sets their fill to the color instead of returning the color
		switch (state) {
		case 0:
			return Color.web("00006f");
		case 1:
			return Color.web("0000cc");
		case 2:
			return Color.web("0000f3");
		case 3:
			return Color.web("00b1fe");
		case 4:
			return Color.web("27fecf");
		case 5:
			return Color.web("7afe7c");
		case 6:
			return Color.web("cdfe29");
		case 7:
			return Color.web("fac500");
		case 8:
			return Color.web("fe6300");
		case 9:
			return Color.web("f10900");
		case 10:
			return Color.web("860000");
		}

		return null;
	}

	// SETTERS ----

	public void setCellArray(Cell[][] cellArray) {
		this.cellArray = cellArray; // sets the cellArray
	}

	public void setHoldCurrentCycleForPause(int holdCurrentCycleForPause) {
		this.holdCurrentCycleForPause = holdCurrentCycleForPause; // sets the index of the last run cycle when pause it
																	// hit
	}

	public void setTotalNumberOfCells(int totalNumberOfCells) {
		this.totalNumberOfCells = totalNumberOfCells; // sets the total number of cells in the grid.
	}

	// MUTATORS

	public void addToSusceptibleCellCountList(int count) {
		susceptibleCellCount.add(count); // adds the count of susceptible to the arraylist to represents the index's
											// susceptible size
	}

	public void addToInfectedCellCountList(int count) {
		infectedCellCount.add(count); // adds the count of infected to the arraylist to represents the index's
										// infected size
	}

	public void addToRecoveredCellCountList(int count) {
		recoveredCellCount.add(count); // adds the count of recovered to the arraylist to represents the index's
										// recovered size
	}

	// PRINT

	public void printCount() { // prints count into console for testing purposes
		int susceptible = susceptibleCellCount.get(susceptibleCellCount.size() - 1);
		int infected = infectedCellCount.get(infectedCellCount.size() - 1);
		int recovered = recoveredCellCount.get(recoveredCellCount.size() - 1);

		System.out.println("S: " + susceptible + " I: " + infected + " R: " + recovered);
	}
}
