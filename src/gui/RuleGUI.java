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

import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;

import data.*;
import rules.CurrentStateCondition;
import rules.NeighborStateCondition;
import rules.Rule;
import rules.RuleCondition;


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
			panel.add(new JLabel("If"));
			if(cond instanceof NeighborStateCondition) {
				panel.add(new JLabel("" + ((NeighborStateCondition)cond).getColorState()));
				panel.add(new JLabel("" + ((NeighborStateCondition)cond).getOp()));
				panel.add(new JLabel("" + ((NeighborStateCondition)cond).getQuantity()));
			} 
			else {
				panel.add(new JLabel("self"));
				panel.add(new JLabel("" + ((CurrentStateCondition)cond).getOp()));
				panel.add(new JLabel("" + ((CurrentStateCondition)cond).getColorState()));
			}
			add(panel);

			// If no more conditions, this is overwritten to be -->
			paddingPanel = new JLabel("&&");
			add(paddingPanel);
		}
		paddingPanel.setText("-->");
		
		add(new JLabel("" + result));
	}

	public JRadioButton getSelectButton() {
		return selectButton;
	}
	public boolean isSelected() {
		return selectButton.isSelected();
	}
}
