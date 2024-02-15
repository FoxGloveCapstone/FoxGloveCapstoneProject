package simulation;

/*UMGC CAPSTONE PROJECT
* Title:Game of Life in Java By Team Fox Glove:
*         Anthony Farias
          Mitchell Howard
          Patrick Kamdem
          Hyrum Madson
          Bensaiten Sanchez Flores 
          
*CMSC 495 7380
*Professor Sanford
*February 12, 2024 
*
*The Cell class: it Controls the state of a specific cell.
*/

import rules.*;
import data.*;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JButton;

public class Cell {
	private static ArrayList<Rule> ruleset;	
	private ColorState currentState, nextState, initialState;
	private JButton guiElement;

	public static void setRuleSet(Rule[] rules) {
		ruleset = new ArrayList<>();
		ruleset.addAll(Arrays.asList(rules));
	}
	
	public static void addRule(Rule rule) {
		ruleset.add(rule);
	}
	
	public static void removeRule(Rule rule) {
		ruleset.remove(rule);
	}
	
	public static void removeRule(int index) {
		ruleset.remove(index);
	}
	
	public Cell(JButton guiElement) {
		this.guiElement = guiElement;
		setColorState(ColorState.WHITE);
	}
	
	public void calculateNextStep(Neighbors neighbors) {
		ColorState col;
		
		for(Rule rule: ruleset) {
			col = rule.check(neighbors, currentState);
			if(col != null) {
				nextState = col;
				return;
			}
		}
		
		nextState = currentState;
	}
	
	public void updateGUI() {
		currentState = nextState;
		
		// Set button color
		Color color = ColorState.colorStateToRGB(currentState);
		guiElement.setBackground(color);
	}
	
	/* Getter and Setter */
	public void setColorState(ColorState state) {
		initialState = state;
		currentState = state;
		
		// Set button color
		Color color = ColorState.colorStateToRGB(state);
		guiElement.setBackground(color);
	}
	
	public ColorState getColorState() {
		return currentState;
	}
}
