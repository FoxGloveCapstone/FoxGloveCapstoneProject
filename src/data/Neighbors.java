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
*The Neighbors class: it An alias for HashMap<Color, int>. 
* This stores data concerning how many of a cell’s neighbors have a certain state. 
* The key stores a Color the cell can have.
* The value stores the number of neighbors in that state.
*/


import java.util.HashMap;


public class Neighbors {
	private HashMap<ColorState, Integer> neighbors = new HashMap<ColorState, Integer>();
	
	public void add(ColorState key, int value) {
		// If key already has a value, the numbers are added.
		// Otherwise, a new entry is created in the map.
		neighbors.merge(key, value, Integer::sum);
	}
	public void add(ColorState key) {
		add(key, 1);
	}
	
	public int get(ColorState key) {
		return neighbors.getOrDefault(key, 0);
	}
}
