package ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;

/*
 *  @author Tomas S
 *  @author - Ryan C
 *  
 *  @Project - Final 
 *  @Professor - Dr. Micah S
 *  @Class - COMP1050
 *  
 *  @Class Description - THe controller for the ControlPanel.fxml file. This controls and the UI
 */

public class ControlPanelController {

	// Initializes and grabs the control objects from the FXML file. The @FXML
	// reference's the controller's FXML file

	@FXML
	private Button btnGG; // Generate Grid Btn
	@FXML
	private Button btnRS; // Run Simulation Btn
	@FXML
	private Button btnGF; // Generate gif btn
	@FXML
	private ProgressIndicator currentTaskProgressIndicator; // Current Task Indicator
	@FXML
	private ProgressIndicator allTasksProgressIndicator; // All Tasks Indicator
	@FXML
	private ProgressIndicator gifGenProgressIndicator; // gif generator indicator
	@FXML
	private Pane controlScene; // Control Panel's main pane
	@FXML
	private Label labelCD; // Cell Dimensions label
	@FXML
	private Label labelCI; // Cell Infected Chance Label
	@FXML
	private Slider sliderCD; // Cell Dimensions slider
	@FXML
	private Slider sliderCIN; // Cell infected chance slider
	@FXML
	private CheckBox checkBoxPAC; // Pause after cycle checkbox

	@FXML // CONSTRUCTOR
	void initialize() {
		// main controller for the UI
	}

	// GETTERS

	public Button getBtnGG() {
		return btnGG; // get generate grid btn
	}

	public Button getBtnRS() {
		return btnRS; // get run sim btn
	}

	public Button getBtnGF() {
		return btnGF; // get generate gif btn
	}

	public ProgressIndicator getCTPI() {
		return currentTaskProgressIndicator; // get current task indicator
	}

	public ProgressIndicator getATPI() {
		return allTasksProgressIndicator; // get all tasks indicator
	}

	public ProgressIndicator getGGPI() {
		return gifGenProgressIndicator; // get gif gen indicator
	}

	public Pane getCS() {
		return controlScene; // get main pane
	}

	public Label getLCD() {
		return labelCD; // get cell dimensions label
	}

	public Label getLCI() {
		return labelCI; // get cell infected chance label
	}

	public Slider getSCD() {
		return sliderCD; // get cell dimensions slider
	}

	public Slider getSCI() {
		return sliderCIN; // get cell infected chance slider
	}

	public CheckBox getCheckBoxPAC() {
		return checkBoxPAC; // get pause after cycle checkbox
	}

	public boolean getPACStatus() {
		return checkBoxPAC.isSelected(); // returns boolean of whether the pause after cycle checkbox is selected
	}

}
