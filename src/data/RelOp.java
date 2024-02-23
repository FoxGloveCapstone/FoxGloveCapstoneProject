package data;

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
*/

// >, >=, <, <=, ==, !=
// These are used by the rule conditions.
public enum RelOp { 
	GT, GE, LT, LE, EQ, NE; 

	@Override
	public String toString() {
		switch(this) {
			case GT: 
				return ">";
			case GE:
				return ">=";
			case LT:
				return "<";
			case LE:
				return "<=";
			case NE:
				return "!=";
			case EQ:
			default:
				return "==";
		}
	}

	public static RelOp[] getAllRelOps() {
		return new RelOp[] { EQ, NE, GT, GE, LT, LE };
	}
} 
