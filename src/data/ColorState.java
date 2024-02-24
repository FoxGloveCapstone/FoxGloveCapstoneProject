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
*
*ColorState: Represents the states a cell can be in.
* White = dead
* Black = live
*/

import java.awt.Color;

public enum ColorState {
	WHITE, 
	BLACK,
	RED,
	GREEN,
	BLUE,
	ORANGE,
	YELLOW,
	MAGENTA,
	PINK,
	DARKGRAY,
	GRAY,
	LIGHTGRAY,
	CYAN;
	
	// This looks redundant, but if/when the ruleset is made extendible it'll be easier to maintain.
	public static Color colorStateToRGB(ColorState state) {
		switch(state) {
		case BLACK: 
			return Color.BLACK;
		case RED:
			return Color.RED;
		case GREEN:
			return Color.GREEN;
		case BLUE:
			return Color.BLUE;
		case ORANGE:
			return Color.ORANGE;
		case YELLOW: 
			return Color.YELLOW;
		case MAGENTA:
			return Color.MAGENTA;
		case PINK:
			return Color.PINK;
		case DARKGRAY:
			return Color.DARK_GRAY;
		case GRAY:
			return Color.GRAY;
		case LIGHTGRAY:
			return Color.LIGHT_GRAY;
		case CYAN:
			return Color.CYAN;
		case WHITE:
		default:
			return Color.WHITE;
		}
	}

	public static ColorState[] getAllColorStates () {
		return new ColorState[] { BLACK, WHITE, RED, GREEN, BLUE, ORANGE, YELLOW, MAGENTA, DARKGRAY, GRAY, LIGHTGRAY, CYAN, PINK };
	}
}
