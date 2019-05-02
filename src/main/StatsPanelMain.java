package main;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.stage.Stage;
import outSideSources.MyClassLoader;
import ui.StatsPanelController;

/*
 *  @author Tomas S
 *  @author - Ryan C
 *  
 *  @Project - Final 
 *  @Professor - Dr. Micah S
 *  @Class - COMP1050
 *  
 *  @Class Description - loads the fxml code of statspanel.fxml and assigns 
 *  the loader and creates the scenes and panes for the UI. and then assigns the controller and other settings. 
 *  Also adding event handlers for the linechart and piechart
 */

public class StatsPanelMain {

	// Creates controller for the states panel
	private StatsPanelController statsPanelController;
	// Custom classLoader given by outside source, credit in class, helps load GUI
	// faster
	private static ClassLoader cachingClassLoader = new MyClassLoader(FXMLLoader.getDefaultClassLoader());
	// Creates lineChart object with the x, y being a number axis
	private LineChart<Number, Number> lineChartSIR;
	// creates piechart object
	private PieChart pieChartSIR;

	@SuppressWarnings("unchecked") // for the linechart model
	public StatsPanelMain() throws IOException {
		// Creates stage that is a substage of the primary stage
		Stage subStage = new Stage();

		// creates a fxml loader for the statespanel.fxml
		FXMLLoader loaderCP = new FXMLLoader(this.getClass().getResource("/ui/StatsPanel.fxml"));
		// assigns custom class loader to the fxmlloader
		loaderCP.setClassLoader(cachingClassLoader);
		// Initializes the states panel controller
		statsPanelController = new StatsPanelController();
		// assigns the controller to the fxmlloader
		loaderCP.setController(statsPanelController);

		// creates parent root element for the fxmlloader
		Parent rootCP = loaderCP.load();
		// assigns scene to the parent root
		Scene sceneCP = new Scene(rootCP);
		// sets the scene to the substage
		subStage.setScene(sceneCP);
		// sets title to the subsbtage
		subStage.setTitle("Statistics Panel");
		// displays the substage to the user
		subStage.show();

		// assigns a css style shete to the stats panel that controls the looks and
		// color of the linechart and pie chart
		sceneCP.getStylesheets().add(getClass().getResource("/css/StatsPanelStyleSheet.css").toExternalForm());

		// Event Handler for when the stage is closed and exits the system to ensure
		// that no threads are running in the background
		subStage.setOnCloseRequest(event -> {
			System.out.println("Stage is closing");
			// stops the background operations which would normally continue if system did
			// not close
			System.exit(0);
		});

		// Initializes the linechart and the piechart and gets them from the stats
		// controller
		lineChartSIR = statsPanelController.getLineChartSIR();
		pieChartSIR = statsPanelController.getPieChartSIR();

		// Event Handler that runs when the user changes the width of the stats panel,
		// it then updates the size of the linchart and piechart
		sceneCP.widthProperty().addListener((obs, oldVal, newVal) -> {
			double sceneX = sceneCP.getWidth();

			// if the scene's x value is greater then 300 then
			if (sceneX > 300) {
				// set the piechart position to sceneX - 300
				pieChartSIR.setLayoutX(sceneX - 300);
				// set the piechart width to 300
				pieChartSIR.setPrefWidth(300);

				lineChartSIR.setLayoutX(0);
				lineChartSIR.setPrefWidth(sceneX);
			} else { // other wise
				// sets position
				lineChartSIR.setLayoutX(0);
				pieChartSIR.setLayoutX(0);

				// sets width
				lineChartSIR.setPrefWidth(sceneX);
				pieChartSIR.setPrefWidth(sceneX);

				// sets width
				lineChartSIR.setPrefWidth(sceneX);
				pieChartSIR.setPrefWidth(sceneX);
			}
		});

		// Event Handler that runs when the user changes the height of the stats panel,
		// it then updates the size of the linchart and piechart
		sceneCP.heightProperty().addListener((obs, oldVal, newVal) -> {
			// Do whatever you want
			double sceneY = sceneCP.getHeight();

			// if the scene's y value is greater then 700
			if (sceneY > 700) {
				// sets the linecharts height to the scene's height and it's y pos to 0
				lineChartSIR.setPrefHeight(sceneY);
				lineChartSIR.setLayoutY(0);

				// sets linecharts height to 300 and the y pos to 0
				pieChartSIR.setPrefHeight(300);
				pieChartSIR.setLayoutY(0);
			} else {
				// sets position
				lineChartSIR.setLayoutY(sceneY - (sceneY * .40));
				pieChartSIR.setLayoutY(sceneY * .1);

				// sets height
				lineChartSIR.setPrefHeight(sceneY * .40);
				pieChartSIR.setPrefHeight(sceneY * .40);
			}

		});
	}

	// returns the control Panel Controller that is assigned to the fxml file.
	public StatsPanelController getSPC() {
		return statsPanelController;
	}

}
