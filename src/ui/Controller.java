package ui;

import java.net.URL;
import java.util.ResourceBundle;

import cell.Cell;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Controller implements Initializable {
	
	@FXML private GridPane grid;
	
	private int GRIDDEMENSIONS_X = 100;
	private int GRIDDEMENSIONS_Y = 75;
	private int CELLDEMENSIONS = 10;
	
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        // TODO Auto-generated method stub
    	
    	for(int i = 0; i < GRIDDEMENSIONS_X; i++) {
    		for(int k = 0; k < GRIDDEMENSIONS_Y; k++) {
    			if((i + (k % 2)) % 2 == 0) {
    				grid.add(new Cell(i,k, Color.YELLOW, 0, CELLDEMENSIONS, CELLDEMENSIONS).getCell(), i, k);
    			}
    			else {
    				grid.add(new Cell(i,k, Color.BLUE, 0, CELLDEMENSIONS, CELLDEMENSIONS).getCell(), i, k);
    			}
    		}
    	}
    	
    }
}
