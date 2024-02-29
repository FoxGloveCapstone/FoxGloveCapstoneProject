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
 * Gets input from user in order to create a new rule.
 */

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpinnerNumberModel;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import data.ColorState;
import data.RelOp;
import rules.CurrentStateCondition;
import rules.NeighborStateCondition;
import rules.Rule;
import rules.RuleCondition;

@SuppressWarnings("serial")
public class RuleBuilderDialog extends JDialog {
	private JScrollPane scrollPane;
	private JPanel conditionList; 
	private Rule rule = null;
	private ArrayList<RuleConditionGUI> conditionElements = new ArrayList<>();
	private JComboBox<ColorState> resultSelector;

	public RuleBuilderDialog(JFrame frame) {
		super(frame, "Creating new rule", true);
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
		resultSelector = new JComboBox<>(ColorState.getAllColorStates());
		resultPanel.add(new JLabel("If all conditions are true, cell should be: "));
		resultPanel.add(resultSelector);
		add(resultPanel);

		// Buttons to close dialog window.
		JButton closeButton = new JButton("Cancel");
		JButton saveButton = new JButton("Save");
		closeButton.addActionListener(new CloseWindow());
		saveButton.addActionListener(new SaveRuleAndHide());

		// Add buttons to their panel.
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(closeButton);
		buttonPanel.add(saveButton);


		add(buttonPanel);

		// Create dialog window.
		pack();
		setVisible(true);
	}

	// Called by RulesListGUI.
	// This value will be null if user cancels operation.
	public Rule getRule() {
		return rule;
	}

	// Helper method for creating new rule condition.
	// This allows for one condition to exist by default.
	private void createNewCondition() {
		RuleConditionGUI gui = new RuleConditionGUI();
		conditionList.add(gui);
		conditionElements.add(gui);

		gui.getDeleteButton().addActionListener(new DeleteCondition(gui));
		scrollPane.setViewportView(conditionList);
		pack();
	}

	/* Action Listeners */
	
	// Adds another rule condition to list.
	private class AddNewCondition implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			createNewCondition();
		}
	}
	
	// Remove rule condition from list.
	private class DeleteCondition implements ActionListener {
		private RuleConditionGUI gui;
		
		// This class needs a reference to their parent, in order to delete themselves from the list.
		public DeleteCondition(RuleConditionGUI gui) {
			this.gui = gui;
		}
		
		public void actionPerformed(ActionEvent e) {
			conditionList.remove(gui);
			conditionElements.remove(gui);
			
			scrollPane.setViewportView(conditionList);
			System.out.println("Deleted rule condition");
		}
	}
	
	// Closes window without saving. All data will be destroyed.
	private class CloseWindow implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			System.out.println("Cancelled new rule");
			dispose();
		}
	}
	
	// Instantiates a Rule and hides window.
	// Caller must ensure window is deleted, after reading the value.
	private class SaveRuleAndHide implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// Create new rule from gui.
			RuleCondition[] conditions = collectConditionsFromGUI();
			ColorState result = (ColorState)resultSelector.getSelectedItem();
			rule = new Rule(conditions, result);

			// Hide window.
			// Caller is responsible for disposing of it.
			setVisible(false);
		}
		
		// Helper method to parse GUI elements into RuleCondition objects.
		private RuleCondition[] collectConditionsFromGUI() {
			RuleCondition[] conditions = new RuleCondition[conditionElements.size()];

			for (int i = 0; i < conditions.length; i++) {
				conditions[i] = conditionElements.get(i).toRuleCondition();	
			}
			return conditions;
		}
	}

	// Helper class representing one RuleCondition. 
	// Holds gui elements used to modify a specific rulecondition.
	private class RuleConditionGUI extends JPanel {
		// If creating a CurrentStateCondition, the target is self/the current cell.
		// Otherwise, the target are the neighbors with a given state.
		private static StateTarget[] stateTargets = StateTarget.getStateTargets();
		
		// What kind of cells are being looked at (Self or ColorState).
		private JComboBox<StateTarget> targetSelector;
		
		/* RuleCondition: <target> <relop> <value> 
		 *		Checks if <target> matches(using relop) <value> 
		 * CurrentStateCondition: <target=self> <relop is either EQ or NE> <value is a ColorState>
		 * NeighborStateCondition: <target(s) are neighbors with a ColorState> <relop can be any RelOp> <value is an integer> 
		 */
		
		// Used by NeighborStateCondition constructors.
		// Contains all RelOps.
		private JComboBox<RelOp> opSelector;
		// Get integer quantity from user.
		private JSpinner intValueSelector;
		
		// Used by CurrentStateCondition constructors.
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
		
		// Converts data held by GUI elements into a RuleCondition.
		public RuleCondition toRuleCondition() {
			ColorState target = ((StateTarget)targetSelector.getSelectedItem()).getState();
			if(target == null) {
				return new CurrentStateCondition(
						(ColorState)colorValueSelector.getSelectedItem(), 
						(RelOp)limitedOpSelector.getSelectedItem());
			} 
			return new NeighborStateCondition(
					target, 
					(RelOp)opSelector.getSelectedItem(), 
					(int)intValueSelector.getValue());
		}

		// Action Listener used to show/hide the appropriate operator and value selector elements
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

	// Used to construct an array of StateTargets, which contain every possible value, including null.
	public static StateTarget[] getStateTargets() {
		ColorState[] colorStates = ColorState.getAllColorStates();
		StateTarget[] stateTargets = new StateTarget[colorStates.length + 1];
		stateTargets[0] = new StateTarget(null);

		for(int i = 0; i < colorStates.length; i++) {
			stateTargets[i + 1] = new StateTarget(colorStates[i]);
		}
				
		return stateTargets;
	}

	// When JComboBox displays these values, it will show "SELF" instead of null.
	@Override
	public String toString() {
		if(state == null) 
			return "SELF";
		return state.toString();
	}
}

