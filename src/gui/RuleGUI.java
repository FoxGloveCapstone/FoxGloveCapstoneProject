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
 * Used to display a specific rule in the rules list.
 */

import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JLabel;

import data.ColorState;
import data.RelOp;
import rules.CurrentStateCondition;
import rules.NeighborStateCondition;
import rules.Rule;
import rules.RuleCondition;

@SuppressWarnings("serial")
public class RuleGUI extends JPanel {
	private JRadioButton selectButton;

	public RuleGUI(Rule rule) {
		RuleCondition[] conditions = rule.getConditions();
		ColorState result = rule.getResult();
		
		selectButton = new JRadioButton();
		JPanel selectPanel = new JPanel();
		selectPanel.add(selectButton);
		add(selectPanel);
		
		// Set here incase conditions[] is empty.
		JLabel paddingPanel = new JLabel();
		
		for(RuleCondition cond: conditions) {
			// Declaring this outside of the loop puts the && before the If
			// E.g. If self == BLACK && BLACK == 2 --> BLACK 
			// becomes: && If self == BLACK BLACK == 2 --> BLACK
			JPanel panel = new JPanel();
			panel.add(new JLabel(getConditionStatement(cond)));
			add(panel);

			// If no more conditions, this is overwritten.
			paddingPanel = new JLabel("&&");
			add(paddingPanel);
		}
		paddingPanel.setText("--> it becomes " + result);
	}
	
	// Get the plain english form of rule condition.
	private String getConditionStatement(RuleCondition cond) {
		// If a cell is/isnt COLOR 
		if(cond instanceof CurrentStateCondition) {
			return String.format("If a cell %s %s", 
					cond.getOp() == RelOp.EQ ? "is" : "isn't", 
					cond.getColorState());
		}

		// EQ: If a cell has INT COLOR neighbors
		// NE: If a cell doesn't have INT COLOR neighbors
		// GE: If a cell has INT or more COLOR neighbors
		// GT: If a cell has more than INT COLOR neighbors
		// LE: If a cell has INT or less COLOR neighbors
		// LT: If a cell has less than INT COLOR neighbors
		RelOp op = cond.getOp();
		String neighborState = String.format("%s neighbors", cond.getColorState());
		String quantity;

		// Initialize value of quantity. 
		// All values except NE start with "has".
		if(op == RelOp.NE) 
			quantity = "doesn't have ";
		else 
			quantity = "has ";
		
		// Add text that precedes INT, where necessary.
		if(op == RelOp.GT)
			quantity += "more than ";
		else if(op == RelOp.LT)
			quantity += "less than ";

		// Add INT to text.
		// Notice, no trailing space.
		quantity += ((NeighborStateCondition)cond).getQuantity();

		// Add text following INT.
		if(op == RelOp.GE) 
			quantity += " or more";
		else if(op == RelOp.LE)
			quantity += " or less";
		return String.format("If a cell %s %s", quantity, neighborState);
	}

	// Called by parent in order to add button to a ButtonGroup.
	// This ensures only one rule is selected at a time.
	public JRadioButton getSelectButton() {
		return selectButton;
	}
	
	public boolean isSelected() {
		return selectButton.isSelected();
	}
}
