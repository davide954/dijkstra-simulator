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

    /**
     * Adds mouse listeners for interaction (click, drag).
     */
    private void addMouseListeners() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // Prevent interaction if algorithm is running
                if (mainFrame.isRunning())
                    return;

                int col = e.getX() / CELL_SIZE;
                int row = e.getY() / CELL_SIZE;

                if (isValidCell(row, col)) {
                    Cell clickedCell = grid[row][col];

                    if (SwingUtilities.isLeftMouseButton(e)) {
                        if (e.isShiftDown()) {
                            // Shift + Left-click to set start cell
                            if (!clickedCell.isWall && !clickedCell.isEnd) {
                                mainFrame.setStartCell(clickedCell);
                                mainFrame.clearPath(); // Clear old path state as start point moved
                            }
                        } else if (e.isControlDown()) {
                            // Control + Left-click to set end cell
                            if (!clickedCell.isWall && !clickedCell.isStart) {
                                mainFrame.setEndCell(clickedCell);
                                mainFrame.clearPath(); // Clear old path state as end point moved
                            }
                        } else if (clickedCell.equals(mainFrame.getStartCell())) {
                            draggedCell = clickedCell;
                            isDraggingStart = true;
                        } else if (clickedCell.equals(mainFrame.getEndCell())) {
                            draggedCell = clickedCell;
                            isDraggingEnd = true;
                        } else {
                            // Toggle wall if no modifier keys are pressed
                            isDrawingWall = !clickedCell.isWall;
                            applyWallChange(clickedCell, isDrawingWall);
                        }
                    } else if (SwingUtilities.isRightMouseButton(e)) {
                        // Change weight (e.g., 1 -> 5 -> 1)
                        if (!clickedCell.isWall && !clickedCell.isStart && !clickedCell.isEnd) {
                            clickedCell.weight = (clickedCell.weight == 1) ? 5 : 1; // Toggle weight between 1 and 5
                            mainFrame.clearPath(); // Clear path if weights change
                        }
                    }
                    repaint(); // Redraw the grid after interaction
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                draggedCell = null;
                isDraggingStart = false;
                isDraggingEnd = false;
                isDrawingWall = null; // Reset wall drawing state
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                // Prevent interaction if algorithm is running
                if (mainFrame.isRunning())
                    return;

                int newCol = e.getX() / CELL_SIZE;
                int newRow = e.getY() / CELL_SIZE;

                if (isValidCell(newRow, newCol)) {
                    Cell newCell = grid[newRow][newCol];

                    if (isDraggingStart) {
                        // Prevent placing start on end or wall
                        if (!newCell.isEnd && !newCell.isWall && !newCell.equals(mainFrame.getStartCell())) {
                            mainFrame.setStartCell(newCell); // Update start cell via main frame
                            mainFrame.clearPath(); // Clear old path state as start point moved
                            repaint();
                        }
                    } else if (isDraggingEnd) {
                        // Prevent placing end on start or wall
                        if (!newCell.isStart && !newCell.isWall && !newCell.equals(mainFrame.getEndCell())) {
                            mainFrame.setEndCell(newCell); // Update end cell via main frame
                            mainFrame.clearPath(); // Clear old path state as end point moved
                            repaint();
                        }
                    } else if (SwingUtilities.isLeftMouseButton(e) && isDrawingWall != null) {
                        // Draw/erase walls by dragging
                        if (!newCell.isStart && !newCell.isEnd) {
                            applyWallChange(newCell, isDrawingWall);
                            repaint(); // Redraw the grid after interaction
                        }
                    }
                }
            }
        });
    }

    /**
     * Applies the wall change to a cell, also resetting its weight if it becomes a
     * wall.
     * 
     * @param cell   The cell to modify.
     * @param isWall True to make it a wall, false to remove it.
     */
    private void applyWallChange(Cell cell, boolean isWall) {
        if (cell.isWall != isWall) { // Only change if state is different
            cell.isWall = isWall;
            if (isWall) {
                cell.weight = 1; // Walls have no weight for pathfinding
                cell.reset(); // Clear any previous pathfinding state on the wall
            } else {
                cell.reset(); // When a wall is removed, clear its state
            }
            mainFrame.clearPath(); // Clear path if walls change
        }
    }

    /**
     * Checks if the given row and column are within the grid boundaries.
     * 
     * @param row The row index.
     * @param col The column index.
     * @return true if the cell is valid, false otherwise.
     */
    private boolean isValidCell(int row, int col) {
        return row >= 0 && row < grid.length && col >= 0 && col < grid[0].length;
    }

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