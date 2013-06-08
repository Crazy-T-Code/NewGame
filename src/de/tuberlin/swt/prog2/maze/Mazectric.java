package de.tuberlin.swt.prog2.maze;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * Simple graphical Mazectric-Game. Further informations about Mazectric can be
 * found under: http://conwaylife.com/wiki/index.php?title=Maze
 */
public class Mazectric extends JFrame {
	private static final long serialVersionUID = 8281821359272768123L;

	/**
	 * Number of rows in the grid
	 */
	private static int rows = 225;
	/**
	 * Number of columns in the grid
	 */
	private static int cols = 225;

	/**
	 * Size of one square cell. (cellSize * cellSize)
	 */
	private static int cellSize = 4;
	private static GridCanvas canvas;

	/**
	 * Start-button
	 */
	private static JButton bStart;
	/**
	 * Stop-button
	 */
	private static JButton bStop;

	/**
	 * Creates a new Mazectric-Game GUI
	 */
	public static void addComponentsToPane(Container pane) {
		pane.setLayout(new GridBagLayout());

		// Mazectric-Rule set
		// Neighbours needed to survive: 1, 2, 3, 4 or 5
		int[] surviveRule = { 1, 2, 3, 4, 5 };
		// Neighbours needed to be born: 3
		int[] bornRule = { 3 };

		// int[] surviveRule = { 1, 3, 5 ,7 };
		// int[] bornRule = { 1, 3, 5 ,7 };

		Grid grid = null;
		// TODO: Create a real grid (using the surviveRule/bornRule arrays and
		// rows/cols) an set the initial figure.

		// create GUI
		JLabel lGenerationsNumber = new JLabel();

		canvas = new GridCanvas(grid, cellSize, lGenerationsNumber);
		bStart = new JButton("Start");
		bStop = new JButton("Stop");
		bStop.setEnabled(false);
		bStart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				start();
			}
		});

		bStop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				stop();
			}
		});

		Container controlPane = new Container();
		controlPane.setLayout(new GridBagLayout());
		GridBagConstraints constraint = new GridBagConstraints();

		JLabel lGenerations = new JLabel("Generation: ");

		constraint.fill = GridBagConstraints.HORIZONTAL;
		constraint.gridx = 0;
		constraint.gridy = 0;
		constraint.gridwidth = 2;
		controlPane.add(lGenerations, constraint);

		constraint.gridx = 2;
		constraint.gridy = 0;
		constraint.ipadx = 10;
		constraint.gridwidth = 1; // set to default
		controlPane.add(lGenerationsNumber, constraint);

		constraint.gridx = 1;
		constraint.gridy = 1;
		constraint.insets = new Insets(10, 5, 0, 5); // vertical and horizontal
		                                             // space
		controlPane.add(bStart, constraint);

		constraint.gridx = 1;
		constraint.gridy = 2;
		controlPane.add(bStop, constraint);

		JLabel space = new JLabel();
		constraint.gridx = 2;
		constraint.gridy = 2;
		constraint.gridwidth = 2;
		constraint.insets = new Insets(0, 25, 0, 0); // horizontal space
		controlPane.add(space, constraint);

		pane.add(canvas);
		pane.add(controlPane);
	}

	/**
	 * Main method that starts the Mazectric-Game
	 */
	public static void main(String[] args) {
		// create GUI-Thread
		Thread gui = new Thread(new Runnable() {
			@Override
			public void run() {
				createAndShowGUI();
			}
		});

		// start GUI
		gui.start();
	}

	/**
	 * Start-button was clicked.
	 */
	protected static void start() {
		bStop.setEnabled(true);
		bStart.setEnabled(false);
		canvas.start();
	}

	/**
	 * Stop-button was clicked.
	 */
	protected static void stop() {
		bStop.setEnabled(false);
		bStart.setEnabled(true);
		canvas.stop();
	}

	private static void createAndShowGUI() {
		Mazectric maze = new Mazectric();
		maze.setDefaultCloseOperation(EXIT_ON_CLOSE);

		addComponentsToPane(maze.getContentPane());

		maze.pack();
		maze.setVisible(true);
	}
}
