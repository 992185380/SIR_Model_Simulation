package main;

import cell.AllCycles;
import javafx.application.Application;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Slider;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import outSideSources.MyClassLoader;
import tasks.DisplayEntireRowTask;
import tasks.GenerateGifTask;
import tasks.GetAllCyclesTask;
import tasks.ResetGridTask;
import tasks.RunAllCyclesTask;
import tasks.SetUpGridTask;
import ui.ControlPanelController;
import ui.MainController;
import ui.StatsPanelController;

/*
 *  @author Tomas S
 *  @author - Ryan C
 *  
 *  @Project - Final 
 *  @Professor - Dr. Micah S
 *  @Class - COMP1050
 *  
 *  @Class Description - The main method that initializes the UI and and 
 *  various other variables that are crucial for the simulation. 
 *  This is the main hub for the sim. 
 */

public class Main extends Application {

	// Grid Dimensions = actualSize/cellDimensions
	private int gridDimensions_x;
	private int gridDimensions_y;
	// Dimensions of cell, by default 7, can be changed by user
	private int cellDimensions = 7;
	// Chance of an infected cell being randomly chosen, default is 300, can be
	// changed by user
	private int infectedCellChance = 300;
	// lastUsed holds the lastUsed chance and cellDimensions so if user changes
	// settings and then changes back to the last used ones the grid will not be
	// remade
	private int lastUsedInfectedCellChance = 300;
	private int lastUsedCellDimensions = 7;
	// Controllers, which control the FXML files and the elements inside
	private MainController mainController;
	private ControlPanelController controlPanelController;
	private StatsPanelController statsPanelController;
	// boolean to determine if the grid is fully displayed so the user can run the
	// simulation
	private boolean isGridSetUp = false;
	// boolean to determine if gridDimensions or cellSettings have been changed or
	// not, if so then grid
	// is re-gen-ed and redisplayed
	private boolean haveGridDimensionsChanged = false;
	private boolean haveCellSettingsChanged = false;
	// Progress indicators the represent current status of tasks
	private ProgressIndicator currentTaskProgressIndicator;
	private ProgressIndicator allTasksProgressIndicator;
	// Custom classLoader given by outside source, credit in class, helps load GUI
	// faster
	private static ClassLoader cachingClassLoader = new MyClassLoader(FXMLLoader.getDefaultClassLoader());
	// AllCycles object holds all valuable data related to the application
	private AllCycles allCycles;

	public static void main(String[] args) {
		// launches GUI
		launch(args);
	}

	// Launches GUI method plus various other tasks. Main Method of the class.
	public void start(Stage primaryStage) throws Exception {
		// Initializes loader and assigns to the main UI
		FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/ui/UserInterface.fxml"));
		loader.setClassLoader(cachingClassLoader);

		// JavaFX Controller, assigns mainController for the program
		mainController = new MainController();
		loader.setController(mainController);
		Parent root = loader.load();

		// sets up scene
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle("SIR Model");
		primaryStage.show();

		// Initializes stats panel and assigns controller to the statsPanel
		StatsPanelMain mainSP = new StatsPanelMain();
		statsPanelController = mainSP.getSPC();
		mainController.setStatsPanelController(statsPanelController);

		// Initializes control panel and assisn controller to the control panel
		ControlPanelMain mainCP = new ControlPanelMain();
		controlPanelController = mainCP.getCPC();
		mainController.setControlPanelController(controlPanelController);

		// Gets main pane from main UI and adds a border to it
		Pane controlPanelPane = controlPanelController.getCS();
		controlPanelPane.setBorder(new Border(
				new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

		// Gets main Grid from UI and adds a border to it
		GridPane grid = mainController.returnGrid();
		grid.setBorder(new Border(
				new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

		// Initializes all control buttons and gets them from controller
		Button cycleBtn = controlPanelController.getBtnRS(); // Runs simulation
		Button setUpBtn = controlPanelController.getBtnGG(); // Sets up grid
		Button getGifBtn = controlPanelController.getBtnGF(); // Gif Gen

		// Initializes sliders and gets them from controller
		Slider sliderCD = controlPanelController.getSCD(); // Cell Dimensions
		Slider sliderCI = controlPanelController.getSCI(); // Cell Infected Chance

		// Initializes labels and gets them from controller, labels for slider values
		Label labelCD = controlPanelController.getLCD(); // Cell dimensions label
		Label labelCI = controlPanelController.getLCI(); // Cell infected chance label

		// Initializes checkbox and gets them from controller
		CheckBox checkBoxPAC = controlPanelController.getCheckBoxPAC(); // pause after cycle checkbox

		// Initializes progress indicators that show progress of tasks
		currentTaskProgressIndicator = controlPanelController.getCTPI(); // Current tasks
		allTasksProgressIndicator = mainController.getControlPanelController().getATPI(); // All Tasks

		// ------ Event Handlers ----------------

		// Event Handler for when the application window is closed, closes the system to
		// close all running background threads
		primaryStage.setOnCloseRequest(event -> {
			System.out.println("Stage is closing");
			System.exit(0);
		});

		// Event Handler for Scene's width Property that updates grid size
		scene.widthProperty().addListener((obs, oldVal, newVal) -> {
			// Updates layout and width
			grid.setLayoutX(0);
			grid.setLayoutY(0);
			grid.setPrefWidth(scene.getWidth());
			// Lets grid know it needs to regen the cells instead of scrambling the current
			// states and colors
			haveGridDimensionsChanged = true;
		});

		// Event Handler for Scene's height Property that updates grid size
		scene.heightProperty().addListener((obs, oldVal, newVal) -> {
			// Updates layout and height
			grid.setLayoutX(0);
			grid.setLayoutY(0);
			grid.setPrefHeight(scene.getHeight());
			// Lets grid know it needs to regen the cells instead of scrambling the current
			// states and colors
			haveGridDimensionsChanged = true;
		});

		// Event Handler for Pause After Cycle checkbox change in value
		checkBoxPAC.selectedProperty().addListener((obs, oldVal, newVal) -> {
			// sends value over to mainController so other tasks are able to access it when
			// needed
			mainController.setPauseAfterCycle(checkBoxPAC.isSelected());
		});

		// Event Handler for Cell Dimensions slider that allCycles holds value for use
		sliderCD.valueProperty().addListener((obs, oldVal, newVal) -> {
			// slider is 0-100, divides value by 100 then multiplies by 20 to convert to
			// 0-20 scale, then rounds to whole number
			cellDimensions = (int) Math.round(20 * (sliderCD.getValue() / 100));

			// makes sure value cannot go below 1 cellDimensions since anything below will
			// cause problems with CPU
			if (cellDimensions <= 1) {
				cellDimensions = 2;
			}

			// Sets label to new Cell Dimensions value for visual display
			labelCD.setText(String.valueOf(cellDimensions));

			// Checks if the last used is not equal to the current value, if not then tells
			// setUpGrid to regen the grid
			if (lastUsedCellDimensions != cellDimensions) {
				haveCellSettingsChanged = true;
			} else {
				haveCellSettingsChanged = false;
			}
		});

		// Event Handler for Cell Infected Chance slider that allCycles holds value for
		// use
		sliderCI.valueProperty().addListener((obs, oldVal, newVal) -> {
			// slider is 0-100, divides value by 100 then multiplies by 1000 to convert to
			// 0-1000 scale, then rounds to whole number
			infectedCellChance = (int) Math.round(1000 * (sliderCI.getValue() / 100));

			// Sets label to new Cell Infected Chance value for visual display
			labelCI.setText(String.valueOf(infectedCellChance));

			// Checks if the last used is not equal to the current value, if not then tells
			// setUpGrid to regen the grid
			if (lastUsedInfectedCellChance != infectedCellChance) {
				haveCellSettingsChanged = true;
			} else {
				haveCellSettingsChanged = false;
			}
		});

		// Event Handler for Set Up Button that will either regenerated the grid or
		// scramble the current grid uppon user's request
		setUpBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				System.out.println("Setting Up Grid");

				// if the grid has been setup for the first time then set boolean to true
				mainController.setFirstRun(true);

				// calculates the dimensions of the grid by taking grid's physical pixel size
				// and divides by cell dimensions
				gridDimensions_x = (int) (grid.getPrefWidth() / cellDimensions);
				gridDimensions_y = (int) (grid.getPrefHeight() / cellDimensions);

				// prints dimensions into console
				System.out.println("Dimensions: X: " + gridDimensions_x + " Y: " + gridDimensions_y);

				// resets the pause button booleans both to false, they determine the state of
				// the pause buttons
				mainController.setPauseBtnActive(false);
				mainController.setUnPauseBtnActive(false);
				// sets Run Cycle button to the text to show that it's ready to be used
				cycleBtn.setText("Run Simulation");

				// runs method that sets up grid.
				setUpGrid();
			}
		});

		// Event Handler for Generate Gif button
		getGifBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				// checks whether simulation has been generated in the background, if so then
				// proceed
				if (mainController.isAllTheCyclesGenerated()) {
					// Runs task in the background that generates the gif of the cycle
					GenerateGifTask task = new GenerateGifTask(allCycles, controlPanelController);
					new Thread(task).start();
				}
			}
		});

		// Event Handler for Run Simulation button
		cycleBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				// Series of statements to see what part of the simulation user is int
				// checks to see if pause button is active, this means if the simulation has
				// been turned on and the pause button can be pressed, and then was pressed
				if (mainController.isPauseBtnActive()) {
					// Tells thread that is currently displaying entire cycle that user paused
					mainController.setPause(true);
					// sets Cycle button text to unpause
					cycleBtn.setText("UnPause");
					// Puts the simulation into unPause mode which sets the unpause button as active
					// and waits for user to press it to resume simulation
					mainController.setUnPauseBtnActive(true);
					// takes simulation out of the pause button stage where the sim can be paused,
					// now it's paused and can only be unpaused
					mainController.setPauseBtnActive(false);

					// Checks to see if the user pressed the Unpause button
				} else if (mainController.isUnPauseBtnActive()) {
					// sets text to pause of the Cycle button
					cycleBtn.setText("Pause");

					// puts sim into possible pause situation by setting pause active
					mainController.setPauseBtnActive(true);
					// turns off pause so thread can be relaunched and continue running sim
					mainController.setPause(false);

					// relaunches thread that runs the entire simulation and gives the thread the
					// last ran cycle
					RunAllCyclesTask task = new RunAllCyclesTask(mainController,
							allCycles.getHoldCurrentCycleForPause(), allCycles);
					new Thread(task).start();

					// checks to see if grid is setup and see whether the cycle is already currently
					// running (maybe unused since adding pause button) and checks if grid has been
					// reset if the variables determining the grid have been changed
				} else if (isGridSetUp && !mainController.getIsCycleCurrentlyRunning()
						&& mainController.getHasGridBeenReset()) {

					// sets the boolean that says cycle is running
					mainController.setIsCycleCurrentlyRunning(true);

					// turns on charts and sets up their respective information
					statsPanelController.setUpCharts();

					// sets text of cycle button to pause
					cycleBtn.setText("Pause");

					// puts sim into pause active situation where user can pause the sim
					mainController.setPauseBtnActive(true);

					// runs thread that generates all the cycles in advanched
					GetAllCyclesTask task = new GetAllCyclesTask(mainController, allCycles);
					new Thread(task).start();
				}
			}
		});

	}

	// Method that goes and generates the grid.
	public void setUpGrid() {
		// sets pause to off to reset the boolean
		mainController.setPause(false);
		// sets all tasks progress indicator to reset the value of it to 0%
		allTasksProgressIndicator.setProgress(0);
		// clears data off the statistics panel
		statsPanelController.clearData();

		// checks to see if the grid hasnt been set up or if grid dimensions have been
		// changed or to see if settings aka values of the cell dimensions or cell
		// infected chance have been changed
		if (!isGridSetUp || haveGridDimensionsChanged || haveCellSettingsChanged) {

			// Creates allCycles object that holds main values for the simulation
			allCycles = new AllCycles(gridDimensions_x, gridDimensions_y, cellDimensions, infectedCellChance);
			// removes all current cells from the grid if there are any
			mainController.removeAllFromGrid();
			// updates the last used values
			lastUsedInfectedCellChance = infectedCellChance;
			lastUsedCellDimensions = cellDimensions;

			// Launches task that generates the grid values and objects
			SetUpGridTask task = new SetUpGridTask(allCycles);
			new Thread(task).start();

			// binds progress indicator to SetUpGridTask
			currentTaskProgressIndicator.progressProperty().bind(task.progressProperty());

			// on SUCCEED/once task is completed
			task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
				@Override
				public void handle(WorkerStateEvent t) {
					// assigns cellArray which holds all the cells of the grid to allCycles object
					// to hold, cellArray was generated by SetUpGridTask
					allCycles.setCellArray(task.getValue());

					// unbinds the current progress indicator from the task
					currentTaskProgressIndicator.progressProperty().unbind();

					// Launches task to start displaying the entire grid, row by row, passing a 0 as
					// the first row to start at
					DisplayEntireRowTask task2 = new DisplayEntireRowTask(mainController, 0, allCycles);
					new Thread(task2).start();

					// on SUCCEED/once task is completed
					task2.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
						@Override
						public void handle(WorkerStateEvent t) {
							// sets cellArray which holds all the cells to the mainController's version of
							// cellArray
							mainController.setCellArray(allCycles.getCellArray());
						}
					});

					// sets boolean that says grid is setup
					isGridSetUp = true;
					// sets boolean that says grid dimensions havent been changed since according to
					// last used values it hasnt
					haveGridDimensionsChanged = false;
					// sets boolean that says grid settings havent been changed since according to
					// last used values it hasnt
					haveCellSettingsChanged = false;

					// sets boolean that says grid has been reset in maincontroller's variables
					mainController.setHasGridBeenReset(true);

					System.out.println("Grid Gen Done");
				}
			}); // Checks to see if the grid isnt currently running, in this case this code will
				// run if none of the settings or dimensions have been changed and therefore
				// will simply rescrambles the states and colors of the grid
		} else {
			// scrambles the grid instead of regenerating it

			// puts the simulation into pause mode to stop the sim
			mainController.setPause(true);
			// sets pause btn as inactive
			mainController.setPauseBtnActive(false);
			// sets unpause btn as inactive
			mainController.setUnPauseBtnActive(false);
			// sets is cycle currently running as false
			mainController.setIsCycleCurrentlyRunning(false);
			// resets the current Cycle
			allCycles.setHoldCurrentCycleForPause(0);

			// Launches Reset Grid Task that rescrambles the grid, passes allCycles that
			// gives the grid to the task
			ResetGridTask task3 = new ResetGridTask(allCycles);
			new Thread(task3).start();

			// binds current task progress indicator to the task
			currentTaskProgressIndicator.progressProperty().bind(task3.progressProperty());

			// on SUCCEED/ once task is finished
			task3.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
				@Override
				public void handle(WorkerStateEvent t) {
					// sets updated cellarray to maincontroller
					mainController.setCellArray(allCycles.getCellArray());

					// unbinds current task progress indicator
					currentTaskProgressIndicator.progressProperty().unbind();

					// sets boolean that says grid has been reset in maincontroller's variables
					mainController.setHasGridBeenReset(true);

					// resets currentTaskProgressIndicator
					currentTaskProgressIndicator.setProgress(100.0);
				}
			});
		}
	}
}