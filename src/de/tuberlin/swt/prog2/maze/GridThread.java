package de.tuberlin.swt.prog2.maze;

// Achtung neu hinzugefuegt +++++++

import java.awt.Point;

public class GridThread extends Thread {

	/**
	 * Current grid that can be read safely and holds the current generation.
	 */
	private final Grid grid;

	private final Point start;

	private Point end = null;

	public GridThread(Grid grid, Point start, Point end) {
		this.grid = grid;
		this.start = start;
		this.end = end;
	}

	@Override
	public void run() {
		// while(GridCanvas.running) {
		try {
			grid.newGeneration(start, end);
			Thread.sleep(100);
		} catch (Exception e) {
		}
		// }
	}
}