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

    // Mouse listeners will be added in a subsequent commit

    /**
     * Paints the component. This method is called by the Swing framework.
     * 
     * @param g The Graphics object to protect.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Iterate through all cells and draw them
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[0].length; col++) {
                Cell cell = grid[row][col];
                drawCell(g2d, cell);
            }
        }
    }

    /**
     * Draws a single cell on the grid.
     * 
     * @param g2d  The Graphics2D object to draw on.
     * @param cell The Cell object to draw.
     */
    private void drawCell(Graphics2D g2d, Cell cell) {
        int x = cell.col * CELL_SIZE;
        int y = cell.row * CELL_SIZE;

        Color cellColor = COLOR_EMPTY; // Default color

        // Determine cell color based on its state, prioritizing path, current, visited,
        // then empty
        if (cell.isWall) {
            cellColor = COLOR_WALL;
        } else if (cell.isPath) {
            cellColor = COLOR_PATH;
        } else if (cell.isCurrent) {
            cellColor = COLOR_CURRENT;
        } else if (cell.isVisited) {
            cellColor = COLOR_VISITED;
        }

        g2d.setColor(cellColor);
        g2d.fillRect(x, y, CELL_SIZE, CELL_SIZE);

        // Draw cell border
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.drawRect(x, y, CELL_SIZE, CELL_SIZE);

        // Draw start and end points on top of everything
        if (cell.isStart) {
            g2d.setColor(COLOR_START);
            g2d.fillOval(x + CELL_SIZE / 4, y + CELL_SIZE / 4, CELL_SIZE / 2, CELL_SIZE / 2);
        } else if (cell.isEnd) {
            g2d.setColor(COLOR_END);
            g2d.fillRect(x + CELL_SIZE / 4, y + CELL_SIZE / 4, CELL_SIZE / 2, CELL_SIZE / 2);
        }

        // Display cell weight (only if different from 1 and not a special cell)
        if (cell.weight > 1 && !cell.isWall && !cell.isStart && !cell.isEnd) {
            g2d.setColor(Color.DARK_GRAY);
            g2d.setFont(new Font("Arial", Font.BOLD, CELL_SIZE / 3));
            String weightStr = String.valueOf(cell.weight);
            FontMetrics fm = g2d.getFontMetrics();
            int strX = x + (CELL_SIZE - fm.stringWidth(weightStr)) / 2;
            int strY = y + (fm.getAscent() + (CELL_SIZE - fm.getHeight())) / 2;
            g2d.drawString(weightStr, strX, strY);
        }
    }
}
