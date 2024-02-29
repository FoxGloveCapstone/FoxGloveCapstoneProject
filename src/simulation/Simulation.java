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
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import data.ColorState;
import gui.RulesListGUI;
import rules.Rule;
import rules.RuleSet;

@SuppressWarnings("serial")
public class Simulation extends JFrame {
	private final int DEFAULT_GRID_SIZE = 20;
	private final int MIN_GRID_SIZE = 10;
	private final int MAX_GRID_SIZE = 10000;
	
	private CellManager[] threads;
	private int currentStep;
	private int tickDelay = 10;
	private Timer timer;
	
	//Some aspect of the UI have to be declared here to be accessible 
	private JLabel frameCounter = new JLabel("Frame Counter: ");
	private JLabel speedDisplay = new JLabel("Ticks Per Second: 1");
	private JPanel gridPanel = new JPanel(new GridLayout(DEFAULT_GRID_SIZE, DEFAULT_GRID_SIZE));
	
	public static void main(String[] args) {
        Simulation window = new Simulation();
        window.setVisible(true); 
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
	
	/* GUI Builder Methods */

	// Initialize instance-level GUI elements and create panels.
	private void buildGUI() {
		// Split pane with rules on top and controls on bottom.
		JSplitPane controlBoard = new JSplitPane(
				SwingConstants.HORIZONTAL, 
				new RulesListGUI(this, RuleSet.asArray()),
				buildButtonPanel());

		//Sets Dimensions on side grid and divider location on the splitpane
		controlBoard.setDividerLocation(350);

		//Adds borderlayout and boards to the JFrame
		add(gridPanel, BorderLayout.WEST);
		add(controlBoard, BorderLayout.CENTER);
	
		gridPanel.setPreferredSize(new Dimension(800,900));
		//Close and Size functions for JFrame
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(new Dimension(1400, 800));
	}
	
	// Create buttons that control simulation.
	private JPanel buildButtonPanel() {
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.PAGE_AXIS));

		/* CREATE SUBPANELS */
		// MapGen controls panel.
		JPanel mapGenPanel = new JPanel();
		buttonPanel.add(mapGenPanel);

		// MAP GENERATION CONTROLS.
		// Get map size from user.
		JLabel mapSizeLabel = new JLabel ("Map Size");
		JSpinner mapSizeField = new JSpinner(
				new SpinnerNumberModel(DEFAULT_GRID_SIZE, MIN_GRID_SIZE, MAX_GRID_SIZE, 1));

		// Get seed for randomly generating map from user.
		JTextField mapSeedField = new JTextField("Seed");
		mapSeedField.setColumns(30);

		// Add buttons to generate map.
		JButton mapGenerateButton = new JButton("Generate Map");
		mapGenerateButton.addActionListener(new MapGenerate(mapSizeField));
		JButton randMapGenerateButton = new JButton("Generate Random Map");
		randMapGenerateButton.addActionListener(new RandomMapGenerate(mapSeedField, mapSizeField));

		// Add map controls to parent.
		mapGenPanel.add(mapSizeLabel);
		mapGenPanel.add(mapSizeField);
		mapGenPanel.add(mapGenerateButton);
		mapGenPanel.add(mapSeedField);
		mapGenPanel.add(randMapGenerateButton);
		
		// SIM CONTROLS
		JPanel simControlsPanel = new JPanel();
		buttonPanel.add(simControlsPanel);

		// Add buttons to control simulation.
		JButton playButton = new JButton("Start");
		playButton.addActionListener(new StartTimer());
		JButton pauseButton = new JButton("Pause");
		pauseButton.addActionListener(new PauseTimer());
		JButton nextButton = new JButton("Next");
		nextButton.addActionListener(new StepTimer());
		JButton clearButton = new JButton("Clear");
		clearButton.addActionListener(new ClearGrid()); 
		JButton resetButton = new JButton("Reset");
		resetButton.addActionListener(new ResetGrid());

		// Adds controls to parent board.
		simControlsPanel.add(playButton);
		simControlsPanel.add(pauseButton);
		simControlsPanel.add(nextButton);
		simControlsPanel.add(clearButton);
		simControlsPanel.add(resetButton);

		// NAV CONTROLS
		JPanel navControls = new JPanel();
		buttonPanel.add(navControls);

		// Controls for moving the viewport/navigating the grid.
		JButton zoomInButton = new JButton("Zoom In");
		zoomInButton.addActionListener(new MoveViewport(0,0,-2));
		JButton zoomOutButton = new JButton("Zoom Out");
		zoomOutButton.addActionListener(new MoveViewport(0,0,2));
		JButton panLeftButton = new JButton("Pan Left");
		panLeftButton.addActionListener(new MoveViewport(0,-2,0));
		JButton panRightButton = new JButton("Pan Right");
		panRightButton.addActionListener(new MoveViewport(0,2,0));
		JButton panUpButton = new JButton("Pan Up");
		panUpButton.addActionListener(new MoveViewport(-2,0,0));
		JButton panDownButton = new JButton("Pan Down");
		panDownButton.addActionListener(new MoveViewport(2,0,0));
		
		// Add navigation/viewport controls to parent.
		navControls.add(panLeftButton);
		navControls.add(panRightButton);
		navControls.add(panUpButton);
		navControls.add(panDownButton);
		navControls.add(zoomInButton);
		navControls.add(zoomOutButton);

		// SPEED CONTROLS
		JPanel speedControls = new JPanel();
		buttonPanel.add(speedControls);

		// Create buttons to increase and decrease sim speed.
		JButton slowButton = new JButton("<-");
		slowButton.addActionListener(new DecreaseTimerSpeed());
		JButton speedButton = new JButton("->");
		speedButton.addActionListener(new IncreaseTimerSpeed());

		// Add speed controls to parent board.
		speedControls.add(slowButton);
		speedControls.add(speedDisplay);
		speedControls.add(speedButton);

		// FRAME COUNTER DISPLAY 
		// Add frame counter to board.
		JPanel fCounterDisplay = new JPanel();
		fCounterDisplay.add(frameCounter);
		buttonPanel.add(fCounterDisplay);

		// DRAW CONTROLS
		JPanel drawControls = new JPanel();
		buttonPanel.add(drawControls);

		// Add color selector to board.
		JPanel colorSelector = buildColorSelector();
		drawControls.add(colorSelector);

		return buttonPanel;
	}
	
	/* Element used to select the current draw color. 
	 * Two color select mode is mostly for debugging.
	 * If it is replaced using extendible ruleset:
	 *	- Remove JToggleButton declaration.
	 *	- Rewrite JLabel label to use alt message.
	 *	- Use parameterless ColorSelector constructor.
	 *	- Remove extra board.add() calls.
	*/
	private JPanel buildColorSelector() {
		JPanel board = new JPanel();
		JLabel label = new JLabel("Drawing color: ");
		
		// Create dropdown panel to select color to draw with.
		JComboBox<ColorState> colorList = new JComboBox<>(ColorState.getAllColorStates());
		colorList.addActionListener(new SelectDrawingColor(colorList));
		// Add elements to parent board
		board.add(label);
		board.add(colorList);
		
		return board;
	}
	
	/* SIMULATION CONTROLS HELPERS */
	
	// Advance to next frame.
	public void step() {
		currentStep++;
		frameCounter.setText("Frame Counter: " + String.valueOf(currentStep));
		
		// For multithreading, this would be broken into two loops.
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
		frameCounter.setText("Frame Counter: " + String.valueOf(currentStep));
		tickDelay = 10;
		speedDisplay.setText("Ticks Per Second: " + (Float.toString((float)1000/timer.getDelay())));
		Grid.setDrawingMode(true);
	}
	// Helper method to create a new grid.
	private void generateMap(int size) {
		// Clear board and regenerate.
		gridPanel.removeAll();
		// Grid.generate(gridBoard, rows, columns);
		Grid.generate(gridPanel, size);
		repaint();
		setVisible(true);
		
		// Reset simulation parameters.
		clearGridBoard();

		// Create new cellmanager(s)
		threads = new CellManager[] { new CellManager() };
	}
	// Helper method to create new grid with random starting state.
	private void generateMap(String seed, int size) {
		// Clear board and regenerate.
		gridPanel.removeAll();
		// Grid.generate(gridBoard, rows, columns);
		Grid.generateRandom(gridPanel, seed, size);
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
		}

		else {
			timer.setDelay((tickDelay - 9) * 1000);
		}

		speedDisplay.setText(String.format("Ticks per Second: %.2f", 1000.0/timer.getDelay()));
	}


	/* ACTION LISTENERS */
	
	/* MAP GENERATION LISTENERS */
	public class MapGenerate implements ActionListener {
		JSpinner mapSizeField;
		public MapGenerate(JSpinner mapSizeField) {
			this.mapSizeField = mapSizeField;
		}

		public void actionPerformed(ActionEvent e) {
			// Get user input from text fields.
			int mapSize = (int)mapSizeField.getValue();
			generateMap(mapSize);
			// Print to console for debugging.
			System.out.println("Map Generate");
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
			int mapSize = (int)mapSizeField.getValue();
			generateMap(seedField.getText(), mapSize); 
			// Print to console for debugging.
			System.out.println("Random Map Generate");
		}
	}

	/* SIMULATION CONTROLS */
	public class StartTimer implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// Initializes timer and speed display. 
			setSpeedDisplay();
			timer.start();

			// Prevents user from overwriting initial state.
			Grid.setDrawingMode(false);

			// Print to debug log.
			System.out.println("Play");
        }
	}
	public class PauseTimer implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			timer.stop();
			System.out.println("Pause");
        }
	}
	// Advance to next frame.
	public class StepTimer implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			step();
			System.out.println("Step");
        }
	}
	public class IncreaseTimerSpeed implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			tickDelay--;

			// Prevents speeds above 10 ticks per second
			if(tickDelay < 1) {
				tickDelay = 1;
			}

			setSpeedDisplay();
			System.out.println("Increase Speed: " + timer.getDelay());
        }
	}
	public class DecreaseTimerSpeed implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			tickDelay++;

			// Prevents negative speed
			if(tickDelay > 19) {
				tickDelay = 19;
			}

			setSpeedDisplay();
			System.out.println("Decrease Speed: " + timer.getDelay());
        }
	}
	// Resets the map to its initial state. 
	// This preserves whatever the user has drawn.
	public class ResetGrid implements ActionListener {
		public void actionPerformed(ActionEvent e) {	
			clearGridBoard();
			Grid.reset();
			System.out.println("Reset");
        }
	}
	// Clears the map to blank state.
	public class ClearGrid implements ActionListener {
		public void actionPerformed(ActionEvent e) {	
			clearGridBoard();
			Grid.clear();
			System.out.println("Clear");
        }
	}
	public class SelectDrawingColor implements ActionListener {
		JComboBox<ColorState> colorSelector;

		public SelectDrawingColor(JComboBox<ColorState> selector) {
			colorSelector = selector;
		}
		
		public void actionPerformed(ActionEvent e) {
			ColorState col = (ColorState)colorSelector.getSelectedItem();
			System.out.println("Drawing color is: " + col);
			Grid.setDrawingColor(col);
			
			repaint();
		}
	}
	public class MoveViewport implements ActionListener {
		int xDelta;
		int yDelta;
		int zDelta;
		
		public MoveViewport(int horizontalMovement, int verticalMovement, int zoomChange)
		{
			xDelta = horizontalMovement;
			yDelta = verticalMovement;
			zDelta = zoomChange;
		}
		
		public void actionPerformed(ActionEvent e) {
			Grid.newZoom(gridPanel, xDelta, yDelta, zDelta);
			repaint();
			setVisible(true);

			// Print to console for debugging.
			System.out.println("Moved Viewport");
		}
	}
}
