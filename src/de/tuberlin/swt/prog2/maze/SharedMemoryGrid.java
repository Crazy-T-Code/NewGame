package de.tuberlin.swt.prog2.maze;

import java.awt.Point;

/**
 * Grid for a cellular automaton that works with four worker-threads and the
 * grid is always shared between all worker- threads.
 */
public class SharedMemoryGrid implements Grid {
	/**
	 * Number of rows in the grid
	 */
	private int rows;

	/**
	 * Number of columns in the grid
	 */
	private int cols;

	/**
	 * Current grid that can be read safely and holds the current generation.
	 */
	private boolean[][] grid;

	/**
	 * Next grid that can be written safely and holds the next generation.
	 */
	private boolean[][] newGrid;

	/**
	 * Number of the current generation
	 */
	private int generation;

	/**
	 * List of all worker-threads that calculates parts of the next generation.
	 */
	// TODO: Un-Comment for HA 3.2.
	// private ArrayList<GridThread> threads = new ArrayList<GridThread>();

	/**
	 * Array of all numbers of neighbours that lead into surviving cells
	 */
	private int neighboursLeadToSurvival[];

	/**
	 * Array of all numbers of neighbours that lead into new born cells
	 */
	private int neighboursLeadToBirth[];

	/**
	 * Array of points to indicate all neighbours of a current cell.
	 */
	private static final Point NEIGHBOURS[] = { new Point(-1, -1), // left/above
	        new Point(-1, 0), // left
	        new Point(-1, 1), // left/below
	        new Point(0, -1), // above
	        new Point(0, 1), // below
	        new Point(1, -1), // right/above
	        new Point(1, 0), // right
	        new Point(1, 1) // right/below
	};

	/**
	 * Constructor
	 * 
	 * @param rows
	 *            number of rows
	 * @param cols
	 *            number of columns
	 * @param surviveRule
	 *            array of numbers of neighbours needed to let a cell survive
	 * @param bornRule
	 *            array of numbers of neighbours needed to let a cell to be born
	 */
	public SharedMemoryGrid(int rows, int cols, int[] surviveRule,
	        int[] bornRule) {
		// TODO: implement

	}

	@Override
	public void createInitialFigure() {
		// TODO: implement
	}

	@Override
	public boolean getCell(int x, int y) {
		return grid[x][y];
	}

	@Override
	public int getCols() {
		return cols;
	}

	@Override
	public int getGenerationNumber() {
		return generation;
	}

	@Override
	public int getRows() {
		return rows;
	}

	@Override
	public void newGeneration() {
		// TODO: implement
	}

	@Override
	public void newGeneration(Point start, Point end) {
		// TODO: implement

		/*** survive rule ***/
		// wenn die Zelle lebt und Anzahl Nachbarzellen, lebend >= 1 und <= 5
		// dann Zelle lebt
		/*** born rule ***/
		// wenn Zelle tot und Anzahl Nachbarzellen, lebend == 3
		// dann Zelle wird geboren -> lebt
		/*** sonst, alle anderen F�lle - Zelle tot - setDeadCell(int x, int y) ***/
		// wenn Zelle lebt dann tot
		// wenn Zelle tot dann tot

		/*** Achtung zwei Spielfelder ***/
		// 1. Feld aktuelle Generation - grid[x][y]
		// 2. Feld kommende Generation - newGrid [x][y]

	}

	@Override
	public void toggleCell(int x, int y) {
		grid[x][y] = !grid[x][y];
	}

	/**
	 * Replaces the current grid with the new one
	 */
	private void applyNewGrid() {
		// swap grids to save memory
		boolean[][] temp = grid;
		grid = newGrid;
		newGrid = temp;
	}

	/**
	 * Counts all living neighbour cells of a specific cell.</br></br>
	 * 
	 * HINT: To avoid the hassle with
	 * <code>ArrayIndexOutOfBoundsException</code>s use a try/catch-block!
	 * 
	 * @param x
	 *            x-coordinate of the specific cell
	 * @param y
	 *            y-coordinate of the specific cell
	 * @return number of living neighbour cells (0-8 is possible)
	 */
	private int countNeighbours(int x, int y) {
		int number = 0;
		// TODO: implement
		return number;
	}

	/**
	 * Sets a specific cell in the current grid to the state dead.
	 * 
	 * @param x
	 *            x-coordinate of the specific cell
	 * @param y
	 *            y-coordinate of the specific cell
	 */
	private void setDeadCell(int x, int y) {
		grid[x][y] = false;
	}

	/**
	 * Sets a specific cell in the current grid to the state living.
	 * 
	 * @param x
	 *            x-coordinate of the specific cell
	 * @param y
	 *            y-coordinate of the specific cell
	 */
	private void setLivingCell(int x, int y) {
		grid[x][y] = true;
	}

	/**
	 * Sets a specific cell in the next grid to the state dead.
	 * 
	 * @param x
	 *            x-coordinate of the specific cell
	 * @param y
	 *            y-coordinate of the specific cell
	 */
	private void setNewDeadCell(int x, int y) {
		newGrid[x][y] = false;
	}

	/**
	 * Sets a specific cell in the next grid to the state living.
	 * 
	 * @param x
	 *            x-coordinate of the specific cell
	 * @param y
	 *            y-coordinate of the specific cell
	 */
	private void setNewLivingCell(int x, int y) {
		newGrid[x][y] = true;
	}
}
