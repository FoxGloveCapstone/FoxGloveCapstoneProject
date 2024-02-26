package rules;

import java.util.EnumSet;
import java.util.HashSet;

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
*The Rule class: it checks whether certain conditions are met by a cell’s environment 
 * (how many of its neighbors are alive/dead, its current state, etc).
 */

import data.*;

public class Rule {
	// If all of these resolve to true
	private RuleCondition[] conditions;
	// The cell should become this ColorState
	private ColorState result;

	public Rule(RuleCondition[] conditions, ColorState result) {
		this.conditions = conditions;
		this.result = result;
	}
	public Rule(RuleCondition condition, ColorState result) {
		this.conditions = new RuleCondition[] { condition };
		this.result = result;
	}
	public Rule(String constructString) {
		String[] tokens = constructString.split(" "); 
		
		conditions = new RuleCondition[tokens.length - 1];
		
		int x = 0;
		
		for (String token: tokens)
		{
						
			switch(token.substring(0,2))
			{
			case "CS": 
				conditions[x]= new CurrentStateCondition(token.substring(3));
				x+=1;
				break;
			case "NS":
				conditions[x]= new NeighborStateCondition(token.substring(3));
				x+=1;
				break;
			case "CR":
				result = ColorState.parseString(token.substring(3));
				break;
			default:
				//error
			}
		}
	}

	// The return value of this could be an Optional type, 
	// which can either be empty or contain a specific value.	
	// Its technically safer to use than null, but in the scope of this project its fine.
	public ColorState check(Neighbors neighbors, ColorState currentColorState) {
		for (RuleCondition cond : conditions) {
			if (!cond.check(neighbors, currentColorState)) {
				return null;
			}
		}
		return result;
	}
	
	// Helper Method to generate default rules automatically.
	public static Rule[] getDefaultRuleset() {
		return Rule.getDefaultRuleset(ColorState.BLACK);
	}

	public static Rule[] getDefaultRuleset(ColorState liveState) {
		Rule[] rules = new Rule[4];
		ColorState LIVE = liveState;
		final ColorState DEAD = ColorState.WHITE;

		// Rule 1: Any live cell with fewer than two live neighbors dies (referred to as underpopulation). 
		// A dead cell with < 2 neighbors remains dead.
		rules[0] = new Rule(new NeighborStateCondition(LIVE, RelOp.LT, 2), DEAD);

		// Rule 2: Any live cell with three or more neighbors dies (referred to as overpopulation). 
		// A dead cell with > 3 neighbors remains dead.
		rules[1] = new Rule(new NeighborStateCondition(LIVE, RelOp.GT, 3), DEAD);

		// Rule 3: Any live cell with two OR three live neighbor's lives, unchanged, to the next generation. 
		// Rule 3 has to be broken into two parts, because this is the easiest way to emulate an OR operation.
		// Rule 3a: Any live cell with two live neighbor's lives, unchanged, to the next generation. 
		rules[2] = new Rule(
				new RuleCondition[] {
					new CurrentStateCondition(LIVE),
					new NeighborStateCondition(LIVE, RelOp.EQ, 2)
				}, LIVE);

		// However, the second half of rule 3 can be combined with rule 4.
		// Rule 3b: Any live cell with three neighbors stays alive.  
		// Rule 4: Any dead cell with exactly three neighbors comes to life.  
		rules[3] = new Rule(new NeighborStateCondition(LIVE, RelOp.EQ, 3), LIVE);
		
		return rules;
	}
	
	@Override
	public String toString() {		
		String returnString = "";
		
		for (RuleCondition rule: conditions)
		{			
			returnString += (rule.toString()+" ");
		}
		
		returnString += "CR:" + result.toString();
		
		returnString +=" EndRule";
		
		return returnString;
	}

	// Get color states mentioned in conditions and result.
	public HashSet<ColorState> getRelatedColorStates() {
		HashSet<ColorState> colors = new HashSet<>();
		colors.add(result);

		for(RuleCondition cond: conditions) {
			colors.add(cond.getColorState());
		}

		return colors;
	}

	// Used by RuleGUI to read conditions.
	public RuleCondition[] getConditions() {
		return conditions;
	}
	// Used by RuleGUI to read result.
	public ColorState getResult() {
		return result;
	}
}
