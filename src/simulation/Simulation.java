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

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import data.ColorState;
import data.RuleSet;
import gui.RuleGUI;
import gui.RulesListGUI;
import rules.Rule;

public class Simulation extends JFrame {
	private final int DEFAULT_GRID_SIZE = 20;
	private final int MAX_GRID_SIZE = 10000;
	
	private CellManager[] threads;
	private int currentStep;
	private int tickDelay = 10;
	private Timer timer;
	
	//Some aspect of the UI have to be declared here to be accessible 
	private JLabel fCounter = new JLabel("Frame Counter: ");
	private JLabel speedDisplay = new JLabel("Ticks Per Second: 1");
	private JPanel gridBoard = new JPanel(new GridLayout(DEFAULT_GRID_SIZE, DEFAULT_GRID_SIZE));
	
	
	//private JTextField zoomXField = new JTextField("");
	//private JTextField zoomYField = new JTextField("");
	//private JTextField centerXField = new JTextField("");
	//private JTextField centerYField = new JTextField("");

	private RulesListGUI rulesBoard;
	
	public static void main(String[] args) {
        Simulation window = new Simulation();
        window.setVisible(true); // Open the window, making it visible on the screen.
	}

	public Simulation() {
		super("Game of Life");
		RuleSet.setRuleSet(Rule.getDefaultRuleset());
		buildGUI();

		//Timer is implemented for the ticker functionality, default delay of 1 second
		timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	step();
            }
        });

		// Create a grid to start
		generateMap(DEFAULT_GRID_SIZE);
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

	// Panel that holds rules and controls.
	private JSplitPane buildControlBoard() {
		// Create panel that displays the rules.
		// JPanel rulesBoard = buildRulesBoard();		
		rulesBoard = new RulesListGUI(this, RuleSet.asArray());
		// Create panel to hold simulation control buttons.
		JPanel buttonBoard = buildButtonBoard();

		// Output panel that contains both.
		JSplitPane controlBoard = new JSplitPane(SwingConstants.HORIZONTAL, rulesBoard, buttonBoard);

		//Sets Dimensions on side grid and divider location on the splitpane
		controlBoard.setDividerLocation(350);
		
		return controlBoard;
	}
	
	// Create buttons that control simulation.
	private JPanel buildButtonBoard() {
		JPanel buttonBoard = new JPanel();
		buttonBoard.setLayout(new BoxLayout(buttonBoard, BoxLayout.PAGE_AXIS));

		JPanel mapControls = new JPanel();
		JPanel simControls = new JPanel();
		JPanel speedControls = new JPanel();
		JPanel drawControls = new JPanel();
		JPanel navControls = new JPanel();
		JSpinner mapSizeField = new JSpinner(new SpinnerNumberModel(DEFAULT_GRID_SIZE, 1, MAX_GRID_SIZE, 1));

		// Wrapper panel for frame counter
		JPanel fCounterDisplay = new JPanel();
		fCounterDisplay.add(fCounter);
		
		// Add subpanels to main board
		buttonBoard.add(mapControls);
		buttonBoard.add(simControls);
		buttonBoard.add(navControls);
		buttonBoard.add(speedControls);
		buttonBoard.add(fCounterDisplay);
		buttonBoard.add(drawControls);

		// Elements for sim controls
		JLabel rowsLabel = new JLabel ("Map Size");
		
		JButton mapGenerateButton = new JButton("Generate Map");
		JButton randMapGenerateButton = new JButton("Generate Random Map");
		JTextField seedField = new JTextField("Seed");
		seedField.setColumns(30);

		JButton playButton = new JButton("Start");
		JButton pauseButton = new JButton("Pause");
		JButton nextButton = new JButton("Next");
		JButton clearButton = new JButton("Clear");
		JButton resetButton = new JButton("Reset");
		JButton slowButton = new JButton("<-");
		JButton speedButton = new JButton("->");
		
		JButton zoomButton = new JButton("testZoom");
		
		JButton zoomInButton = new JButton("zoom in");
		JButton zoomOutButton = new JButton("zoom out");
		JButton panLeftButton = new JButton("pan left");
		JButton panRightButton = new JButton("pan right");
		JButton panUpButton = new JButton("pan up");
		JButton panDownButton = new JButton("pan down");

		
		// Attach actionlisteners to sim controls
		mapGenerateButton.addActionListener(new MapGenerate(mapSizeField));
		randMapGenerateButton.addActionListener(new RandomMapGenerate(seedField, mapSizeField));
		playButton.addActionListener(new Play());
		pauseButton.addActionListener(new Pause());
		nextButton.addActionListener(new Step());
		slowButton.addActionListener(new DecreaseSpeed());
		speedButton.addActionListener(new IncreaseSpeed());
		clearButton.addActionListener(new Clear()); 
		resetButton.addActionListener(new Reset());
		//zoomButton.addActionListener(new Zoom());
		
		zoomInButton.addActionListener(new Zoom(0,0,-2));
		zoomOutButton.addActionListener(new Zoom(0,0,2));
		panLeftButton.addActionListener(new Zoom(0,-2,0));
		panRightButton.addActionListener(new Zoom(0,2,0));
		panUpButton.addActionListener(new Zoom(-2,0,0));
		panDownButton.addActionListener(new Zoom(2,0,0));

		
		// Adds controls to parent board.
		simControls.add(playButton);
		simControls.add(pauseButton);
		simControls.add(nextButton);
		simControls.add(clearButton);
		simControls.add(resetButton);

		// Add map controls to parent.
		mapControls.add(rowsLabel);
		mapControls.add(mapSizeField);
		mapControls.add(mapGenerateButton);
		mapControls.add(seedField);
		mapControls.add(randMapGenerateButton);
		
		// Add speed controls to parent board.
		speedControls.add(slowButton);
		speedControls.add(speedDisplay);
		speedControls.add(speedButton);
		
		//Navigation (zoom) controls		
		navControls.add(panLeftButton);
		navControls.add(panRightButton);
		navControls.add(panUpButton);
		navControls.add(panDownButton);
		navControls.add(zoomInButton);
		navControls.add(zoomOutButton);
		
		// Add rule selection elements to board 
		JPanel colorSelector = buildColorSelector();
		drawControls.add(colorSelector);
		return buttonBoard;
	}
	
	// Element used to select the current draw color. 
	// Two color select mode is mostly for debugging.
	// If it is replaced using extendible ruleset:
	//	- Remove JToggleButton declaration.
	//	- Rewrite JLabel label to use alt message.
	//	- Use parameterless ColorSelector constructor.
	//	- Remove extra board.add() calls.
	private JPanel buildColorSelector() {
		JPanel board = new JPanel();
		JLabel label = new JLabel("Drawing color: ");
		
		// Create dropdown panel to select color to draw with.
		JComboBox<ColorState> colorList = new JComboBox<>(ColorState.getAllColorStates());
		colorList.addActionListener(new ColorSelector(colorList));
		// Add elements to parent board
		board.add(label);
		board.add(colorList);
		
		return board;
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
		Grid.setDrawingMode(true);
	}
	// Helper method to create a new grid.
	private void generateMap(int size) {
		// Clear board and regenerate.
		gridBoard.removeAll();
		// Grid.generate(gridBoard, rows, columns);
		Grid.generate(gridBoard, size);
		repaint();
		setVisible(true);
		
		// Reset simulation parameters.
		clearGridBoard();

		// Create new cellmanager(s)
		threads = new CellManager[] { new CellManager() };
	}
	private void generateMap(String seed, int size) {
		// Clear board and regenerate.
		gridBoard.removeAll();
		// Grid.generate(gridBoard, rows, columns);
		Grid.generateRandom(gridBoard, seed, size);
		repaint();
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
		JSpinner mapSizeField;
		public MapGenerate(JSpinner mapSizeField) {
			this.mapSizeField = mapSizeField;
		}

		public void actionPerformed(ActionEvent e) {
			// Get user input from text fields.
			try {
				// Parse input to int. 
				int mapSize = (int)mapSizeField.getValue();

				generateMap(mapSize);
			}
			catch(Exception s) {
				JOptionPane.showMessageDialog(null, "Invalid Input! Please Enter a positive number greater than 0!", 
						"ERROR", JOptionPane.ERROR_MESSAGE);
			}
			// Print to console for debugging.
			System.out.println("MapGenerate");
        }
	}
	public class RandomMapGenerate implements ActionListener {
		private JTextField seedField;
		private JSpinner mapSizeField;

		public RandomMapGenerate(JTextField seedField, JSpinner mapSizeField) {
			this.mapSizeField = mapSizeField;
			this.seedField = seedField;
		}

		public void actionPerformed(ActionEvent e) {
			try {
				// Parse input to int. 
				int mapSize = (int)mapSizeField.getValue();
				generateMap(seedField.getText(), mapSize); 
			}
			catch(Exception s) {
				JOptionPane.showMessageDialog(null, "Invalid Input! Please Enter a positive number greater than 0!", 
						"ERROR", JOptionPane.ERROR_MESSAGE);
			}
			// Print to console for debugging.
			System.out.println("Random MapGenerate");
		}
	}
	// Starts the timer and modifies the speed display.
	public class Play implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			setSpeedDisplay();
			timer.start();
			System.out.println("Play");
			Grid.setDrawingMode(false);
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
			System.out.println("Increase Speed");
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
			System.out.println("Decrease Speed");
        }
	}

	// Used to select which color to draw with.
	public class ColorSelector implements ActionListener {
		JComboBox<ColorState> colorSelector;
		public ColorSelector(JComboBox<ColorState> selector) {
			colorSelector = selector;
		}
		
		public void actionPerformed(ActionEvent e) {
			ColorState col = (ColorState)colorSelector.getSelectedItem();
			System.out.println("Drawing color is: " + col);
			Grid.setDrawingColor(col);
			
			repaint();
		}
	}
	
	//checks text fields to zoom into the board
	public class Zoom implements ActionListener 
	{
		int xDelta;
		int yDelta;
		int zDelta;
		
		public Zoom(int horizontalMovement, int verticalMovement, int zoomChange)
		{
			xDelta = horizontalMovement;
			yDelta = verticalMovement;
			zDelta = zoomChange;
		}
		
		public void actionPerformed(ActionEvent e) {
			// Get user input from text fields.
				
			Grid.newZoom(gridBoard, xDelta, yDelta, zDelta);
			repaint();
			setVisible(true);

			// Print to console for debugging.
			System.out.println("zoooom");
		}
	}
}
