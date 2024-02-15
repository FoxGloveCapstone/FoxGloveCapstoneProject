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
	private CellManager[] threads;
	private int currentStep;
	private int tickSpeed = 10;
	private Timer timer;
	
	//Some aspect of the UI have to be declared here to be accessible 
	private JLabel fCounter = new JLabel("Frame Counter: ");
	private JLabel speedDisplay = new JLabel("Ticks Per Second: 1");
	private JPanel gridBoard = new JPanel(new GridLayout(10, 10));
	private JTextField rowsField = new JTextField("10");
	private JTextField columnsField = new JTextField("10");

	public static void main(String[] args) {
        Simulation window = new Simulation();
		// TODO: Create main window GUI
        window.setVisible(true); // Open the window, making it visible on the screen.
	}

	public Simulation() {
		super("Game of Life");

		//Sets the panels being used
		JPanel buttonBoard = new JPanel(new FlowLayout());		
		JPanel rules = new JPanel();		
		JSplitPane ruleBoard = new JSplitPane(SwingConstants.HORIZONTAL, rules, buttonBoard);
		Cell.setRuleSet(Rule.getDefaultRuleset());
		//GridLayout gridLayout = new GridLayout();
		
		//gridBoard.add(gridLayout);
		
		//Sets Dimensions on side grid and divider location on the splitpane
		ruleBoard.setDividerLocation(350);
		gridBoard.setPreferredSize(new Dimension(800,900));
		
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
		
		mapGenerateButton.addActionListener(new MapGenerate(this));
		
		playButton.addActionListener(new Play(this));
		pauseButton.addActionListener(new Pause(this));
		nextButton.addActionListener(new Step(this));
		slowButton.addActionListener(new LowerSpeed(this));
		speedButton.addActionListener(new IncreaseSpeed(this));
		clearButton.addActionListener(new Clear(this)); 
		resetButton.addActionListener(new Reset(this));

		
		//Labels for the rules section
		JLabel rowsLabel = new JLabel ("Rows");
		JLabel columnsLabel = new JLabel("Columns");
		JLabel rules1 = new JLabel("--Any live cell with fewer than two live neighbours dies (referred to as underpopulation).");
		JLabel rules2 = new JLabel("--Any live cell with more than three live neighbours dies (referred to as overpopulation).");
		JLabel rules3 = new JLabel("--Any live cell with two or three live neighbours lives, unchanged, to the next generation.");
		JLabel rules4 = new JLabel("--Any dead cell with exactly three live neighbours comes to life.");
		JLabel rules5 = new JLabel("Rules for the Game Of Life");
		JLabel rules6 = new JLabel("*************************************************************************************************");
		JLabel rules7 = new JLabel("--Current version supports Grid generation and timer, not cell logic or simulation.");
		
		
		//Adds rules to ruleboard
		rules.add(rules5);
		rules.add(rules6);
		rules.add(rules1);
		rules.add(rules2);
		rules.add(rules3);
		rules.add(rules4);
		rules.add(rules7);

		
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
		
		//Adds borderlayout and boards to the JFrame
		add(gridBoard, BorderLayout.WEST);
		add(ruleBoard, BorderLayout.CENTER);
		
	
		//Close and Size functions for JFrame
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(new Dimension(1400, 800));
		
		//Timer is implemented for the ticker functionality, default delay of 1 second
		timer = new Timer(1000, new ActionListener() 
		{
            @Override
            public void actionPerformed(ActionEvent e) 
            {
            	step();
            }
        });
	}
	
	/* Simulation Controls */
	//all control methods are called within action listeners, the step method is also called from the timer.
	
	//starts the timer and modifies the speed display
	public void start() 
	{
		timer.start();
		speedDisplay.setText("Ticks Per Second: " + (Float.toString((float)1000/timer.getDelay())));
	}
	
	//stops the timer
	public void pause()
	{
		timer.stop();
	}
	
	//increments the tick by 1
	public void step() 
	{
		currentStep++;
		fCounter.setText("Frame Counter: " + String.valueOf(currentStep));
		
		for(CellManager thread: threads) {
			thread.calculateFrame();
			thread.updateGUI();
		}
	}
	
	// Helper method used to bring the simulation back to its initial state
	private void clearGridBoard() 
	{
		//setVisible(false);
		//gridBoard.removeAll();
		currentStep = 0;
		timer.stop();
		timer.setDelay(1000);
		//setVisible(true);
		fCounter.setText("Frame Counter: " + String.valueOf(currentStep));
		tickSpeed = 10;
		speedDisplay.setText("Ticks Per Second: " + (Float.toString((float)1000/timer.getDelay())));
	}
	
	// Clears the map such that all cells are dead
	public void clear() 
	{
		clearGridBoard();
		Grid.clear();
	}
	
	// Resets the map to its initial state. 
	// This is whatever the user has drawn.
	public void reset() 
	{
		clearGridBoard();
		Grid.reset();
	}
	
	//increases ticker speed
	public void increaseSpeed() 
	{
		tickSpeed--;
		
		//prevents speeds above 10 ticks per second
		if(tickSpeed < 1)
		{
			tickSpeed = 1;
		}
		
		//sets delay value
		if(tickSpeed <= 10)
		{
			timer.setDelay(tickSpeed * 100);
			System.out.println(timer.getDelay());
		}
		else
		{
			timer.setDelay((tickSpeed - 9) * 1000);
			System.out.println(timer.getDelay());
		}
		
		speedDisplay.setText("Ticks Per Second: " + (Float.toString((float)1000/timer.getDelay())));
	}
	
	//decreases ticker speed
	public void decreaseSpeed() 
	{
		tickSpeed++;
		
		//prevents negative speed
		if(tickSpeed > 19)
		{
			tickSpeed = 19;
		}
		
		//sets delay value
		if(tickSpeed <= 10)
		{
			timer.setDelay(tickSpeed * 100);
			System.out.println(timer.getDelay());
		}
		else
		{
			timer.setDelay((tickSpeed - 9) * 1000);
			System.out.println(timer.getDelay());
		}
		
		speedDisplay.setText("Ticks Per Second: " + (Float.toString((float)1000/timer.getDelay())));
	}
	
	//generates map based on user input
	public void mapGenerate()
	{
		String generateRows = rowsField.getText();
		String generateColumns = columnsField.getText();
		try 
		{
			int generateR = Integer.parseInt(generateRows);
			int generateC = Integer.parseInt(generateColumns);
			setVisible(false);
			gridBoard.removeAll();
			Grid.generate(gridBoard, generateR, generateC);
			setVisible(true);
			currentStep = 0;
			timer.stop();
			timer.setDelay(1000);
			fCounter.setText("Frame Counter: " + String.valueOf(currentStep));
			tickSpeed = 10;
			speedDisplay.setText("Ticks Per Second: " + (Float.toString((float)1000/timer.getDelay())));
			
			threads = new CellManager[] { new CellManager() };
		}
		catch(NumberFormatException s)
		{
	           JOptionPane.showMessageDialog(null, "Invalid Input! Please Enter a Number!", 
                       "ERROR", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	
	// Action listeners
	
	public static class MapGenerate implements ActionListener
	{
		private Simulation simulator;
		
		MapGenerate(Simulation sim)
		{
			simulator = sim;
		}
		
		public void actionPerformed(ActionEvent e) 
		{
			simulator.mapGenerate();
			
			System.out.println("MapGenerate");
        }
	}
	
	public static class Play implements ActionListener
	{
		private Simulation simulator;
		
		Play(Simulation sim)
		{
			simulator = sim;
		}
		
		public void actionPerformed(ActionEvent e) 
		{
			System.out.println("Play");
			simulator.start();
        }
	}
	
	public static class Pause implements ActionListener
	{
		private Simulation simulator;
		
		Pause(Simulation sim)
		{
			simulator = sim;
		}
		public void actionPerformed(ActionEvent e) 
		{
			System.out.println("Pause");
			simulator.pause();
        }
	}
	
	public static class Step implements ActionListener
	{
		private Simulation simulator;
		
		Step(Simulation sim)
		{
			simulator = sim;
		}
		public void actionPerformed(ActionEvent e) 
		{
			System.out.println("Step");
			simulator.step();
        }
	}
	
	public static class Reset implements ActionListener
	{
		private Simulation simulator;
		
		Reset(Simulation sim)
		{
			simulator = sim;
		}
		
		public void actionPerformed(ActionEvent e) 
		{	
			simulator.reset();
			System.out.println("Reset");
        }
	}
	public static class Clear implements ActionListener
	{
		private Simulation simulator;
		
		Clear(Simulation sim)
		{
			simulator = sim;
		}
		
		public void actionPerformed(ActionEvent e) 
		{	
			simulator.clear();
			System.out.println("Clear");
        }
	}
	
	
	public static class LowerSpeed implements ActionListener
	{
		private Simulation simulator;
		
		LowerSpeed(Simulation sim)
		{
			simulator = sim;
		}
		
		public void actionPerformed(ActionEvent e) 
		{
			simulator.decreaseSpeed();
			
			System.out.println("lower speed");
        }
	}
	
	public static class IncreaseSpeed implements ActionListener
	{
		private Simulation simulator;
		
		IncreaseSpeed(Simulation sim)
		{
			simulator = sim;
		}
		
		public void actionPerformed(ActionEvent e) 
		{
			simulator.increaseSpeed();
			
			System.out.println("Increase speed");
        }
	}
	
}
