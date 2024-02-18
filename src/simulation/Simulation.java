package simulation;

/*UMGC CAPSTONE PROJECT
 * Title:Game of Life in Java By Team Fox Glove:
 *         Anthony Farias
           Mitchell Howard
           Patrick Kamdem
           Hyrum Madson
           Bensaiten Sanchez Flores 
 * 
 *CMSC 495 7380
 *Professor Sanford
 *February 12, 2024 
 *
 *The Simulation class: Controls the flow and execution of the simulation. 
 * This will most likely be the central class. 
 * It controls the timing and the main GUI. 
 */

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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import rules.Rule;

public class Simulation extends JFrame {
	private final int DEFAULT_GRID_SIZE = 10;

	private CellManager[] threads;
	private int currentStep;
	private int tickDelay = 10;
	private Timer timer;
	
	//Some aspect of the UI have to be declared here to be accessible 
	private JLabel fCounter = new JLabel("Frame Counter: ");
	private JLabel speedDisplay = new JLabel("Ticks Per Second: 1");
	private JPanel gridBoard = new JPanel(new GridLayout(DEFAULT_GRID_SIZE, DEFAULT_GRID_SIZE));
	private JTextField rowsField = new JTextField("" + DEFAULT_GRID_SIZE);
	private JTextField columnsField = new JTextField("" + DEFAULT_GRID_SIZE);

	public static void main(String[] args) {
        Simulation window = new Simulation();
		// TODO: Create main window GUI
        window.setVisible(true); // Open the window, making it visible on the screen.
	}

	public Simulation() {
		super("Game of Life");
		Cell.setRuleSet(Rule.getDefaultRuleset());
		buildGUI();

		//Timer is implemented for the ticker functionality, default delay of 1 second
		timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	step();
            }
        });

		// Create a grid to start
		generateMap(DEFAULT_GRID_SIZE, DEFAULT_GRID_SIZE);
	}
	
	/* GUI Builder */
	// Initialize instance-level GUI elements and create panels.
	private void buildGUI() {
		JSplitPane controlBoard = buildControlBoard();

		//Adds borderlayout and boards to the JFrame
		add(gridBoard, BorderLayout.WEST);
		add(controlBoard, BorderLayout.CENTER);
	
		gridBoard.setPreferredSize(new Dimension(800,900));
		//Close and Size functions for JFrame
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(new Dimension(1400, 800));
	}
	// Create buttons that control simulation.
	private JPanel buildButtonBoard() {
		JPanel buttonBoard = new JPanel(new FlowLayout());		

		//Buttons and Textfields for operations
		rowsField.setColumns(10);
		columnsField.setColumns(10);
		JButton mapGenerateButton = new JButton("Generate Map");
		JButton playButton = new JButton("Start");
		JButton pauseButton = new JButton("Pause");
		JButton nextButton = new JButton("Next");
		JButton clearButton = new JButton("Clear");
		JButton resetButton = new JButton("Reset");
		JButton slowButton = new JButton("<-");
		JButton speedButton = new JButton("->");
		
		mapGenerateButton.addActionListener(new MapGenerate());
		
		playButton.addActionListener(new Play());
		pauseButton.addActionListener(new Pause());
		nextButton.addActionListener(new Step());
		slowButton.addActionListener(new DecreaseSpeed());
		speedButton.addActionListener(new IncreaseSpeed());
		clearButton.addActionListener(new Clear()); 
		resetButton.addActionListener(new Reset());

		
		JLabel rowsLabel = new JLabel ("Rows");
		JLabel columnsLabel = new JLabel("Columns");
		//adds Buttons to ButtonBoard
		buttonBoard.add(rowsLabel);
		buttonBoard.add(rowsField);
		buttonBoard.add(columnsLabel);
		buttonBoard.add(columnsField);
		buttonBoard.add(mapGenerateButton);
		buttonBoard.add(playButton);
		buttonBoard.add(pauseButton);
		buttonBoard.add(nextButton);
		buttonBoard.add(clearButton);
		buttonBoard.add(resetButton);
		buttonBoard.add(slowButton);
		buttonBoard.add(speedDisplay);
		buttonBoard.add(speedButton);
		buttonBoard.add(fCounter);
		
		return buttonBoard;
	}
	// Create panel that displays rules.
	// Extendible ruleset would overwrite this method.
	private JPanel buildRulesBoard() {
		JPanel rules = new JPanel();		

		//Labels for the rules section
		JLabel rules1 = new JLabel("--Any live cell with fewer than two live neighbours dies (referred to as underpopulation).");
		JLabel rules2 = new JLabel("--Any live cell with more than three live neighbours dies (referred to as overpopulation).");
		JLabel rules3 = new JLabel("--Any live cell with two or three live neighbours lives, unchanged, to the next generation.");
		JLabel rules4 = new JLabel("--Any dead cell with exactly three live neighbours comes to life.");
		JLabel rules5 = new JLabel("Rules for the Game Of Life");
		JLabel rules6 = new JLabel("*************************************************************************************************");
		
		
		//Adds rules to ruleboard
		rules.add(rules5);
		rules.add(rules6);
		rules.add(rules1);
		rules.add(rules2);
		rules.add(rules3);
		rules.add(rules4);

		return rules;
	}
	// Panel that holds rules and controls.
	private JSplitPane buildControlBoard() {
		// Create panel that displays the rules.
		JPanel rulesBoard = buildRulesBoard();		
		// Create panel to hold simulation control buttons.
		JPanel buttonBoard = buildButtonBoard();

		// Output panel that contains both.
		JSplitPane controlBoard = new JSplitPane(SwingConstants.HORIZONTAL, rulesBoard, buttonBoard);

		//Sets Dimensions on side grid and divider location on the splitpane
		controlBoard.setDividerLocation(350);
		
		return controlBoard;
	}
	
	/* Simulation Controls Helpers */ 

	// Advance to next frame.
	public void step() {
		currentStep++;
		fCounter.setText("Frame Counter: " + String.valueOf(currentStep));
		
		for(CellManager thread: threads) {
			thread.calculateFrame();
			thread.updateGUI();
		}
	}
	// Helper method used to bring the simulation back to its initial state.
	private void clearGridBoard() {
		currentStep = 0;
		timer.stop();
		timer.setDelay(1000);
		fCounter.setText("Frame Counter: " + String.valueOf(currentStep));
		tickDelay = 10;
		speedDisplay.setText("Ticks Per Second: " + (Float.toString((float)1000/timer.getDelay())));
	}
	// Helper method to create a new grid.
	private void generateMap(int rows, int columns) {
		// Clear board and regenerate.
		gridBoard.removeAll();
		Grid.generate(gridBoard, rows, columns);
		setVisible(true);
		
		// Reset simulation parameters.
		clearGridBoard();

		// Create new cellmanager(s)
		threads = new CellManager[] { new CellManager() };
	}
	// Helper method to set and format speed display.
	private void setSpeedDisplay() {
		// Sets delay value
		if(tickDelay <= 10) {
			timer.setDelay(tickDelay * 100);
			System.out.println(timer.getDelay());
		}

		else {
			timer.setDelay((tickDelay - 9) * 1000);
			System.out.println(timer.getDelay());
		}

		speedDisplay.setText(String.format("Ticks per Second: %.2f", 1000.0/timer.getDelay()));
	}

	/* Simulation Controls Action Listeners */
	
	// Parse user input for number of rows and columns.
	// Then create new grid.
	public class MapGenerate implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// Get user input from text fields.
			String generateRows = rowsField.getText();
			String generateColumns = columnsField.getText();
			try {
				// Parse input to int. 
				int generateR = Integer.parseInt(generateRows);
				int generateC = Integer.parseInt(generateColumns);

				generateMap(generateR, generateC);
			}
			catch(Exception s) {
				JOptionPane.showMessageDialog(null, "Invalid Input! Please Enter a positive number greater than 0!", 
						"ERROR", JOptionPane.ERROR_MESSAGE);
			}
			// Print to console for debugging.
			System.out.println("MapGenerate");
        }
	}
	// Starts the timer and modifies the speed display.
	public class Play implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			setSpeedDisplay();
			timer.start();
			System.out.println("Play");
        }
	}
	// Stops the timer.
	public class Pause implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			timer.stop();
			System.out.println("Pause");
        }
	}
	// Advance to next frame.
	public class Step implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			System.out.println("Step");
			step();
        }
	}
	// Resets the map to its initial state. 
	// This preserves whatever the user has drawn.
	public class Reset implements ActionListener {
		public void actionPerformed(ActionEvent e) {	
			clearGridBoard();
			Grid.reset();
			System.out.println("Reset");
        }
	}
	// Clears the map such that all cells are dead
	public class Clear implements ActionListener {
		public void actionPerformed(ActionEvent e) {	
			clearGridBoard();
			Grid.clear();
			System.out.println("Clear");
        }
	}
	// Increases ticker speed
	public class IncreaseSpeed implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			tickDelay--;

			//prevents speeds above 10 ticks per second
			if(tickDelay < 1) {
				tickDelay = 1;
			}

			setSpeedDisplay();
			System.out.println("lower speed");
        }
	}
	
	// Decreases ticker speed
	public class DecreaseSpeed implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			tickDelay++;

			//prevents negative speed
			if(tickDelay > 19) {
				tickDelay = 19;
			}

			setSpeedDisplay();
			System.out.println("Increase speed");
        }
	}
}
