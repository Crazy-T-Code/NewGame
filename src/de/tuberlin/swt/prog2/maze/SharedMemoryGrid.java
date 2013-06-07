package de.tuberlin.swt.prog2.maze;

import java.awt.Point;
import java.util.ArrayList;

/**
 * Grid for a cellular automaton that works with four worker-threads and the
 * grid is always shared between all worker- threads.
 */
public class SharedMemoryGrid implements Grid {
	/**
	 * Number of rows in the grid
	 */
	private final int rows;

	/**
	 * Number of columns in the grid
	 */
	private final int cols;

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
	private final ArrayList<GridThread> threads = new ArrayList<GridThread>();

	/**
	 * Array of all numbers of neighbours that lead into surviving cells
	 */
	private final int neighboursLeadToSurvival[];

	/**
	 * Array of all numbers of neighbours that lead into new born cells
	 */
	private final int neighboursLeadToBirth[];

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
		// TODO: implement - done
		this.rows = rows;
		this.cols = cols;
		this.neighboursLeadToSurvival = surviveRule;
		this.neighboursLeadToBirth = bornRule;
		this.grid = new boolean[this.rows][this.cols];
		this.newGrid = new boolean[this.rows][this.cols];
		createInitialFigure();
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
		// TODO: implement - done
		for (int i = 0; i < NEIGHBOURS.length; i++) {
			if (getCell(x + NEIGHBOURS[i].x, y + NEIGHBOURS[i].y)) {
				number++;
			}
		}
		return number;
	}

	@Override
	public void createInitialFigure() {
		// TODO: implement
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				setLivingCell(this.rows / 2 - 5 + i, this.cols / 2 - 5 + j);
				setNewLivingCell(this.rows / 2 - 5 + i, this.cols / 2 - 5 + j);
			}
		}
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

	public boolean inArray(int number, int[] array) {
		for (int i = 0; i < array.length; i++) {
			if (number == array[i]) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void newGeneration() {
		// TODO: implement

		GridThread thread1 = new GridThread(SharedMemoryGrid.this, new Point(1,
		        1), new Point(getCols() / 2, getRows() - 1));
		GridThread thread2 = new GridThread(SharedMemoryGrid.this, new Point(
		        getCols() / 2, 1), new Point(getCols() - 1, getRows() - 1));

		thread1.start();
		thread2.start();

		try {
			thread1.join();
			thread2.join();
		} catch (InterruptedException e) {
		}

		applyNewGrid();
		this.generation++;
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
		/*** sonst, alle anderen Faelle - Zelle tot - setDeadCell(int x, int y) ***/
		// wenn Zelle lebt dann tot
		// wenn Zelle tot dann tot

		/*** Achtung zwei Spielfelder ***/
		// 1. Feld aktuelle Generation - grid[x][y]
		// 2. Feld kommende Generation - newGrid [x][y]

		/*** weiteres ***/
		// jede Zelle hat zwei Zustaende - boolean - lebt:true, tot:false

		// neu
		// prueft fuer alle Zeilen ob die Zelle lebt oder tot ist
		for (int zeile = start.x; zeile < end.x; zeile++) {
			// prueft fuer alle Spalten ob die Zelle lebt oder tot ist
			for (int spalte = start.y; spalte < end.y; spalte++) {
				// if cell is living
				if (getCell(zeile, spalte)) {
					// prueft ob die Zelle sterben muss
					if (inArray(countNeighbours(zeile, spalte),
					        neighboursLeadToSurvival)) {
						setNewLivingCell(zeile, spalte);
					} else {
						setNewDeadCell(zeile, spalte);
					}
				} else { // die Zelle ist tot
					// check whether the dead cell will be born
					if (inArray(countNeighbours(zeile, spalte),
					        neighboursLeadToBirth)) {
						setNewLivingCell(zeile, spalte);
					} else {
						setNewDeadCell(zeile, spalte);
					}
				}

			}
		}

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

	@Override
	public void toggleCell(int x, int y) {
		grid[x][y] = !grid[x][y];
	}
}
