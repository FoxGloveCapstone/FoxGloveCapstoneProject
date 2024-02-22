package data;
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
 * Class containing a static reference to the Rules.
 */

import java.util.ArrayList;
import java.util.Arrays;

import rules.Rule;

public class RuleSet {
	private static ArrayList<Rule> ruleSet;

	public static void setRuleSet(Rule[] rules) {
		ruleSet = new ArrayList<>();
		ruleSet.addAll(Arrays.asList(rules));
	}

	public static Rule[] asArray() {
		Rule[] output = new Rule[ruleSet.size()];
		return ruleSet.toArray(output);
	}

	public static void add(Rule rule) {
		ruleSet.add(rule);
	}
	public static Rule remove(int index) {
		return ruleSet.remove(index);
	}
	public static void remove(Rule rule) {
		ruleSet.remove(rule);
	}
	public static void moveRule(int ruleIndex, int newIndex) {
		// Ensure ruleIndex is in bounds.
		if(ruleIndex < 0 || ruleIndex >= size())
			return;
		// Ensure newIndex is in bounds.
		if(newIndex < 0)
			newIndex = 0;
		else if(newIndex >= size())
			newIndex = size() - 1;

		Rule rule = ruleSet.remove(ruleIndex);
		ruleSet.add(newIndex, rule);
	}
	public static int size() {
		return ruleSet.size();
	}
}
