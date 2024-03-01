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
 * The Grid Class: Acts as a data structure containing the 2D array of Cells. 
 * Since there would only be one grid, making this static would make referencing its methods easier. 
 */

import data.ColorState;
import data.Neighbors;
import rules.RuleSet;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JPanel;

public class Grid {
	private static Cell[][] cells;
	// These are here temporarily, we can change it to a method or readonly
	private static int size;
	// Color to set cells that are clicked.
	private static ColorState currentDrawingColor = ColorState.BLACK;
	// Whether to overwrite cell's initial state. User can still draw on grid, 
	// but their changes will not be saved by reset().
	private static boolean isInDrawingMode = true;
	
	// Viewport camera controls.
	private static int zoomFactor = 5;
	private static int centerX = 5;
	private static int centerY = 5;

	// Used by the 3 different generate methods.
	private static void init(JPanel gridPanel, int size) {
		Grid.size = size;

		centerX = size/2;
		centerY = size/2;
		zoomFactor = size;

		cells = new Cell[size][size];
		gridPanel.setLayout(new GridLayout(size, size));
	}
	
	public static void generate(JPanel gridPanel, int size) {
		init(gridPanel, size);

		// Create cells.
        for (int x = 0; x < size; x++) {
			for(int y = 0; y < size; y++) {
				// String arguments passed here will label the buttons, 
				// we probably don't want the buttons labeled.
				JButton button = new JButton();
				Cell cell = new Cell(button);
				cells[x][y] = cell;

				// Adds the color manipulation to each cell so when you click it it changes color
				button.addActionListener(new ToggleCellColor(cell));
				gridPanel.add(button);
			}	
		}
	}

	public static void generateRandom(JPanel gridPanel, String seed, int size) {
		// Init.
		init(gridPanel, size);
		Random rand = new Random(stringToLong(seed));

		// Get colors that appear in the ruleset.
		ColorState[] colors = RuleSet.colorStatesUsedInRules();			

		// Create cells.
        for (int x = 0; x < size; x++) {
			for(int y = 0; y < size; y++) {
				// String arguments passed here will label the buttons, 
				// we probably don't want the buttons labeled.
				JButton button = new JButton();
				Cell cell = new Cell(button);
				cells[x][y] = cell;

				// Randomly set grid
				cell.setColorState(colors[Math.abs(rand.nextInt()) % colors.length], true);
				
				// Adds the color manipulation to each cell so when you click it it changes color
				button.addActionListener(new ToggleCellColor(cell));
				gridPanel.add(button);
			}	
		}
	}
	
	// Helper method used to convert seed from a string into a long.
	private static long stringToLong(String seed) {
		/*
		 * Long is 8 bytes. char is 1 byte.
		 * - Iterate over chars in string.
		 * - Add first char to first byte of long, second char to second byte, ...
		 * - Cycle: ninth char added to first byte, ...
		 */
		long output = 0;

		int index = 0;
		for(char chara: seed.toCharArray()) {
			// Shift char (index % 8) bytes to the left.
			long num = chara << 8 * (index++ % 8);
			output += num;
		}

		System.out.println("Long seed: " + output);
		return output;
	}
	// Removes buttons and adds them back based on parameters.
	public static void newZoom(JPanel gridPanel, int xChange, int yChange, int zoomChange) {	
		// Alter camera position.
		//scales zoom and pain with viewport size
		if(zoomFactor > 25)
		{
			zoomChange *= 3;
			xChange *= 2;
			yChange *= 2;
		}
		
		if(zoomFactor > 50)
		{
			zoomChange *= 3;
			xChange *= 2;
			yChange *= 2;
		}
		
		if(zoomFactor > 100)
		{
			zoomChange *= 3;
			xChange *= 2;
			yChange *= 2;
		}
		
		zoomFactor += zoomChange;
		centerX += xChange;
		centerY += yChange;
		
		// Ensure center is in bounds.
		if(centerX - (zoomFactor/2) < 0 || centerY - (zoomFactor/2) < 0)
		{	
			zoomFactor -= zoomChange;
			centerX -= xChange;
			centerY -= yChange;
			return;
		}
		if(centerX + (zoomFactor/2) > size || centerY + (zoomFactor/2) > size) {	
			zoomFactor -= zoomChange;
			centerX -= xChange;
			centerY -= yChange;
			return;
		}
		
		// Reset grid gui.
		gridPanel.removeAll();
		gridPanel.setLayout(new GridLayout(zoomFactor, zoomFactor));

		// Viewport boundary.
		int rightBound = centerX + (zoomFactor/2);
		int leftBound = centerX - (zoomFactor/2);
		int topBound = centerY + (zoomFactor/2);
		int lowBound = centerY - (zoomFactor/2);
		
		for (int x = 0; x < size; x++) {
			for(int y = 0; y < size; y++) {
				// Ensure cell is within bounds.
				if(x >= leftBound && x < rightBound && y >= lowBound && y < topBound) {
					// If cell is in bounds, read its button to the gui.
					gridPanel.add(cells[x][y].getButton());
				}
			}	
		}
	}
	
	// If user manually set state of cell, return to that state
	// Else return to starting state
	public static void reset() {
		for(int x = 0; x < size; x++) {
			for(int y = 0; y < size; y++) {
				cells[x][y].resetColorState();
			}
		}
	}
	// Return cells to starting state
	public static void clear() {
		for(int x = 0; x < size; x++) {
			for(int y = 0; y < size; y++) {
				cells[x][y].clearColorState();
			}
		}
	}
	
	public static void setDrawingColor(ColorState col) {
		currentDrawingColor = col;
	}
	public static void setDrawingMode(boolean drawMode) {
		isInDrawingMode = drawMode;
	}
	
	// Collect the cells that neighbor Cell(x,y), ensuring not to go out of bounds.
	// Tally up colorstates of each cell.
	public static Neighbors getNeighborsOf(int x, int y) {
		Neighbors n = new Neighbors();
		Cell cell;

		// Get top-left neighbor 
		if(x - 1 > -1 && y - 1 > -1) {
			cell = Grid.getCellAt(x - 1, y - 1);
			n.add(cell.getColorState());
		}
		// Get top neighbor
		if(y - 1 > -1) {
			cell = Grid.getCellAt(x, y - 1);
			n.add(cell.getColorState());
		}
		// Get top-right neighbor
		if(x + 1 < size && y - 1 > -1) {
			cell = Grid.getCellAt(x + 1, y - 1);
			n.add(cell.getColorState());
		}
		// Get left neighbor
		if(x - 1 > -1) {
			cell = Grid.getCellAt(x - 1, y);
			n.add(cell.getColorState());
		}
		// Get right neighbor
		if(x + 1 < size) {
			cell = Grid.getCellAt(x + 1, y);
			n.add(cell.getColorState());
		}
		// Get bottom-left neighbor
		if(x - 1 > -1 && y + 1 < size) {
			cell = Grid.getCellAt(x - 1, y + 1);
			n.add(cell.getColorState());
		}
		// Get bottom neighbor
		if(y + 1 < size) {
			cell = Grid.getCellAt(x, y + 1);
			n.add(cell.getColorState());
		}
		// Get bottom-right neighbor
		if(x + 1 < size && y + 1 < size) {
			cell = Grid.getCellAt(x + 1, y + 1);
			n.add(cell.getColorState());
		}

		return n;
	}
	
	public static Cell getCellAt(int x, int y) {
		// Caller must guarantee indices are in bounds.
		return cells[x][y];
	}
	public static int getSize() {
		return size;
	}
	
	public static class ToggleCellColor implements ActionListener {
		public Cell cell;
		
		public ToggleCellColor(Cell cell) {
			this.cell = cell;
		}

		public void actionPerformed(ActionEvent e) {
			// Toggle cell color.
            if (cell.getColorState() == Grid.currentDrawingColor) 
                cell.setColorState(ColorState.WHITE, Grid.isInDrawingMode);
            else 
                cell.setColorState(Grid.currentDrawingColor, Grid.isInDrawingMode);
		}
	}
}
