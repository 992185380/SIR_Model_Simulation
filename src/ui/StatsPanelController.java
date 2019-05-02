package ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;

/*
 *  @author Tomas S
 *  @author - Ryan C
 *  
 *  @Project - Final 
 *  @Professor - Dr. Micah S
 *  @Class - COMP1050
 *  
 *  @Class Description - THhe controller for the StatsPanel.fxml file. This controls and the UI
 */

public class StatsPanelController {

	// Initializes and grabs the control objects from the FXML file. The @FXML
	// reference's the controller's FXML file

	@FXML
	private LineChart<Number, Number> lineChartSIR; // Initializes the lineChart object thats taken from the FXML file
	@FXML
	private PieChart pieChartSIR; // Initializes the pieChart object thats taken from the FXML file
	@FXML
	private NumberAxis xAxis; // Initializes the X Axis that is an Number Axis for the line chart
	@FXML
	private NumberAxis yAxis; // Initializes the Y Axis that is an Number Axis for the line chart

	@FXML // CONSTRUCTOR
	void initialize() {
	}

	// Sets up both the pie chart and the line chart
	public void setUpCharts() {
		// Creates a list that holds the collections for the pie chart, and then assigns
		// the names and values for the collections
		ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
				new PieChart.Data("Susceptible", 0), new PieChart.Data("Infected", 0),
				new PieChart.Data("Recovered", 0));

		// binds the collection to the piechart's data
		pieChartSIR.dataProperty().set(pieChartData);

		// Line Chart
		// creates the x, y axis labels
		xAxis.setLabel("Cycle");
		yAxis.setLabel("Cells");

		// sets the upper bounds for the x, y axis
		xAxis.setUpperBound(500);
		yAxis.setUpperBound(30000);

		// creates the series for Susceptible and then binds the series to the line
		// chart
		XYChart.Series<Number, Number> seriesS = new XYChart.Series<>();
		seriesS.setName("Susceptible");
		lineChartSIR.getData().add(seriesS);

		// creates the series for Infected and then binds the series to the line chart
		XYChart.Series<Number, Number> seriesI = new XYChart.Series<>();
		seriesI.setName("Infected");
		lineChartSIR.getData().add(seriesI);

		// creates the series for Recovered and then binds the series to the line chart
		XYChart.Series<Number, Number> seriesR = new XYChart.Series<>();
		seriesR.setName("Recovered");
		lineChartSIR.getData().add(seriesR);
	}

	public LineChart<Number, Number> getLineChartSIR() {
		return lineChartSIR; // returns the line chart object
	}

	public PieChart getPieChartSIR() {
		return pieChartSIR; // returns the piechart object
	}

	public void clearData() {
		// clears all the data from the piechart and the linechart
		lineChartSIR.getData().clear();
		pieChartSIR.getData().clear();
	}

}
