package CSMC495;

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



@SuppressWarnings("serial")
public class LifeGUI extends JFrame {
	
	LifeGUI(){
	
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
		
        for (int i = 0; i < 100; i++) 
        {
        	//String arguments passed here will label the buttons, we probably don't want the buttons labeled
            JButton button = new JButton();
            
            //Sets the buttons size, its important to do this so that the buttons are square shaped
            button.setPreferredSize(new Dimension(30,30));
            
            //Unneeded code that makes the cells alternate between black and white, mostly done to demonstrate color manipulation
           // if(i % 2 <= 0)
            	button.setBackground(Color.WHITE);
           // else    
            	//button.setBackground(Color.BLACK);
            
            //adds the color manipulation to each cell so when you click it it changes color
            button.addActionListener(new Colorswitch(button));
            gridBoard.add(button);
        }
		
		
		
		
		
		//Close and Size functions for JFrame
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(new Dimension(1400, 800));
	}
	
	public static void main(String[] args) {
		
		//Starts the Simulation
		new LifeGUI().setVisible(true);
	}
	
	public static class Colorswitch implements ActionListener
	{
		public JButton button;
		
		public Colorswitch(JButton butt) 
		{
			this.button = butt;
		}

		public void actionPerformed(ActionEvent e) 
		{
			//switches the color when you click it
            if (button.getBackground() == Color.BLACK) 
                button.setBackground(Color.WHITE);
            else 
                button.setBackground(Color.BLACK);
        }
	}
	
}
