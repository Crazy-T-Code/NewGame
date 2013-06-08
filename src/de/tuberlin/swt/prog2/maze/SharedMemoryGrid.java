package de.tuberlin.swt.prog2.maze;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;

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
	private ArrayList<GridThread> threads = new ArrayList<GridThread>();

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

		this.rows = rows;
		this.cols = cols;
		this.neighboursLeadToBirth = bornRule;
		this.neighboursLeadToSurvival = surviveRule;
		this.generation = generation;
		this.grid = new boolean[this.cols][this.rows];
		this.newGrid = new boolean[this.cols][this.rows];

	}

	@Override
	public int getRows() {
		return rows;
	}

	@Override
	public int getCols() {
		return cols;
	}

	@Override
	public void toggleCell(int x, int y) {
		grid[x][y] = !grid[x][y];
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

	@Override
	public boolean getCell(int x, int y) {
		return grid[x][y];
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
		/*
		 * for-Schleife bis neighbours if-Abfrage getCell number++
		 */
		for (int i = 0; i < NEIGHBOURS.length; i++) {
			try {
				Point s = new Point(x + NEIGHBOURS[i].x, y + NEIGHBOURS[i].y);
				// s=s+NEIGHBOURS[i];
				// if (grid[s.x][s.y])
				if (getCell(x + NEIGHBOURS[i].x, y + NEIGHBOURS[i].y)) {
					number++;
				}
			} catch (ArrayIndexOutOfBoundsException e) {
				e.printStackTrace();
			}
		}
		return number;
	}

	@Override
	public void newGeneration() {
		/*
		 * threads adden
		 * 
		 * threads starten
		 * 
		 * threads joinen
		 * 
		 * newGen++
		 */
		GridThread threadOne = new GridThread(SharedMemoryGrid.this, new Point(
				1, 1), new Point((int) (getCols() / 2), getRows() - 1));
		GridThread threadTwo = new GridThread(SharedMemoryGrid.this, new Point(
				(int) (getCols() / 2), 1), new Point(getRows() - 1,
				getCols() - 1));

		threadOne.start();
		threadTwo.start();

		try {
			threadOne.join();
			threadTwo.join();
			applyNewGrid();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		this.generation++;
	}

	@Override
	public void newGeneration(Point start, Point end) {
		/*
		 * check rows and column if alive or dead 2 for-loops
		 * 
		 * if cell is dead if it has to die else live if cell is alive if it has
		 * to die else keep living
		 */

		/*
		 * 
		 * 
		 * if(getCell(r,c)) { // check whether it has to die
		 * if(inArray(countNeighbours(r, c), neighboursLeadToSurvival)) {
		 * setNewLivingCell(r, c); } else { setNewDeadCell(r, c); } } else { //
		 * cell is dead // check whether the dead cell will be born
		 * if(inArray(countNeighbours(r, c), neighboursLeadToBirth)) {
		 * setNewLivingCell(r, c); } else { setNewDeadCell(r,c);
		 */
		this.generation++;
		for (int r = start.x; r < end.x; r++) {
			for (int c = start.y; c < end.y; c++) {
				// living cell
				if (getCell(r, c)) {
					// it keeps living
					for (int i = 0; i < neighboursLeadToSurvival.length; i++) {
						// FIXME: like it is here the cell will only survive, if it
						// has exactly the last number of the neighboursLeadToSurvival
						if (neighboursLeadToSurvival[i] == countNeighbours(r, c)) {
							setNewLivingCell(r, c);
						} else {
							// it dies
							setNewDeadCell(r, c);
						}
					}
					// dead cell
				} else {
					for (int i = 0; i < neighboursLeadToBirth.length; i++) {
						// revived
						// FIXME: like it is here the cell will only survive, if it
						// has exactly the last number of the neighboursLeadToSurvival
						// BTW: in the current task, there is only 1 neighboursLeadToBirth
						// and it will execute correctly, but it isn't correct at all.
						if (neighboursLeadToBirth[i] == countNeighbours(r, c)) {
							setNewLivingCell(r, c);
						} else {
							// it dies
							setNewDeadCell(r, c);
						}
					}
				}
			}
		}

	}

	public boolean inArray(int number, int[] array) {
		for (int i = 0; i < array.length; i++) {
			if (number == array[i]) {
				return true;
			}
		}
		return false;
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

	@Override
	public void createInitialFigure() {

		/*
		 * int(rows/2-4 bis rows/2+5)
		 */

		for (int r = (int) (rows / 2 - 4); r <= (int) (rows / 2 + 5); r++) {
			for (int c = (int) (cols / 2 - 4); c <= (int) (cols / 2 + 5); c++) {
				setLivingCell(r, c);
				setNewLivingCell(r, c);
			}
		}
	}

	@Override
	public int getGenerationNumber() {
		return generation;
	}

	private class GridThread extends Thread {

		private Point start;
		private Point end;
		private SharedMemoryGrid sharedMemoryGrid;

		public GridThread(SharedMemoryGrid sharedMemoryGrid, Point start,
				Point end) {
			this.sharedMemoryGrid = sharedMemoryGrid;
			this.start = start;
			this.end = end;
		}

		public void run() {
			sharedMemoryGrid.newGeneration(start, end);
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
