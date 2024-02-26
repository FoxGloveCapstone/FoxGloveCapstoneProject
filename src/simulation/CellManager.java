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
		this.rowCount = Grid.getSize();
	}
	
	public CellManager(int rowCount, int rowOffset) {
		this.rowCount = rowCount;
		this.rowOffset = rowOffset;
	}
	
	// Calculation pass.
	// Tell cells to calculate their state in the next frame.
	public void calculateFrame() {
		for(int x = 0; x < Grid.getSize(); x++) {
			for(int y = rowOffset; y < rowCount; y++) {
				Cell currentCell = Grid.getCellAt(x, y);
				Neighbors neighbors = Grid.getNeighborsOf(x, y);
				currentCell.calculateNextStep(neighbors);
			}
		}		
	}
	
	// Update pass.
	// Tell cells to change colors.
	public void updateGUI() {
		for(int x = 0; x < Grid.getSize(); x++) {
			for(int y = rowOffset; y < rowCount; y++) {
				Cell currentCell = Grid.getCellAt(x, y);
				currentCell.updateGUI();
			}
		}	
	}
}
