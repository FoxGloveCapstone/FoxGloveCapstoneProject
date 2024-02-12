package rules;

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
*The CurrentStateCondition class: it contains a boolean statement about a cell’s current state. 
* If this statement is evaluated to be true, this condition is met.
*/

import data.ColorState;
import data.Neighbors;
import data.RelOp;

class CurrentStateCondition extends RuleCondition {
	private ColorState requiredColorState;
	private RelOp op;

	public CurrentStateCondition(ColorState requiredColorState) {
		this.requiredColorState = requiredColorState;
		this.op = RelOp.EQ;
	}

	public CurrentStateCondition(ColorState requiredColorState, RelOp op) {
		// Technically, this value can only be EQ or NE.
		// However, the check method only checks if op == NE. Any other value defaults to EQ.
		this.op = op;
		this.requiredColorState = requiredColorState;
	}

	@Override
	public boolean check(Neighbors neighbors, ColorState currentColorState) {
		if(op == RelOp.NE) 
			return this.requiredColorState != currentColorState;
		return this.requiredColorState == currentColorState;
	}
}
