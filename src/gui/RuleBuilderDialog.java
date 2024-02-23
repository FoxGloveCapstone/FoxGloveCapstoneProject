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
 * Gets input from user to create a new rule.
 */

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpinnerNumberModel;

import data.ColorState;
import data.RelOp;
import rules.Rule;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;


public class RuleBuilderDialog extends JDialog {
	private JScrollPane scrollPane; 
	private JPanel conditionList; 
	// private Rule rule = null;
	private Rule rule = null;
	
	public RuleBuilderDialog(JFrame frame) {
		super(frame, "Creating new rule", true);
		// setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setLayout(new GridLayout(5, 1));
		setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));

		// Create scrollpane containing list of RuleConditions.
		add(new JLabel("Conditions"));
		conditionList = new JPanel();
		conditionList.setLayout(new BoxLayout(conditionList, BoxLayout.PAGE_AXIS));
		scrollPane = new JScrollPane(conditionList);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		add(scrollPane);

		// Add default condition.
		createNewCondition();

		// Button to add a new RuleCondition.
		JButton newCond = new JButton("Add Condition");
		add(newCond);
		newCond.addActionListener(new AddNewCondition());

		// Combo box to select result.
		JPanel resultPanel = new JPanel();
		JComboBox<ColorState> resultSelector = new JComboBox<>(ColorState.getAllColorStates());
		resultPanel.add(new JLabel("If all conditions are true, cell should be: "));
		resultPanel.add(resultSelector);
		add(resultPanel);

		// Buttons to close dialog window.
		JButton closeButton = new JButton("Cancel");
		JButton saveButton = new JButton("Save");

		JPanel buttonPanel = new JPanel();
		buttonPanel.add(closeButton);
		buttonPanel.add(saveButton);

		closeButton.addActionListener(new CloseWindow());
		saveButton.addActionListener(new SaveRuleAndHide());

		add(buttonPanel);

		// Create dialog window.
		pack();
		setVisible(true);
	}

	public Rule getRule() {
		return rule;
	}

	// Helper method for creating new rule condition.
	// This allows for one condition to exist by default.
	private void createNewCondition() {
		RuleConditionGUI gui = new RuleConditionGUI();
		conditionList.add(gui);

		gui.getDeleteButton().addActionListener(new DeleteCondition(gui));
		scrollPane.setViewportView(conditionList);
		pack();
	}

	private class AddNewCondition implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			createNewCondition();
		}
	}
	private class DeleteCondition implements ActionListener {
		private RuleConditionGUI gui;
		public DeleteCondition(RuleConditionGUI gui) {
			this.gui = gui;
		}
		public void actionPerformed(ActionEvent e) {
			conditionList.remove(gui);
			scrollPane.setViewportView(conditionList);
			System.out.println("Deleted rule condition");
		}
	}
	private class CloseWindow implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			dispose();
			System.out.println("Cancelled new rule");
		}
	}
	private class SaveRuleAndHide implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// TODO: Iterate over RuleConditionGUI and parse -> RuleCondition[]
			// Instantiate Rule.
			// Caller gets rule.
			
			setVisible(false);
		}
	}

	private class RuleConditionGUI extends JPanel {
		// If creating a CurrentStateCondition, the target is self/the current cell.
		// Otherwise, the target are the neighbors with a given state.
		private static StateTarget[] stateTargets = StateTarget.getStateTargets();

		private JComboBox<StateTarget> targetSelector;
		// Contains all RelOps.
		private JComboBox<RelOp> opSelector;
		// Get quantity from user.
		private JSpinner intValueSelector;
		
		// Only contains EQ and NE.
		private JComboBox<RelOp> limitedOpSelector;
		// Get ColorState from user.
		private JComboBox<ColorState> colorValueSelector;

		// Button to delete condition.
		private JButton deleteButton;

		public RuleConditionGUI() {
			// Create target drop down menu.
			// If the user selects the first option "SELF", then this is a CurrentStateCondition.
			// Otherwise, they select a ColorState and this is a NeighborStateCondition.
			targetSelector = new JComboBox<>(stateTargets);
			add(targetSelector);
			targetSelector.setSelectedItem(ColorState.BLACK);
			targetSelector.setSelectedIndex(1);

			// When the value of targetSelector is changed, all of the other elements change as well.
			targetSelector.addActionListener(new SwitchTarget());

			// RelOp dropdown menu. 
			opSelector = new JComboBox<>(RelOp.getAllRelOps());
			add(opSelector);

			// If CurrentStateCondition options are EQ or NE.
			limitedOpSelector = new JComboBox<>(new RelOp[] { RelOp.EQ, RelOp.NE });
			limitedOpSelector.setEnabled(false);
			limitedOpSelector.setVisible(false);
			add(limitedOpSelector);

			// When creating a NeighborStateCondition, this gets an integer from the user.
			// Only one can be enabled at a time.
			intValueSelector = new JSpinner(new SpinnerNumberModel(1, 0, 8, 1));
			add(intValueSelector);

			// Otherwise, get a ColorState from the user.
			colorValueSelector = new JComboBox<>(ColorState.getAllColorStates());	
			colorValueSelector.setEnabled(false);
			colorValueSelector.setVisible(false);
			add(colorValueSelector);

			// Add delete button.
			// Action listener is added by parent.
			deleteButton = new JButton("X");
			add(deleteButton);
		}

		// Allows parent to add actionlistener to this button.
		public JButton getDeleteButton() {
			return deleteButton;
		}

		private class SwitchTarget implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				StateTarget target = (StateTarget)targetSelector.getSelectedItem();
				boolean isTargetSelf = target.getState() == null;

				limitedOpSelector.setEnabled(isTargetSelf);
				limitedOpSelector.setVisible(isTargetSelf);
				colorValueSelector.setEnabled(isTargetSelf);
				colorValueSelector.setVisible(isTargetSelf);

				opSelector.setEnabled(!isTargetSelf);
				opSelector.setVisible(!isTargetSelf);
				intValueSelector.setEnabled(!isTargetSelf);
				intValueSelector.setVisible(!isTargetSelf);
				
				pack();
				setVisible(true);
			}
		}
	}
}

// This class is a wrapper for ColorState. 
// Its purpose is to override the toString() method such that 'null' returns "SELF".
// This way, when the user is selecting the target for a condition, 
// SELF (CurrentStateCondition) is an option.
class StateTarget {
	private ColorState state;

	public StateTarget(ColorState state) {
		this.state = state;
	}

	public ColorState getState() {
		return state;
	}

	public static StateTarget[] getStateTargets() {
		ColorState[] colorStates = ColorState.getAllColorStates();
		StateTarget[] stateTargets = new StateTarget[colorStates.length + 1];
		stateTargets[0] = new StateTarget(null);

		for(int i = 0; i < colorStates.length; i++) {
			stateTargets[i + 1] = new StateTarget(colorStates[i]);
		}
				
		return stateTargets;
	}

	@Override
	public String toString() {
		if(state == null) 
			return "SELF";
		return state.toString();
	}
}

