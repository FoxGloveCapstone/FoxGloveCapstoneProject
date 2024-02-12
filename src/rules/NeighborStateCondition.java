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
*The NeighborStateCondition class: it Contains a boolean statement about a cell’s neighbors. 
 * If this statement is evaluated to be true, this condition is met.
 */

import data.*;

class NeighborStateCondition extends RuleCondition {
	// The state of the cells to evaluate.
	private ColorState colorState;
	// The quantity to evaluate against.
	private int quantity;
	// How to compare colorState to quantity.
	private RelOp op;

	public NeighborStateCondition(ColorState colorState, RelOp op, int quantity) {
		this.colorState = colorState;
		this.op = op;
		this.quantity = quantity;
	}

	@Override
	public boolean check(Neighbors neighbors, ColorState currentColorState) {
		int value = neighbors.get(colorState);

		switch(op) {
			case GT:
				return value > quantity;
			case GE:
				return value >= quantity;
			case LT:
				return value < quantity;
			case LE:
				return value <= quantity;
			case EQ:
				return value == quantity;
			case NE:
			default:
				return value != quantity;
		}
	}
}
