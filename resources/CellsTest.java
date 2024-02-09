public class Cell {
    private boolean alive;

    public Cell() {
        this.alive = false;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }
}
public class CellManager {
    private int gridSize;
    private Cell[][] grid;

    public CellManager(int gridSize) {
        this.gridSize = gridSize;
        this.grid = new Cell[gridSize][gridSize];
        initializeGrid();
    }

    private void initializeGrid() {
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                grid[i][j] = new Cell();
            }
        }
    }

    public void setCellState(int row, int col, boolean state) {
        grid[row][col].setAlive(state);
    }

    public boolean isCellAlive(int row, int col) {
        return grid[row][col].isAlive();
    }

    public void evolve() {
        Cell[][] newGrid = new Cell[gridSize][gridSize];
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                int liveNeighbors = countLiveNeighbors(i, j);
                newGrid[i][j] = new Cell();
                if (grid[i][j].isAlive()) {
                    if (liveNeighbors < 2 || liveNeighbors > 3) {
                        newGrid[i][j].setAlive(false);
                    } else {
                        newGrid[i][j].setAlive(true);
                    }
                } else {
                    if (liveNeighbors == 3) {
                        newGrid[i][j].setAlive(true);
                    }
                }
            }
        }
        grid = newGrid;
    }

    private int countLiveNeighbors(int row, int col) {
        int count = 0;
        for (int i = row - 1; i <= row + 1; i++) {
            for (int j = col - 1; j <= col + 1; j++) {
                if (i >= 0 && i < gridSize && j >= 0 && j < gridSize && !(i == row && j == col) && grid[i][j].isAlive()) {
                    count++;
                }
            }
        }
        return count;
    }
}


