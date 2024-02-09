package simulation;
import rules.*;

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
	
	public void calculateNextStep(Neighbors neighbors) {
		
	}
	
	public void updateGUI() {
		
	}
}
