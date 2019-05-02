package cell;

import java.util.ArrayList;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/*
 *  @author Tomas S
 *  @author - Ryan C
 *  
 *  @Project - Final 
 *  @Professor - Dr. Micah S
 *  @Class - COMP1050
 *  
 *  @Class Description - An object the represents the cell in the grid. 
 *  Every rectangle is a cell object that holds certain data that is used throughout the sim that is unique to every cell
 */

public class Cell {
	// x and y holds the cell's position in the grid
	private int x;
	private int y;
	// Color hols the cell's current javafx color
	private Color color;
	// state holds the cell's current state that ranges from 0-10 and every state
	// represents a different level of infection
	private int state;
	// Rectangle object is what is physically displayed onto the grid by Javafx. The
	// object is apart of their library. The cell object initializes the rectangle
	// objet and then holds it for easy access to change certain settings and values
	private Rectangle cell;
	// every cell has an arraylist that holds their states throughout the cycles so
	// that the sim can backtrack and forward track after the entire sim has been
	// created
	private ArrayList<Integer> allStates;

	// CONSTRUCTOR - takes in values as param and initializes certain variables and
	// creates a rectangle and list object
	public Cell(int x, int y, Color color, int state, int demension_x, int demension_y) {
		this.x = x;
		this.y = y;
		this.color = color;
		this.state = state;
		// Creates a rectangle object that the cell object will hold and pass along to
		// javafx.
		this.cell = new Rectangle(demension_x, demension_y, color);
		// Initializes the list that holds all the int state values throughout the
		// entire cycle for this particular cell.
		allStates = new ArrayList<Integer>();
	}

	// NOTES
	/*
	 * if state is between 2-4 then it simply grows state by 1. if state is between
	 * 5-9 then it grows by one. if state is 1 then it can be infected. so basically
	 * just grows.
	 */

	// GETTERS

	public Color getColor() {
		return color; // returns current javafx color
	}

	public int getY() {
		return y; // returns cell's current y value
	}

	public int getX() {
		return x; // returns cell's current x value
	}

	public int getState() {
		return state; // return cell's current state
	}

	public Rectangle getCell() {
		return cell; // returns the rectangle object
	}

	public int getSizeOfAllStates() {
		return allStates.size(); // gets the number of values in the allStates list which also represents the
									// number of cycles throughout the entire sim
	}

	public int getStateFromAllStateAtCycle(int c) {
		// Try catch for here because the arraylist will go out of bounds and the catch
		// throws the exception array since it does not impact the sim
		try {
			return allStates.get(c); // returns the state at cycle c
		} catch (Exception e) {
			// e.printStackTrace();
		}
		return 0; // returns 0 if cycle is out of bounds
	}

	// SETTERS

	public void setColor(Color color) { // sets the color given to the object's variable and then sets fill of the
										// rectangle to that color
		this.color = color;
		cell.setFill(color);
	}

	public void setState(int state) { // sets state value given
		this.state = state;
	}

	public void addCurrentStateToList() { // adds the object current value of state to the allStates list
		allStates.add(state);
	}

	public void setStateList(int state) { // adds a specific state given to the allstates list
		this.state = state;
		allStates.add(state);
	}

	public void setCell(Rectangle cell) { // takes given rectangle cell and sets it to the object's rectangle cell
		this.cell = cell;
	}

	// sets infected cell
	public void setInfectedCell() { // sets this current cell to become infected by settings its state to 5 and
									// changing it's color to correspond
		state = 5;
		cell.setFill(Color.web("7afe7c")); // sets fill by grabbing color through it's hex value
	}

	// MUTATORS

	public void clearStateList() {
		allStates.clear(); // clears out all the values in the allState list, usually if sim is being reset
	}

	public void updateState() { // adds one value to the state if the state is not equal to 10. 10 is max value
		if (state != 10) {
			state++;
		}
	}

	public void updateStateToBecomeInfected() { // adds 1 to the value of the state if it's 0-1 current state which
												// makes it become infected since 2-9 is infected state
		if (state <= 1) {
			state++;
		}
	}

	public void setColorByState(int state) { // Takes in the state of the cell and runs through switch statement the
												// sets the fill of the rectangle to the color of the hex value which is
												// parsed by javafx, this is javafx.color
		switch (state) {
		case 0:
			cell.setFill(Color.web("00006f"));
			break;
		case 1:
			cell.setFill(Color.web("0000cc"));
			break;
		case 2:
			cell.setFill(Color.web("0000f3"));
			break;
		case 3:
			cell.setFill(Color.web("00b1fe"));
			break;
		case 4:
			cell.setFill(Color.web("27fecf"));
			break;
		case 5:
			cell.setFill(Color.web("7afe7c"));
			break;
		case 6:
			cell.setFill(Color.web("cdfe29"));
			break;
		case 7:
			cell.setFill(Color.web("fac500"));
			break;
		case 8:
			cell.setFill(Color.web("fe6300"));
			break;
		case 9:
			cell.setFill(Color.web("f10900"));
			break;
		case 10:
			cell.setFill(Color.web("860000"));
			break;
		}

		cell.setOpacity(1); // sets opacity to 1 for safe keeping
	}
}
