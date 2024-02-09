package simulation;

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

/*
 * Controls the flow and execution of the simulation. This will most likely be the central class. 
 * Controls the timing and the main GUI. 
 */
public class Simulation extends JFrame {
	private CellManager[] threads;
	private int currentStep;

	public static void main(String[] args) {
        Simulation window = new Simulation();
		// TODO: Create main window GUI
        window.setVisible(true); // Open the window, making it visible on the screen.
	}

	public Simulation() {
		super("Game of Life");

		//Sets the panels being used
		JPanel buttonBoard = new JPanel(new FlowLayout());
		JPanel gridBoard = new JPanel(new GridLayout(10, 10));		
		JPanel rules = new JPanel();		
		JSplitPane ruleBoard = new JSplitPane(SwingConstants.HORIZONTAL, rules, buttonBoard);
		//GridLayout gridLayout = new GridLayout();
		
		//gridBoard.add(gridLayout);
		
		//Sets Dimensions on side grid and divider location on the splitpane
		ruleBoard.setDividerLocation(350);
		gridBoard.setPreferredSize(new Dimension(800,900));
		
		//Buttons and Textfields for operations
		JTextField rowsField = new JTextField();
		JTextField columnsField = new JTextField();
		rowsField.setColumns(10);
		columnsField.setColumns(10);
		JButton mapGenerateButton = new JButton("Generate Map");
		JButton playButton = new JButton("Start");
		JButton pauseButton = new JButton("Pause");
		JButton nextButton = new JButton("Next");
		JButton clearButton = new JButton("Clear");
		
		mapGenerateButton.addActionListener(new Mapgenerate());
		playButton.addActionListener(new Play());
		pauseButton.addActionListener(new Pause());
		nextButton.addActionListener(new Step());
		clearButton.addActionListener(new Reset());
		
		//Labels for the rules section
		JLabel rowsLabel = new JLabel ("Rows");
		JLabel columnsLabel = new JLabel("Columns");
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
		
		//Adds borderlayout and boards to the JFrame
		add(gridBoard, BorderLayout.WEST);
		add(ruleBoard, BorderLayout.CENTER);
		
		
		Grid grid = new Grid();
		grid.generate(gridBoard, 50, 50);
		
		
		//Close and Size functions for JFrame
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(new Dimension(1400, 800));
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
	
	public static class Mapgenerate implements ActionListener
	{
		public void actionPerformed(ActionEvent e) 
		{
			System.out.println("Mapgenerate");
        }
	}
	
	public static class Play implements ActionListener
	{
		public void actionPerformed(ActionEvent e) 
		{
			System.out.println("Play");
        }
	}
	
	public static class Pause implements ActionListener
	{
		public void actionPerformed(ActionEvent e) 
		{
			System.out.println("Pause");
        }
	}
	
	public static class Step implements ActionListener
	{
		public void actionPerformed(ActionEvent e) 
		{
			System.out.println("Step");
        }
	}
	
	public static class Reset implements ActionListener
	{
		public void actionPerformed(ActionEvent e) 
		{
			System.out.println("Reset");
        }
	}
	
}
