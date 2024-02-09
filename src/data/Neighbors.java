package data;
import java.util.HashMap;

/*
 * An alias for HashMap<Color, int>. 
 * This stores data concerning how many of a cell’s neighbors have a certain state. 
 * The key stores a Color the cell can have.
 * The value stores the number of neighbors in that state.
 */
public class Neighbors {
	private HashMap<ColorState, Integer> neighbors = new HashMap<ColorState, Integer>();
	
	public void add(ColorState key, int value) {
		// If key already has a value, the numbers are added.
		// Otherwise, a new entry is created in the map.
		// Hyrum: I haven't actually tested this function to make sure it does what I think it does. I'm trusting the documentation on this one, for now.
		neighbors.merge(key, value, Integer::sum);
	}
	
	public int get(ColorState key) {
		return neighbors.getOrDefault(key, 0);
	}
}
