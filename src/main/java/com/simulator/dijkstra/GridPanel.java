package com.simulator.dijkstra;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * GridPanel class for drawing the grid and handling user interactions
 * such as creating walls, changing cell weights, and moving start/end points.
 */
public class GridPanel extends JPanel {
    private static final int CELL_SIZE = 30;

    // Colors for visualization
    private static final Color COLOR_START = new Color(0, 153, 0); // Green
    private static final Color COLOR_END = new Color(153, 0, 0); // Red
    private static final Color COLOR_WALL = new Color(0, 0, 0); // Black
    private static final Color COLOR_EMPTY = new Color(255, 255, 255); // White
    private static final Color COLOR_VISITED = new Color(173, 216, 230); // Light Blue
    private static final Color COLOR_PATH = new Color(255, 255, 0); // Yellow
    private static final Color COLOR_CURRENT = new Color(255, 165, 0); // Orange

    private Cell[][] grid;
    private DijkstraPathfinding mainFrame; // Reference to the main frame
    private Cell draggedCell = null;
    private boolean isDraggingStart = false;
    private boolean isDraggingEnd = false;

    // To track if we are initially toggling a wall or drawing it
    private Boolean isDrawingWall = null; // true if drawing, false if erasing, null if not drawing

    /**
     * Constructs a GridPanel.
     * 
     * @param grid      The 2D array of Cell objects representing the grid.
     * @param mainFrame A reference to the main DijkstraPathfinding frame for
     *                  interaction.
     */
    public GridPanel(Cell[][] grid, DijkstraPathfinding mainFrame) {
        this.grid = grid;
        this.mainFrame = mainFrame;
        setPreferredSize(new Dimension(grid[0].length * CELL_SIZE, grid.length * CELL_SIZE));
        addMouseListeners();
    }
}