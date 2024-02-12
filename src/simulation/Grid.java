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
	private Cell[][] cells;

	public void generate(JPanel gridPanel, int width, int height) {
		cells = new Cell[width][height];
		gridPanel.setLayout(new GridLayout(width, height));

        for (int x = 0; x < width; x++) {
			for(int y = 0; y < height; y++) {
				// String arguments passed here will label the buttons, 
				// we probably don't want the buttons labeled.
				JButton button = new JButton();
				Cell cell = new Cell(button);
				
				// Sets the buttons size, its important to do this so that the buttons are square shaped
				button.setPreferredSize(new Dimension(30, 30));
				
				// Adds the color manipulation to each cell so when you click it it changes color
				button.addActionListener(new ColorSwitch(button, cell));
				gridPanel.add(button);
			}	
		}
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
	
	public static class ColorSwitch implements ActionListener {
		// This variable might not be necessary anymore, but I'll keep it here for now, just in case.
		public JButton button;
		public Cell cell;
		
		public ColorSwitch(JButton butt, Cell cell) {
			this.button = butt;
			this.cell = cell;
		}

		public void actionPerformed(ActionEvent e) {
			// If ruleset extension is implemented and more colors are added, 
			// we'd probably have a palette of colors to select from. (You'd click red, everything you select turns red).
			// In this scenario, the Grid class would be responsible for knowing which color to set the Cell to
			// and this code will get completely replaced.
            if (cell.getColorState() == ColorState.BLACK) 
                cell.setColorState(ColorState.WHITE);
            else 
                cell.setColorState(ColorState.BLACK);
        }
	}
}
