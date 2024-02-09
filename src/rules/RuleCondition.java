package rules;
import data.*;

/*
 * Contains a boolean statement about a cell’s environment. 
 * If this statement is evaluated to be true, this condition is met.
 */
abstract class RuleCondition {
	public abstract boolean check(Neighbors neighbors, ColorState currentColorState);
}



