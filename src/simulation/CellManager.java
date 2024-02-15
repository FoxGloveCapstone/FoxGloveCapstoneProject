package simulation;

/*UMGC CAPSTONE PROJECT
* Title:Game of Life in Java By Team Fox Glove:
*         Anthony Farias
          Mitchell Howard
          Patrick Kamdem
          Hyrum Madson
          Bensaiten Sanchez Flores 
          
*CMSC 495 7380
*Professor Sanford
*February 12, 2024 
*
* CellManager class: it iterates over a set of cells during each pass. 
* If we add multithreading, each CellManager would get its own thread and some number of rows to manage. 
*/

import data.Neighbors;

public class CellManager {
	private int rowOffset;
	private int rowCount;

	public CellManager() {
		rowOffset = 0;
		this.rowCount = Grid.height;
	}
	
	public CellManager(int rowCount, int rowOffset) {
		this.rowCount = rowCount;
		this.rowOffset = rowOffset;
	}
	
	public void calculateFrame() {
		// Calculation pass.
		for(int x = 0; x < Grid.width; x++) {
			for(int y = rowOffset; y < rowCount; y++) {
				Cell currentCell = Grid.getCellAt(x, y);
				Neighbors neighbors = Grid.getNeighborsOf(x, y);
				currentCell.calculateNextStep(neighbors);
			}
		}		
	}
	
	public void updateGUI() {
		// Tell cells to change colors.
		for(int x = 0; x < Grid.width; x++) {
			for(int y = rowOffset; y < rowCount; y++) {
				Cell currentCell = Grid.getCellAt(x, y);
				currentCell.updateGUI();
			}
		}	
	}
}
