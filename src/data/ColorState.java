package data;

import java.awt.Color;

/*
 * Represents the states a cell can be in.
 * White = dead
 * Black = live
 */
public enum ColorState {
	WHITE, 
	BLACK;
	
	// Hyrum: I know this looks redundant, but if/when the ruleset is made extendible it'll be easier to maintain.
	public static Color colorStateToRGB(ColorState state) {
		switch(state) {
		case BLACK: 
			return Color.BLACK;
		case WHITE:
		default:
			return Color.WHITE;
		}
	}
}

