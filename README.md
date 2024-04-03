# FoxgloveCapstoneProject
This is the Capstone project created by Team Foxglove, in the UMGC graduating class of 2024. It runs an interactable simulation of Conway's Game of Life, the details of which can be found [here](https://conwaylife.com/). Additionally, this project allows the user to add extra colors to the grid and define custom rules to determine their behavior.

## User Interface
![Entire UI](https://github.com/FoxGloveCapstone/FoxGloveCapstoneProject/resources/1.png?raw=true)
Fig 1: The User interface


![UI for generating a new grid/map](https://github.com/FoxGloveCapstone/FoxGloveCapstoneProject/resources/2.png?raw=true)
Fig 2: The controls for generating a new grid/map.

2.1) The “Map Size” determines the length and width of the grid. All grids must be square. The actual grid will not be updated until one of the generate map buttons are pressed.

2.2) Clicking the “Generate Map” button creates a new blank grid.

2.3) Clicking the “Generate Random Map” button creates a new randomized grid using the seed value provided in the text field labelled “seed” in figure 2.


![UI for controlling the simulation](https://github.com/FoxGloveCapstone/FoxGloveCapstoneProject/resources/3.png?raw=true)
Fig 3: The buttons for controlling the simulation.

3.1) The start button begins the simulation with the currently displayed configuration and proceeds to the next step once per second. The ticks per section buttons will increase the speed at which the simulation runs.

3.2) The next button increments the simulation by only one step.

3.3) The Pause button pauses the simulation at the current step. 

3.4) The clear button resets the grid to an empty state. 

3.5) The reset button resets the grid to the original user placed configuration.


![UI for controlling camera and sim speed](https://github.com/FoxGloveCapstone/FoxGloveCapstoneProject/resources/4.png?raw=true)
Fig 4: The buttons for controlling the camera, simulation speed, and a display showing the current frame number.

4.1) The “pan” buttons will move the screen up down left or right when the camera is zoomed in.

4.2) The zoom in button will increment the grid closer to a point while the zoom out button will increment the grid further away, up to the original point.

4.3) The “Ticks per Second” shows the speed the simulation is currently playing at. The arrows can be used to increment/decrement this speed.

4.4) The “Frame Counter” shows the current frame the simulation is on.


## Advanced Controls
The program allows for the user to define their own rules and offers a variety of colors to select from while doing so.

![UI displaying simulation rules](https://github.com/FoxGloveCapstone/FoxGloveCapstoneProject/resources/5-1.png?raw=true)
Fig 5.1: The ruleset governing how the simulation is running and the controls for modifying it.

Every frame, each cell tests their environment against the list of rules shown in figure 5.1. The first rule to have all its conditions met is then executed, determining the cell’s state in the next frame.
A rule is composed of 1 or more conditions, which are simple boolean (true or false) statements about the cell’s environment. The environment consists of the cell’s current state and the states of its 8 neighbors. A rule also has a result: the state a cell becomes after meeting all its conditions. 


![Closeup of rule](https://github.com/FoxGloveCapstone/FoxGloveCapstoneProject/resources/5-2.png?raw=true)
Fig 5.2: Rule 3 from the default set of rules. It is two conditions are met the cell turns black in the next frame.

A rule can be selected using the radio button on its left (as seen in figures 5.1 and 5.2). The selected rule can then be modified using the buttons shown in figure 5.3 (with the exceptions of the “New Rule” and “Revert to Default Ruleset” buttons, which operate on the entire list). 


![UI for modifying the ruleset](https://github.com/FoxGloveCapstone/FoxGloveCapstoneProject/resources/5-3.png?raw=true)
Fig 5.3: The buttons used to modify the ruleset.

• The “Move Rule Up” and “Move Rule Down” buttons move the currently selected up or down in the list, respectively. The order of the rules matters, as the cell will execute the first one it matches with.
• The “New Rule” button opens a dialog window, which allows the user to create their own custom rule. This is discussed in further detail below.
• The “Remove Rule” button removes the selected rule from the list.
• The “Revert to Default Ruleset” button resets the list to the original 4.


![Rule Creation Dialog](https://github.com/FoxGloveCapstone/FoxGloveCapstoneProject/resources/5-4.png?raw=true)
Fig 5.4: The rule creation dialog window.

There are 2 types of conditions a rule can use, and every rule can have as many conditions as needed. These conditions are shown in a list at the top of the window shown in 5.4. A new condition can be added by clicking the “Add Condition” button and a condition can be removed by clicking the “X” on its right. 

The first type of condition analyzes the neighbors around the cell (the first and second conditions shown in figure 5.4). It has 3 parts which are selected using the three dropdown menus. These parts are \<color>, \<relationship>, and \<quantity>, where:
    • \<color>: one of the colors this cell’s neighbors can be in.
    • \<quantity>: A number between 0 and 8, referring to the number of neighbors with/without this color state.
    • \<relationship>: equal (==), not equal (!=), greater than (>), greater than or equal (>=), less than (\<), or less than or equal (\<=). This is how the number of neighbors must relate to the \<quantity>.

For example, in the first condition in figure 5.4 <color = black>, <relationship = ‘==’>, and <quantity = 1>. In plain English, this means that a cell must have 1 neighbor which is currently black (no more, no less). Similarly, the second condition means that the cell must have less than 8 red neighbors. 

The second type of condition checks whether a cell is/is not a specific color. It can be selected by picking “Self” from the first dropdown menu (HINT: this is the option above the default option, so you might have to scroll up to find it). The second dropdown offers the choice of equal or not equal. And the third dropdown allows you to select a color state. 

The third and fourth conditions in figure 5.4 are examples of this condition type. The third condition checks if this cell (self) is green, which the fourth condition checks that it is not blue. (Technically, you would not use both conditions in the same rule, as if a cell is green, it is necessarily not blue. This was done for demonstrative purposes).

Below the “Add Condition” box in figure 5.4 is where you set the result of the rule. This is the color the cell will take in the next frame if it satisfies the conditions defined by this rule. So, for the example rule from above, a green cell with 1 black and 7 red neighbors will turn orange in the next frame. 


![UI for controlling drawing color](https://github.com/FoxGloveCapstone/FoxGloveCapstoneProject/resources/5-5.png?raw=true)
Fig 5.5: The dropdown menu where the user can select their drawing color. 

Clicking on a blank cell in the grid will set that cell’s state to match the currently selected drawing color. By default, this is black, but it can be changed using the dropdown menu shown in figure 5.5. Clicking on a non-blank cell will change its color to white. 

NOTE: the behavior of the cells is determined by the rules. If you set a cell to a color that is not mentioned in the rules, it will most likely disappear in the next frame or otherwise behave in an unexpected manner. For example, if you are using the default ruleset and make a cell red, this cell will turn white or black in the next frame, depending on its neighbors. 


![UI for saving and loading rulesets](https://github.com/FoxGloveCapstone/FoxGloveCapstoneProject/resources/6.png?raw=true)
Fig 6: The controls for saving and loading rulesets.

• Pressing the “Print Rule Seed” button will save a rule seed string to your clipboard (copy). This saves a copy of the current ruleset
• Pasting a rule seed into the text field and pressing “Generate Rule Seed” will load that ruleset.
