package gui;
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
 * Displays all rules that are currently active.
 * Allows the user to move, add, and remove rules.
 */

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import rules.Rule;
import rules.RuleSet;

@SuppressWarnings("serial")
public class RulesListGUI extends JPanel {
	private JScrollPane scrollPane;
	private JPanel rulesList;
	private ArrayList<RuleGUI> ruleElements;
	static JTextField rulesField = new JTextField("");

	// Holds list of radio buttons connected to RuleGUI objects.
	// This way, only one rule can be selected for editing at a time.
	private ButtonGroup selectButtons;

	public RulesListGUI(JFrame frame, Rule[] rules) {
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		rulesList = new JPanel();
		rulesList.setLayout(new BoxLayout(rulesList, BoxLayout.PAGE_AXIS));

		// Add list of rules to scroll pane.
		scrollPane = new JScrollPane(rulesList);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		add(scrollPane);

		// Create list of rules and generate their construct string.
		buildRulesList(rules);
		rulesField.setText(createConstructString());

		// Add editing buttons at bottom.
		JPanel buttonsPanel = new JPanel();
		JButton moveUpButton = new JButton("Move Rule Up");
		JButton moveDownButton = new JButton("Move Rule Down");
		JButton newButton = new JButton("New Rule");
		JButton removeButton = new JButton("Remove Rule");
		JButton revertButton = new JButton("Revert to Default Ruleset");

		JPanel generateMapPanel = new JPanel();
		JButton printButton = new JButton("Print Ruleset String");
		JButton generateButton = new JButton("Generate Ruleset from String");
		rulesField.setColumns(10);

		add(buttonsPanel);
		add(generateMapPanel);
		
		// Add editing buttons to their panel.
		buttonsPanel.add(moveUpButton);
		buttonsPanel.add(moveDownButton);
		buttonsPanel.add(newButton);
		buttonsPanel.add(removeButton);
		buttonsPanel.add(revertButton);

		// Add rule seed panel.
		generateMapPanel.add(rulesField);
		generateMapPanel.add(printButton);
		generateMapPanel.add(generateButton);
	
		// Add action listeners to buttons.
		moveUpButton.addActionListener(new MoveUpRule());
		moveDownButton.addActionListener(new MoveDownRule());
		newButton.addActionListener(new NewRule(frame));
		removeButton.addActionListener(new RemoveRule());
		revertButton.addActionListener(new RevertToDefaults());
		printButton.addActionListener(new PrintRules());
		generateButton.addActionListener(new GenerateRules());
	}
	
	public RulesListGUI(JFrame frame) {
		new RulesListGUI(frame, RuleSet.asArray());
	}
	/* Creates GUI element 
	 * Assumes rulesList is already initialized.
	 */
	private void buildRulesList(Rule[] rules) {
		ruleElements = new ArrayList<>();

		selectButtons = new ButtonGroup();

		RuleGUI element;
		for(Rule rule: rules) {
			element = new RuleGUI(rule);
			selectButtons.add(element.getSelectButton());
			rulesList.add(element);
			ruleElements.add(element);
		}

		scrollPane.setViewportView(rulesList);
		repaint();
	}

	/*
	 * Automatically get list of rules from Cell class and recreate GUI.
	 */
	public void refresh() {
		rulesList.removeAll();
		buildRulesList(RuleSet.asArray());
	}
	
	private int getActiveButtonIndex() {
		for (int i = 0; i < ruleElements.size(); i++) {
			RuleGUI gui = ruleElements.get(i);
			if(gui.isSelected()) {
				return i;
			}
		}
		return -1;
	}
	
	// Helper method to delete everything related to a rule.
	private void removeRule(int index) {
		RuleSet.remove(index);
		rulesList.remove(index);
		ruleElements.remove(index);
	}
	
	// Helper method to change the position of a rule.
	private void moveRule(int oldIndex, int newIndex) {
		RuleSet.moveRule(oldIndex, newIndex);
		
		RuleGUI gui = ruleElements.remove(oldIndex);
		ruleElements.add(newIndex, gui);

		rulesList.remove(oldIndex);
		rulesList.add(gui, newIndex);
	}

	// Helper method to generate ruleset construct string.
	private String createConstructString() {
		String printString = "";
		for(Rule x: RuleSet.getRuleSet()) {
			printString += (x.toString());
		}
		return printString;
	}
	/* Action listeners */ 

	// Open new rule dialog window.
	// Append rule to list.
	public class NewRule implements ActionListener {
		JFrame dialogOwner;

		public NewRule(JFrame f) {
			dialogOwner = f;
		}

		public void actionPerformed(ActionEvent e) {
			// Open dialog window and extract Rule.
			// This is a modal window, so program will wait for it to close before continuing.
			RuleBuilderDialog dialog = new RuleBuilderDialog(dialogOwner);
			Rule newRule = dialog.getRule();
			dialog.dispose();
			
			// User canceled operation.
			if(newRule == null)
				return;

			// Add new rule to RuleSet.
			RuleSet.add(newRule);

			// Add new rule to gui.
			refresh();

			// Debug
			System.out.println("Added new rule");
		}
	}
	// Delete selected rule.
	public class RemoveRule implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			int index = getActiveButtonIndex();
			
			// If no button is selected.
			if(index == -1) 
				return;

			// Item must be deleted from RuleSet, ruleElements
			removeRule(index);
			scrollPane.setViewportView(rulesList);
			System.out.println("Removed rule " + index);
		}
	}
	// Swap selected rule and the rule above it.
	public class MoveUpRule implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			int index = getActiveButtonIndex();
			
			// If no button is selected or very first button is selected.
			if(index <= 0) 
				return;
			
			moveRule(index, index - 1);
			scrollPane.setViewportView(rulesList);
			System.out.println("Moved rule " + index + " up");
		}
	}
	// Swap selected rule and the rule below it.
	public class MoveDownRule implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			int index = getActiveButtonIndex();
			
			// If no button is selected or last element is selected.
			if(index == -1 || index >= ruleElements.size() - 1) 
				return;
			
			moveRule(index, index + 1);
			scrollPane.setViewportView(rulesList);
			System.out.println("Moved rule " + index + " down");
		}
	}
	// Reset ruleset to defaults.
	public class RevertToDefaults implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			RuleSet.setRuleSet(Rule.getDefaultRuleset());
			refresh();
		}
	}
	
	public class PrintRules implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String printString = createConstructString();
						
			// Add string to clipboard.
			StringSelection stringSelection = new StringSelection(printString);
			Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			clipboard.setContents(stringSelection, null);
			
			// Display to user.
			printString += (" has been copied to clipboard");
			JOptionPane.showMessageDialog(null, printString, 
					"Rules", JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	public class GenerateRules implements ActionListener {
		public void actionPerformed(ActionEvent e) 
		{
			String rulesString = rulesField.getText();
						
			String[] tokens = rulesString.split("EndRule");
			Rule[] newRules = new Rule[tokens.length];
						
			int i = 0;
			
			for(String x: tokens)
			{
				newRules[i] = new Rule(x);
				i++;
			}
			
			System.out.println("generated");
			RuleSet.setRuleSet(newRules);
			refresh();
		}
	}

}
