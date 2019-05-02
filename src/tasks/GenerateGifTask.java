package tasks;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;

import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;

import cell.AllCycles;
import javafx.application.Platform;
import javafx.concurrent.Task;
import outSideSources.GifSequenceWriter;
import ui.ControlPanelController;

/*
 *  @author Tomas S
 *  @author - Ryan C
 *  
 *  @Project - Final 
 *  @Professor - Dr. Micah S
 *  @Class - COMP1050
 *  
 *  @Class Description - Generates the images of the cycles and then adds them to a gif. then outputs the gif into the user's directory.
 */

public class GenerateGifTask extends Task<Void> {
	// Initializes the allCycles object that the task uses to reference the cells
	// and grab their information
	private AllCycles allCycles;
	// Initializes the control panel controller to get the progress indicator
	private ControlPanelController controlPanelController;

	public GenerateGifTask(AllCycles allCycles, ControlPanelController controlPanelController) {
		// Assigns the allCycles object from the params
		this.allCycles = allCycles;
		// Assigns the controlPanelController object from the params
		this.controlPanelController = controlPanelController;
	}

	@Override
	protected Void call() throws Exception {

		System.out.println("Gif Generator Started");

		// Creates path by grabbing the user's path and adding the folder name to it
		String path = System.getProperty("user.home") + File.separator + "Documents";
		path += File.separator + "SIR Model Simulation";
		File customDir = new File(path);

		// checks if the path is already made and prints whether it was or wasnt
		if (customDir.exists()) {
			System.out.println(customDir + " already exists");
		} else if (customDir.mkdirs()) {
			System.out.println(customDir + " was created");
		} else {
			System.out.println(customDir + " was not created");
		}

		// Creates image output stream for the gif and tells the location of where to
		// export the gif
		ImageOutputStream iOutput = new FileImageOutputStream(new File(
				path + "\\Simulation_C" + allCycles.getCellDemensions() + "_COI" + allCycles.getChance() + ".gif"));
		// Creates the gif writer object that will create the gif through the images
		GifSequenceWriter gifWriter = new GifSequenceWriter(iOutput, BufferedImage.TYPE_INT_RGB, 50, true);
		// Creates Dimensions of the image
		Dimension imgDim = new Dimension(1300, 900);

		// For loops through all the cycles in the forward motion
		for (int i = 0; i < allCycles.getTotalCycles(); i++) {
			System.out.println("Gif Gen: Current FCycle: " + i);
			// Initializes count as i so platforn.runlater can have access to i as a value
			int count = i;
			// Everything is incased within the platform.runlater to allow for the task to
			// close the thread and the code to then be run on a outside thread the javafx
			// allows connection to. Otherwise javafx will throw an exception refusing to be
			// talked to
			Platform.runLater(new Runnable() {

				@Override
				public void run() {
					// Sets the progress of the progressIndicator
					controlPanelController.getGGPI()
							.setProgress((double) (count / ((double) (allCycles.getTotalCycles() * 2))));
				}
			});

			// Creates Buffered Image
			BufferedImage bi = new BufferedImage(imgDim.width, imgDim.height, BufferedImage.TYPE_INT_RGB);

			// Creates Graphics2D
			Graphics2D g2d = bi.createGraphics();

			// sets Styles
			g2d.setBackground(Color.WHITE);
			g2d.fillRect(0, 0, imgDim.width, imgDim.height);
			g2d.setColor(Color.BLACK);

			// Drawing Stroke
			BasicStroke bs = new BasicStroke(1);
			g2d.setStroke(bs);

			// random generator
			Random gen = new Random();

			// draws all the rectangles through x, y
			for (int x = 0; x < allCycles.getGridDemensionsX(); x++) {
				for (int y = 0; y < allCycles.getGridDemensionsY(); y++) {
					// Gets state from cell and turns it into a color from allCycles
					javafx.scene.paint.Color fx = allCycles
							.getColorByState(allCycles.getCell(x, y).getStateFromAllStateAtCycle(i));

					// turns it into a java awt color
					java.awt.Color awtColor = new java.awt.Color((float) fx.getRed(), (float) fx.getGreen(),
							(float) fx.getBlue(), (float) fx.getOpacity());
					// sets the color of graphics2d to the color of the current cell
					g2d.setColor(awtColor);
					// draws the rectangle in the image
					g2d.drawRect(x * allCycles.getCellDemensions(), y * allCycles.getCellDemensions(),
							allCycles.getCellDemensions(), allCycles.getCellDemensions());
					// fills the rectangle with the color
					g2d.fillRect(x * allCycles.getCellDemensions(), y * allCycles.getCellDemensions(),
							allCycles.getCellDemensions(), allCycles.getCellDemensions());
				}
			}
			// adds the image to the gif writer
			gifWriter.writeToSequence(bi);
		}

//-------------------------------------------------------------------------------------------------------------

		// For loops through all the cycles in the backward motion
		for (int i = allCycles.getTotalCycles(); i > 0; i--) {
			System.out.println("Gif Gen: Current FCycle: " + i);
			// Initializes count as i so platforn.runlater can have access to i as a value
			int count = i;
			// Everything is incased within the platform.runlater to allow for the task to
			// close the thread and the code to then be run on a outside thread the javafx
			// allows connection to. Otherwise javafx will throw an exception refusing to be
			// talked to
			Platform.runLater(new Runnable() {

				@Override
				public void run() {
					// Sets the progress of the progressIndicator
					controlPanelController.getGGPI().setProgress((double) ((count + allCycles.getTotalCycles())
							/ ((double) (allCycles.getTotalCycles() * 2))));
				}
			});

			// Creates Buffered Image
			BufferedImage bi = new BufferedImage(imgDim.width, imgDim.height, BufferedImage.TYPE_INT_RGB);

			// Creates Graphics2D
			Graphics2D g2d = bi.createGraphics();

			// sets Styles
			g2d.setBackground(Color.WHITE);
			g2d.fillRect(0, 0, imgDim.width, imgDim.height);
			g2d.setColor(Color.BLACK);

			// Drawing Stroke
			BasicStroke bs = new BasicStroke(1);
			g2d.setStroke(bs);

			// random generator
			Random gen = new Random();

			// draws all the rectangles through x, y
			for (int x = 0; x < allCycles.getGridDemensionsX(); x++) {
				for (int y = 0; y < allCycles.getGridDemensionsY(); y++) {
					// Gets state from cell and turns it into a color from allCycles
					javafx.scene.paint.Color fx = allCycles
							.getColorByState(allCycles.getCell(x, y).getStateFromAllStateAtCycle(i));

					// turns it into a java awt color
					java.awt.Color awtColor = new java.awt.Color((float) fx.getRed(), (float) fx.getGreen(),
							(float) fx.getBlue(), (float) fx.getOpacity());
					// sets the color of graphics2d to the color of the current cell
					g2d.setColor(awtColor);
					// draws the rectangle in the image
					g2d.drawRect(x * allCycles.getCellDemensions(), y * allCycles.getCellDemensions(),
							allCycles.getCellDemensions(), allCycles.getCellDemensions());
					// fills the rectangle with the color
					g2d.fillRect(x * allCycles.getCellDemensions(), y * allCycles.getCellDemensions(),
							allCycles.getCellDemensions(), allCycles.getCellDemensions());
				}
			}
			// adds the image to the gif writer
			gifWriter.writeToSequence(bi);
		}

		// closes the gif writer
		gifWriter.close();
		// closes the image output stream
		iOutput.close();

		// Everything is incased within the platform.runlater to allow for the task to
		// close the thread and the code to then be run on a outside thread the javafx
		// allows connection to. Otherwise javafx will throw an exception refusing to be
		// talked to
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				// sets the progressindicator to 100% to display the done visual affect
				controlPanelController.getGGPI().setProgress(100.0);
			}
		});

		System.out.println("Gif Generator Ended");

		return null;
	}

}
