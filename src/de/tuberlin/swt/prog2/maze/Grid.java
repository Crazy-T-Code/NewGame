package de.tuberlin.swt.prog2.maze;

import java.awt.Point;

/**
 * Interface of a grid structure for a cellular automaton.
 */
public interface Grid {

	/**
	 * Creates an initial figure in the grid by setting specific living cells.
	 */
	public void createInitialFigure();

	/**
	 * Getter for the current state (dead or living) of a specific cell.
	 * 
	 * @param x
	 *            x-coordinate of the specific cell
	 * @param y
	 *            y-coordinate of the specific cell
	 * @return current state of the cell which is true for living or false for
	 *         dead
	 */
	public boolean getCell(int x, int y);

	/**
	 * Getter for the number of columns in the grid
	 * 
	 * @return number of columns
	 */
	public int getCols();

	/**
	 * Getter for the current number of calculated generations.
	 * 
	 * @return number of the current generation
	 */
	public int getGenerationNumber();

	/**
	 * Getter for the number of rows in the grid
	 * 
	 * @return number of rows
	 */
	public int getRows();

	/**
	 * Calculates a new generation of the whole grid.
	 */
	public void newGeneration();

	/**
	 * Calculates a new generation of a specific rectangular region of the grid.
	 * 
	 * @param start
	 *            upper left point of the specific rectangular region
	 * @param end
	 *            lower right point of the specific rectangular region
	 */
	public void newGeneration(Point start, Point end);

	/**
	 * Toggles a specific cell from dead to living state or the other way round.
	 * 
	 * @param x
	 *            x-coordinate of the specific cell
	 * @param y
	 *            y-coordinate of the specific cell
	 */
	public void toggleCell(int x, int y);
}
