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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class Grid {
	private static Cell[][] cells;
	// These are here temporarily, we can change it to a method or readonly
	public static int width;
	public static int height;
	private static ColorState currentDrawingColor = ColorState.BLACK;
	
	public static void generate(JPanel gridPanel, int width, int height) {
		Grid.width = width;
		Grid.height = height;

		cells = new Cell[width][height];
		gridPanel.setLayout(new GridLayout(width, height));

        for (int x = 0; x < width; x++) {
			for(int y = 0; y < height; y++) {
				// String arguments passed here will label the buttons, 
				// we probably don't want the buttons labeled.
				JButton button = new JButton();
				Cell cell = new Cell(button);
				cells[x][y] = cell;
				
				// Sets the buttons size, its important to do this so that the buttons are square shaped
				button.setPreferredSize(new Dimension(30, 30));
				
				// Adds the color manipulation to each cell so when you click it it changes color
				button.addActionListener(new ColorSwitch(button, cell));
				gridPanel.add(button);
			}	
		}
	}
	// If user manually set state of cell, return to that state
	// Else return to starting state
	public static void reset() {
		for(int x = 0; x < width; x++) {
			for(int y = 0; y < height; y++) {
				cells[x][y].resetColorState();
			}
		}
	}
	// Return cells to starting state
	public static void clear() {
		for(int x = 0; x < width; x++) {
			for(int y = 0; y < height; y++) {
				cells[x][y].clearColorState();
			}
		}
	}
	
	// Set drawing color
	public static void setDrawingColor(ColorState col) {
		currentDrawingColor = col;
	}
	
	public static void generateRandom(String seed, int width, int height) {
		
	}
	
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
		if(x + 1 < width && y - 1 > -1) {
			cell = Grid.getCellAt(x + 1, y - 1);
			n.add(cell.getColorState());
		}
		// Get left neighbor
		if(x - 1 > -1) {
			cell = Grid.getCellAt(x - 1, y);
			n.add(cell.getColorState());
		}
		// Get right neighbor
		if(x + 1 < width) {
			cell = Grid.getCellAt(x + 1, y);
			n.add(cell.getColorState());
		}
		// Get bottom-left neighbor
		if(x - 1 > -1 && y + 1 < height) {
			cell = Grid.getCellAt(x - 1, y + 1);
			n.add(cell.getColorState());
		}
		// Get bottom neighbor
		if(y + 1 < height) {
			cell = Grid.getCellAt(x, y + 1);
			n.add(cell.getColorState());
		}
		// Get bottom-right neighbor
		if(x + 1 < width && y + 1 < height) {
			cell = Grid.getCellAt(x + 1, y + 1);
			n.add(cell.getColorState());
		}


		return n;
	}
	
	public static Cell getCellAt(int x, int y) {
		// Caller must guarantee indices are in bounds.
		return cells[x][y];
	}
	
	public static class ColorSwitch implements ActionListener {
		// This variable might not be necessary anymore, but I'll keep it here for now, just in case.
		public JButton button;
		public Cell cell;
		
		public ColorSwitch(JButton butt, Cell cell) {
			this.button = butt;
			this.cell = cell;
		}

		public void actionPerformed(ActionEvent e) {
			// If/when extendible rulesets are implemented, this will be the only necessary line.
			//cell.setColorState(currentDrawingColor);
			
			// Otherwise, use normal toggle mode
            if (cell.getColorState() == Grid.currentDrawingColor) 
                cell.setColorState(ColorState.WHITE);
            else 
                cell.setColorState(Grid.currentDrawingColor);
		}
	}
}
