package simulation;

import javax.swing.JFrame;
import javax.swing.JPanel;

/*
 * Controls the flow and execution of the simulation. This will most likely be the central class. 
 * Controls the timing and the main GUI. 
 */
public class Simulation extends JPanel {
	private CellManager[] threads;
	private int currentStep;

	public static void main(String[] args) {
		// Hyrum: This is the boilerplate usually given by the professors when doing a graphic project.
        JFrame window;
        window = new JFrame("Conway's Game of Life");  // The parameter shows in the window title bar.
        Simulation panel = new Simulation();
        window.setSize(1000, 1000);
        window.setContentPane(panel); // Show the panel in the window.
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // End program when window closes.
        window.pack();  // Set window size based on the preferred sizes of its contents.

        // TODO: Create main window GUI
        window.setVisible(true); // Open the window, making it visible on the screen.
	}

	/* Simulation Controls */
	
	public void start() {
		
	}
	public void pause() {
		
	}
	public void step() {
		
	}
	public void reset() {
		
	}
	public void clear() {
		
	}
}
