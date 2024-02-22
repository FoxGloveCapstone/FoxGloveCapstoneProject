package rules;

import java.awt.GridLayout;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;

import simulation.Cell;

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
 * Displays all rules currently active.
 */

public class RulesListGUI extends JPanel {
	private JScrollPane scrollPane;
	private JPanel rulesList;
	private ArrayList<RuleGUI> ruleElements;

	// Holds list of radio buttons connected to RuleGUI objects.
	// This way, only one rule can be selected for editing at a time.
	private ButtonGroup selectButtons;

	public RulesListGUI(Rule[] rules) {
		setLayout(new GridLayout(2, 1));
		rulesList = new JPanel();

		// Add list of rules to scroll pane.
		scrollPane = new JScrollPane(rulesList);
		add(scrollPane);

		buildRulesList(rules);
		// Add editing buttons at bottom.
		JPanel buttonsPanel = new JPanel();
		add(buttonsPanel);

		JButton moveUpButton = new JButton("Up");
		JButton moveDownButton = new JButton("Down");
		JButton newButton = new JButton("New");
		JButton removeButton = new JButton("Remove");

		buttonsPanel.add(moveUpButton);
		buttonsPanel.add(moveDownButton);
		buttonsPanel.add(newButton);
		buttonsPanel.add(removeButton);
	
		moveUpButton.addActionListener(new MoveUpRule());
		moveDownButton.addActionListener(new MoveDownRule());
		newButton.addActionListener(new NewRule());
		removeButton.addActionListener(new RemoveRule());
	}
	
	public RulesListGUI() {
		new RulesListGUI(RuleSet.asArray());
	}
	/* Creates GUI element 
	 * Assumes rulesList is already initialized.
	 */
	private void buildRulesList(Rule[] rules) {
		ruleElements = new ArrayList<>();

		rulesList.setLayout(new GridLayout(rules.length, 1));
		selectButtons = new ButtonGroup();

		RuleGUI element;
		for(Rule rule: rules) {
			element = new RuleGUI(rule);
			selectButtons.add(element.getSelectButton());
			rulesList.add(element);
			ruleElements.add(element);
		}

		scrollPane.setViewportView(rulesList);
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
	
	// Helper function to delete everything related to a rule.
	private void removeRule(int index) {
		RuleSet.remove(index);
		rulesList.remove(index);
		ruleElements.remove(index);
	}
	
	private void moveRule(int oldIndex, int newIndex) {
		RuleSet.moveRule(oldIndex, newIndex);
		
		RuleGUI gui = ruleElements.remove(oldIndex);
		ruleElements.add(newIndex, gui);

		rulesList.remove(oldIndex);
		rulesList.add(gui, newIndex);
	}

	/* Action listeners */ 

	// Open new rule dialog window.
	// Append rule to list.
	public class NewRule implements ActionListener {
		public void actionPerformed(ActionEvent e) {
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
		}
	}
	public class MoveUpRule implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			int index = getActiveButtonIndex();
			
			// If no button is selected or very first button is selected.
			if(index <= 0) 
				return;
			
			moveRule(index, index - 1);
			scrollPane.setViewportView(rulesList);
		}
	}
	public class MoveDownRule implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			int index = getActiveButtonIndex();
			
			// If no button is selected or last element is added
			if(index == -1 || index >= ruleElements.size() - 1) 
				return;
			
			moveRule(index, index + 1);
			scrollPane.setViewportView(rulesList);
		}
	}
}
