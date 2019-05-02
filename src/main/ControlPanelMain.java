package main;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import outSideSources.MyClassLoader;
import ui.ControlPanelController;

/*
 *  @author Tomas S
 *  @author - Ryan C
 *  
 *  @Project - Final 
 *  @Professor - Dr. Micah S
 *  @Class - COMP1050
 *  
 *  @Class Description - Loads the fxml file for the controlpanel.fxml and assigns the scenes and panes to it and then displays it to the user
 */

public class ControlPanelMain {

	// Controller object for the controlPanel.fxml
	private ControlPanelController controlPanelController;
	// Custom classLoader given by outside source, credit in class, helps load GUI
	// faster
	private static ClassLoader cachingClassLoader = new MyClassLoader(FXMLLoader.getDefaultClassLoader());

	public ControlPanelMain() throws IOException {

		// creates a a stage that is a substage of the primary stage which is the
		// userInterface.fxml in main.kjava
		Stage subStage = new Stage();

		// assigns the fxml loader to ControlPanel.fxml
		FXMLLoader loaderCP = new FXMLLoader(this.getClass().getResource("/ui/ControlPanel.fxml"));
		// assigns the class loader
		loaderCP.setClassLoader(cachingClassLoader);
		// Initializes the controller
		controlPanelController = new ControlPanelController();
		// assigns the controller
		loaderCP.setController(controlPanelController);

		// creates the parent root
		Parent rootCP = loaderCP.load();
		// creates the scene of the control panel UI
		Scene sceneCP = new Scene(rootCP);
		// assigns the scene to the substage
		subStage.setScene(sceneCP);
		// sets Title of substage
		subStage.setTitle("Control Panel");
		// displays substage
		subStage.show();

		// Event Handler for when the stage is closed and exits the system to ensure
		// that no threads are running in the background
		subStage.setOnCloseRequest(event -> {
			System.out.println("Stage is closing");
			// stops the background operations which would normally continue if system did
			// not close
			System.exit(0);
		});

	}

	// returns the control Panel Controller that is assigned to the fxml file.
	public ControlPanelController getCPC() {
		return controlPanelController;
	}

}
