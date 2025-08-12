package com.simulator.dijkstra;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * Dijkstra Pathfinding Visualizer
 * <p>
 * A Java Swing application that demonstrates the Dijkstra shortest path
 * algorithm
 * with real-time visualization. Users can create walls, move start/end points,
 * and watch the algorithm find the optimal path step by step.
 * <p>
 * Features:
 * - Interactive grid with drag-and-drop functionality
 * - Real-time algorithm visualization
 * - Color-coded cells for different states
 * - Step-by-step pathfinding animation
 * <p>
 * Controls:
 * - Left-click to create/remove walls
 * - Right-click to change cell weight (1 or 5)
 * - Drag green (start) or red (end) points to reposition
 * - Use control buttons to run algorithm, reset grid, or clear path
 *
 * @author bugubster13
 * @version 1.0
 * @since 2025
 */
public class DijkstraPathfinding extends JFrame {
    private static final int ROWS = 20;
    private static final int COLS = 30;
    private static final int CELL_SIZE = 30;
    private static final int DELAY = 5; // Milliseconds delay to visualize steps

    private Cell[][] grid;
    private Cell startCell;
    private Cell endCell;
    private GridPanel gridPanel;

    private boolean isRunning = false;
    private JButton startButton;
    private JLabel statusLabel;

    /**
     * Check if the algorithm is currently running.
     * 
     * @return true if running, false otherwise.
     */
    public boolean isRunning() {
        return isRunning;
    }

    /**
     * Get the start cell.
     * 
     * @return the current start cell.
     */
    public Cell getStartCell() {
        return startCell;
    }

    /**
     * Set the start cell.
     * This method handles updating the old and new start cells' states.
     * 
     * @param cell the new start cell.
     */
    public void setStartCell(Cell cell) {
        // Clear the old start cell's status and reset its pathfinding state
        if (this.startCell != null) {
            this.startCell.isStart = false;
            this.startCell.reset(); // Ensure old start cell is properly reset
        }
        this.startCell = cell;
        this.startCell.isStart = true;
        this.startCell.reset(); // Reset the new start cell's state as well (e.g., distance, previous)
    }

    /**
     * Get the end cell.
     * 
     * @return the current end cell.
     */
    public Cell getEndCell() {
        return endCell;
    }

    /**
     * Set the end cell.
     * This method handles updating the old and new end cells' states.
     * 
     * @param cell the new end cell.
     */
    public void setEndCell(Cell cell) {
        // Clear the old end cell's status and reset its pathfinding state
        if (this.endCell != null) {
            this.endCell.isEnd = false;
            this.endCell.reset(); // Ensure old end cell is properly reset
        }
        this.endCell = cell;
        this.endCell.isEnd = true;
        this.endCell.reset(); // Reset the new end cell's state as well (e.g., distance, previous)
    }

    /**
     * Main constructor for the Dijkstra Pathfinding Visualizer application.
     */
    public DijkstraPathfinding() {
        setTitle("Dijkstra Pathfinding Visualizer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        initializeGrid();
        setupUI();

        pack(); // Sizes the frame so that all its contents are at or above their preferred
                // sizes.
        setLocationRelativeTo(null); // Centers the frame on the screen.
        setVisible(true); // Makes the frame visible.
    }

    /**
     * Initializes the grid with Cell objects and sets default start/end points.
     */
    private void initializeGrid() {
        grid = new Cell[ROWS][COLS];

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                grid[row][col] = new Cell(row, col);
            }
        }

        // Set default start and end points
        // It's crucial to use the setter methods here to ensure proper state
        // initialization
        setStartCell(grid[ROWS / 2][5]);
        setEndCell(grid[ROWS / 2][COLS - 5]);
    }

    /**
     * Sets up the user interface components (grid panel, control panel, instruction
     * panel).
     */
    private void setupUI() {
        setLayout(new BorderLayout());

        // Main grid panel
        gridPanel = new GridPanel(grid, this);
        add(gridPanel, BorderLayout.CENTER);

        // Control panel for buttons and status label
        JPanel controlPanel = new JPanel(new FlowLayout());

        startButton = new JButton("Start Dijkstra");
        startButton.addActionListener(e -> runDijkstra());

        JButton resetButton = new JButton("Reset Grid");
        resetButton.addActionListener(e -> resetGrid());

        JButton clearPathButton = new JButton("Clear Path");
        clearPathButton.addActionListener(e -> clearPath());

        statusLabel = new JLabel(
                "Ready. Shift+Click for Start, Ctrl+Click for End. Drag walls/points. Right-click for weights.");

        controlPanel.add(startButton);
        controlPanel.add(resetButton);
        controlPanel.add(clearPathButton);
        controlPanel.add(statusLabel);

        add(controlPanel, BorderLayout.SOUTH);

        // Instructions panel
        JPanel instructionPanel = new JPanel();
        instructionPanel.setLayout(new BoxLayout(instructionPanel, BoxLayout.Y_AXIS));
        instructionPanel.setBorder(BorderFactory.createTitledBorder("Instructions"));

        instructionPanel.add(new JLabel("• Shift + Left-click to set Start (Green)"));
        instructionPanel.add(new JLabel("• Ctrl + Left-click to set End (Red)"));
        instructionPanel.add(new JLabel("• Left-click & drag to create/remove walls (Black)"));
        instructionPanel.add(new JLabel("• Right-click to change cell weight (Gray numbers)"));
        instructionPanel.add(new JLabel("• Drag green (start) or red (end) points to reposition"));
        instructionPanel.add(new JLabel("• Light Blue: Visited cells, Yellow: Optimal path"));
        instructionPanel.add(new JLabel("• Orange: Current cell being processed"));

        add(instructionPanel, BorderLayout.NORTH);
    }

    /**
     * Performs a complete reset of the grid, clearing walls, weights, and
     * pathfinding states.
     */
    private void resetGrid() {
        if (isRunning)
            return; // Prevent reset while algorithm is running

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                Cell cell = grid[row][col];
                cell.isWall = false;
                cell.weight = 1; // Reset weight to default
                cell.reset(); // Reset pathfinding-specific states including isCurrent
            }
        }
        // Re-set start/end points to default positions, ensuring their status is
        // correct
        // Calling setStartCell/setEndCell will handle resetting the old positions
        setStartCell(grid[ROWS / 2][5]);
        setEndCell(grid[ROWS / 2][COLS - 5]);

        statusLabel.setText("Grid reset.");
        gridPanel.repaint(); // Redraw the grid
    }