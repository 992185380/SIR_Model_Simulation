package tasks;

import cell.AllCycles;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
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
 *  @Class Description - 
 */

public class GetAllCyclesTask extends Task<Void> {

	private double percentage = 0.0;
	private ProgressIndicator currentTaskProgressIndicator;
	private MainController mainController;
	private AllCycles allCycles;

	public GetAllCyclesTask(MainController mainController, AllCycles allCycles) {
		this.mainController = mainController;
		this.currentTaskProgressIndicator = mainController.getControlPanelController().getCTPI();
		this.allCycles = allCycles;

		currentTaskProgressIndicator.progressProperty().unbind();

		createAllCycles();
	}

	@Override
	protected Void call() throws Exception {
		// sets up grid
		return null;
	}

	public void createAllCycles() {
		try {
			if (percentage < 1.0) {
				System.out.println("Getting Cycle. Size: " + allCycles.getCell(0, 0).getSizeOfAllStates());

				ReturnNewGridTask task = new ReturnNewGridTask(allCycles);
				new Thread(task).start();

				task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
					@Override
					public void handle(WorkerStateEvent event) {
						// TODO Auto-generated method stub
						allCycles.setCellArray(task.getValue());

						GetPercentageTask task2 = new GetPercentageTask(allCycles);
						new Thread(task2).start();

						task2.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
							@Override
							public void handle(WorkerStateEvent event) {
								// TODO Auto-generated method stub
								percentage = task2.getValue();
								if (percentage > .99 && percentage < 1.0) {
									currentTaskProgressIndicator.setProgress(.99);
								} else {
									currentTaskProgressIndicator.setProgress(percentage);
								}
								createAllCycles();
							}
						});
					}
				});
			} else {
				// once cycle is done it runs it
				currentTaskProgressIndicator.setProgress(100.0);
				// mainController.getStatsPanelController().setBoundsForAxis(allCycles.getTotalCycles(),
				// allCycles.getTotalNumberOfCells());
				mainController.setAllTheCyclesGenerated(true);
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						// sets text to pause of the Cycle button
						mainController.getControlPanelController().getBtnRS().setText("Pause");
						// ensures the simulation isnt paused
						mainController.setPause(false);
						// tells simulation to run in the forward direction
						mainController.setForwardCycle(true);
						// sets the allCycles object to the maincontroller
						mainController.setCellArray(allCycles.getCellArray());

						// runs the runallcyckestask
						RunAllCyclesTask task = new RunAllCyclesTask(mainController, -1, allCycles);
						new Thread(task).start();

						// Kills the current thread after launching the task above.
						Thread.currentThread().interrupt();
					}
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
