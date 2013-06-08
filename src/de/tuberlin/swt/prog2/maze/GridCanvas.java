package de.tuberlin.swt.prog2.maze;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;

/**
 * Canvas that paints a Grid for a cellular automaton.
 */
public class GridCanvas extends Canvas {
	private static final long serialVersionUID = 8113191369379565595L;

	/**
	 * Grid that should be paint in this canvas.
	 */
	private final Grid grid;

	/**
	 * Size of one square cell. (cellSize * cellSize)
	 */
	private final int cellSize;

	/**
	 * Label that shows the number of the current generation.
	 */
	private final JLabel lGenerationsNumber;

	/**
	 * Color of an living cell.
	 */
	private static final Color CELL_COLOR = Color.BLUE;

	/**
	 * Color of the grid lines.
	 */
	private static final Color GRID_COLOR = Color.GRAY;

	/**
	 * Minimum time in milliseconds between the painting of two generations.
	 */
	private static final int TIME_BETWEEN_PAINTINGS = 30;

	/**
	 * Indicator if the calculation of a new generation should be done.
	 */
	boolean running = false;

	Thread worker = null;

	/**
	 * Constructor
	 * 
	 * @param grid
	 *            Grid that should be paint within this canvas
	 * @param cellSize
	 *            Size of one side of a cell
	 * @param lGenerationsNumber
	 *            Label where the current generation should be shown
	 */
	public GridCanvas(Grid grid, int cellSize, JLabel lGenerationsNumber) {
		this.grid = grid;
		this.cellSize = cellSize;
		this.lGenerationsNumber = lGenerationsNumber;

		// mouseListener to toggle cells from dead to living state or the other
		// way around
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				toggleCell(e.getX(), e.getY());
			}
		});
	}

	@Override
	public Dimension getMaximumSize() {
		return new Dimension(grid.getCols() * cellSize + 1, grid.getRows()
		        * cellSize + 1);
	}

	@Override
	public Dimension getMinimumSize() {
		return new Dimension(grid.getCols() * cellSize + 1, grid.getRows()
		        * cellSize + 1);
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(grid.getCols() * cellSize + 1, grid.getRows()
		        * cellSize + 1);
	}

	/**
	 * Paints the whole grid-canvas including the grid with living cells and the
	 * generation counter.
	 */
	@Override
	public void paint(Graphics g) {
		// painting the grid-lines

		for (int x = 0; x < grid.getCols() + 1; x++) {
			g.setColor(GRID_COLOR);
			g.drawLine(x * cellSize, 0, x * cellSize, grid.getRows() * cellSize);
		}
		for (int y = 0; y < grid.getRows() + 1; y++) {
			g.setColor(GRID_COLOR);
			g.drawLine(0, y * cellSize, grid.getCols() * cellSize, y * cellSize);
		}

		// painting the living cells (Point (1,1) is the upper left cell)
		for (int x = 0; x < grid.getRows(); x++) {
			for (int y = 0; y < grid.getCols(); y++) {
				if (grid.getCell(x, y)) { // living cell
					g.setColor(CELL_COLOR);
				} else { // dead cell
					g.setColor(getBackground());
				}
				g.fillRect(x * cellSize + 1, y * cellSize + 1, cellSize - 1,
				        cellSize - 1);
			}
		}

		// update generation counter
		lGenerationsNumber
		        .setText(Integer.toString(grid.getGenerationNumber()));
	}

	/**
	 * Starts a new calculation round that calculates and paints new generations
	 * until canceled with stop().
	 */
	public void start() {

		// TODO: Create new Thread that calculates and paints new generations
		// until stopped.

		if (running) // already started
			return;

		worker = new Thread() {
			@Override
			public void run() {
				try {
					while (true) {
						sleep(TIME_BETWEEN_PAINTINGS);
						grid.newGeneration();
						// System.out.println("New Generation "
						// + grid.getGenerationNumber());
						repaint();
					}
				} catch (InterruptedException ex) {
					// ex.printStackTrace(System.err);
				}
			}
		};
		worker.start();
		running = true;
	}

	/**
	 * Stops the current running calculation of new generations
	 */
	public void stop() {
		// TODO: Stopping current generation calculations and paintings
		if (running && worker.isAlive()) {
			worker.interrupt();
			running = false;
		}
	}

	@Override
	public void update(Graphics g) {
		// only repaint is needed
		paint(g);
	}

	/**
	 * Toggles a specific cell from dead to living state or the other way round.
	 * 
	 * @param x
	 *            x-coordinate of the specific cell
	 * @param y
	 *            y-coordinate of the specific cell
	 */
	protected void toggleCell(int x, int y) {
		grid.toggleCell(x / cellSize, y / cellSize);
		repaint();
	}

}
