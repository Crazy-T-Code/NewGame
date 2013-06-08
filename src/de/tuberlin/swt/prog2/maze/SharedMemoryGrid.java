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
	// private ArrayList<GridThread> threads = new ArrayList<GridThread>();

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
		// TODO: implement

		this.rows = rows;
		this.cols = cols;
		this.neighboursLeadToBirth = bornRule;
		this.neighboursLeadToSurvival = surviveRule;

		generation = 0;
		grid = new boolean[cols][rows];
		newGrid = new boolean[cols][rows];
	}

	@Override
	public void createInitialFigure() {
		int midX = getCols() / 2;
		int midY = getRows() / 2;
		for (int x = midX - 5; x < midX + 5; x++)
			for (int y = midY - 5; y < midY + 5; y++) {
				setLivingCell(x, y);
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

	@Override
	public void newGeneration() {
		newGeneration(new Point(0, 0), new Point(cols - 1, rows - 1));
	}

	@Override
	public void newGeneration(Point start, Point end) {
		generation++;

		for (int x = start.x; x <= end.x; x++) {
			for (int y = start.y; y <= end.y; y++) {

				int neighbours = countNeighbours(x, y);
				if (isAlive(x, y)) {
					/*** survive rule ***/
					// wenn die Zelle lebt und Anzahl Nachbarzellen, lebend >= 1
					// und <= 5 dann Zelle lebt
					boolean survive = false;
					for (int lLifeCount : neighboursLeadToSurvival) {
						if (neighbours == lLifeCount)
							survive = true;
					}
					if (survive) {
						// zelle überlebt
						setNewLivingCell(x, y);
					} else {
						// zelle überlebt nicht => death
						setNewDeadCell(x, y);
					}
				} else {
					// is Dead
					/*** born rule ***/
					// wenn Zelle tot und Anzahl Nachbarzellen, lebend == 3
					// dann Zelle wird geboren -> lebt
					boolean birth = false;
					for (int lLifeCount : neighboursLeadToBirth) {
						if (neighbours == lLifeCount)
							birth = true;
					}
					if (birth) {
						setNewLivingCell(x, y);
					} else {
						setNewDeadCell(x, y);
					}
				}
				/***
				 * sonst, alle anderen Fälle - Zellen sind schon tot
				 ***/

				/*** Achtung zwei Spielfelder ***/
				// 1. Feld aktuelle Generation - grid[x][y] -> nur lesend
				// 2. Feld kommende Generation - newGrid [x][y] -> schreibend
			}
		}
		applyNewGrid();
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

		for (Point lNeighbour : NEIGHBOURS) {
			try {
				if (isAlive(x + lNeighbour.x, y + lNeighbour.y))
					number++;
			} catch (Exception e) {

			}
		}
		return number;
	}

	/**
	 * TODO comment
	 * 
	 * @param aX
	 * @param aY
	 * @return
	 */
	private boolean isAlive(int x, int y) {
		return grid[x][y];
	}

	private void printNeighbours(int x, int y) {
		for (Point lNeighbour : NEIGHBOURS) {
			System.out.println("x:" + (x + lNeighbour.x) + ", y:"
			        + (y + lNeighbour.y));
			try {
				if (isAlive(x + lNeighbour.x, y + lNeighbour.y))
					System.out.println('+');
				else
					System.out.println('-');
			} catch (Exception e) {
				System.out.println('~');
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
}
