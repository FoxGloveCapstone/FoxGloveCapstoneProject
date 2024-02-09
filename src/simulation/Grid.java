package simulation;

import data.Neighbors;

/*
 * Acts as a data structure containing the 2D array of Cells. 
 * Since there would only be one grid, making this static would make referencing its methods easier. 
 */
public class Grid {
	private Cell[][] cells;
	
	public void generate(int width, int height) {
		
	}
	
	public void generateRandom(String seed, int width, int height) {
		
	}
	
	public Neighbors getNeighborsOf(int x, int y) {
		return null;
	}
	
	public Cell getCellAt(int x, int y) {
		// Caller must guarantee indices are in bounds.
		return cells[x][y];
	}
}
