package simulation;

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
*The Cell class: it Controls the state of a specific cell.
*/

import rules.*;
import data.*;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JButton;

public class Cell {
	private static RuleSet ruleset;	
	// The value of cells when first instantiated.
	private final ColorState STARTING_STATE = ColorState.WHITE;
	// Initial state is starting state by default, but can be overwritten by the user.
	private ColorState currentState, nextState, initialState;
	private JButton guiElement;


	public Cell(JButton guiElement) {
		this.guiElement = guiElement;
		setColorState(STARTING_STATE, true);
	}
	
	public void calculateNextStep(Neighbors neighbors) {
		ColorState col;
		
		for(Rule rule: RuleSet.asArray()) {
			col = rule.check(neighbors, currentState);
			if(col != null) {
				nextState = col;
				return;
			}
		}
		
		nextState = currentState;
	}
	
	public JButton getButton()
	{
		return guiElement;
	}
	
	public void updateGUI() {
		currentState = nextState;
		
		// Set button color
		Color color = ColorState.colorStateToRGB(currentState);
		guiElement.setBackground(color);
	}
	
	// If user manually set state of cell, return to that state
	// Else return to starting state
	public void resetColorState() {
		nextState = initialState;
		// updateGUI sets currentState = nextState;
		updateGUI();
	}
	
	// Return to starting state of cells
	public void clearColorState() {
		nextState = STARTING_STATE;
		// updateGUI sets currentState = nextState;
		updateGUI();
	}

	/* Getter and Setter */
	public void setColorState(ColorState state, boolean setInitialState) {
		if(setInitialState) {
			initialState = state;
		}
		currentState = state;
		
		// Set button color
		Color color = ColorState.colorStateToRGB(state);
		guiElement.setBackground(color);
	}
	
	public ColorState getColorState() {
		return currentState;
	}
}
