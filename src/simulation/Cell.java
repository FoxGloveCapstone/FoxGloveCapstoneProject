package simulation;
import rules.*;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JButton;

import data.*;
/*
 * Controls the state of a specific cell.
 */
public class Cell {
	private static ArrayList<Rule> ruleset;	
	private ColorState currentState, nextState, initialState;
	private JButton guiElement;

	public static void setRuleSet(Rule[] rules) {
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
		
	}
	
	public void updateGUI() {
		
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
