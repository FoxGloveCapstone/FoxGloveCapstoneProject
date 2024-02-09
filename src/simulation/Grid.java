package simulation;

import data.Neighbors;

/*
 * Acts as a data structure containing the 2D array of Cells. 
 * Since there would only be one grid, making this static would make referencing its methods easier. 
 */
public class Grid {
	private final 
	private Cell[][] cells;
	
	public void generate(JPanel gridPanel, int width, int height) {
		cells = new Cell[width][height];
		frame.setLayout(new GridLayout(width, height));

        for (int x = 0; x < width; x++) {
			for(int y = 0; y < height; y++) {
				//String arguments passed here will label the buttons, 
				//we probably don't want the buttons labeled
				JButton button = new JButton();
				//Sets the buttons size, its important to do this so that the buttons are square shaped
				button.setPreferredSize(new Dimension(30,30));
				// Set the cell's state to dead
				button.setBackground(Color.WHITE);
			}
			//adds the color manipulation to each cell so when you click it it changes color
			button.addActionListener(new Colorswitch(button));
			gridBoard.add(button);
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
}
