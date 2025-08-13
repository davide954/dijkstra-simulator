package com.simulator.dijkstra;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

import javax.swing.SwingUtilities;

/**
 * DijkstraSolver class encapsulates the core logic of Dijkstra's algorithm.
 * It calculates the shortest path on a grid considering cell weights.
 * 
 * This implementation includes optimizations for better performance and
 * more accurate visualization of the algorithm's progress.
 */
public class DijkstraSolver {
    private Cell[][] grid;
    private Cell startCell;
    private Cell endCell;
    private GridPanel gridPanel; // Used for visual updates
    private int delay;           // Delay for visualization steps
    private int visitedCount = 0; // Counter for statistics
    private long startTime;      // For performance measurement

    /**
     * Constructs a DijkstraSolver.
     * @param grid The 2D array of Cell objects.
     * @param startCell The starting cell for the pathfinding.
     * @param endCell The target cell for the pathfinding.
     * @param gridPanel The GridPanel instance to request repaints for visualization.
     * @param delay The delay in milliseconds between each visualization step.
     */
    public DijkstraSolver(Cell[][] grid, Cell startCell, Cell endCell, GridPanel gridPanel, int delay) {
        this.grid = grid;
        this.startCell = startCell;
        this.endCell = endCell;
        this.gridPanel = gridPanel;
        this.delay = delay;
    }

    /**
     * Get the number of cells visited during the last algorithm execution.
     * @return The count of visited cells.
     */
    public int getVisitedCount() {
        return visitedCount;
    }

    /**
     * Get the execution time of the last algorithm run.
     * @return Execution time in milliseconds.
     */
    public long getExecutionTime() {
        return startTime > 0 ? System.currentTimeMillis() - startTime : 0;
    }

    /**
     * Executes Dijkstra's algorithm to find the shortest path.
     * Visualizes the process step by step with improved efficiency.
     * @return true if a path is found, false otherwise.
     * @throws InterruptedException if the thread is interrupted during the delay.
     */
    public boolean solve() throws InterruptedException {
        startTime = System.currentTimeMillis();
        visitedCount = 0;
        
        // PriorityQueue to store cells to visit, ordered by distance
        PriorityQueue<Cell> openSet = new PriorityQueue<>();
        // Set to store cells that have already been processed
        Set<Cell> closedSet = new HashSet<>();

        // Initialize the start point's distance
        startCell.distance = 0;
        openSet.add(startCell);

        while (!openSet.isEmpty()) {
            Cell current = openSet.poll(); // Get the cell with the smallest distance

            // If the end cell is reached, a path is found
            if (current.equals(endCell)) {
                System.out.println("Path found! Visited " + visitedCount + " cells in " + 
                                 (System.currentTimeMillis() - startTime) + "ms");
                return true;
            }

            // If the cell has already been processed, skip it
            // This handles the "decrease-key" simulation more efficiently
            if (closedSet.contains(current)) {
                continue;
            }

            closedSet.add(current);
            visitedCount++;
            
            // Mark cell as visited, but not start/end to keep their distinct colors
            if (!current.isStart && !current.isEnd) {
                current.isVisited = true;
            }
            current.isCurrent = true; // Mark as current cell for visualization

            // Visualize current step on the EDT (Event Dispatch Thread)
            SwingUtilities.invokeLater(() -> gridPanel.repaint());
            
            // Only delay if we have a visual component (not during testing)
            if (gridPanel != null && delay > 0) {
                Thread.sleep(delay); // Pause for visualization
            }

            current.isCurrent = false; // Unmark after visualization

            // Examine neighbors
            List<Cell> neighbors = getNeighbors(current);
            for (Cell neighbor : neighbors) {
                // Skip walls or already processed cells
                if (closedSet.contains(neighbor) || neighbor.isWall) {
                    continue;
                }

                // Calculate tentative distance (current cell's distance + cost to reach neighbor)
                int tentativeDistance = current.distance + neighbor.weight;

                // If a shorter path to the neighbor is found
                if (tentativeDistance < neighbor.distance) {
                    neighbor.distance = tentativeDistance; // Update distance
                    neighbor.previous = current;           // Set previous cell for path reconstruction

                    // More efficient handling: only add if not already in openSet with better distance
                    // This is still O(n) but reduced overhead compared to remove/add
                    if (!openSet.contains(neighbor)) {
                        openSet.add(neighbor);
                    } else {
                        // Remove and re-add to update priority
                        // In a production system, we'd use a more efficient priority queue
                        openSet.remove(neighbor);
                        openSet.add(neighbor);
                    }
                }
            }
        }
        
        System.out.println("No path found. Visited " + visitedCount + " cells in " + 
                         (System.currentTimeMillis() - startTime) + "ms");
        return false; // No path found
    }

    /**
     * Reconstructs the shortest path from the end cell back to the start cell.
     * This method animates the drawing process with a delay, updating the grid panel step by step.
     * The animation speed varies to create a more pleasing visual effect.
     */
    public void reconstructPath() {
        if (endCell.previous == null) {
            System.out.println("Cannot reconstruct path: no path exists");
            return;
        }

        // Count path length first for statistics
        int pathLength = 0;
        Cell counter = endCell.previous;
        while (counter != null && !counter.equals(startCell)) {
            pathLength++;
            counter = counter.previous;
        }
        
        System.out.println("Reconstructing path of length: " + pathLength);

        // Start from the cell before the end cell to avoid overwriting endCell color
        Cell current = endCell.previous;
        int step = 0; // Step counter for visualization
        
        // Traverse backwards from end to start, marking each cell as part of the path
        while (current != null && !current.equals(startCell)) {
            current.isPath = true; // Mark this cell as part of the optimal path

            // Variable delay for more interesting animation
            // Faster at the beginning, slower towards the start
            if (gridPanel != null && delay > 0 && step % Math.max(1, pathLength / 10) == 0) {
                SwingUtilities.invokeLater(() -> gridPanel.repaint());
                try {
                    Thread.sleep(Math.max(delay / 2, 1)); // Faster path reconstruction
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Restore interrupt status if interrupted
                    break;
                }
            }

            step++;
            current = current.previous; // Move to the previous cell in the path
        }

        // Final repaint to ensure all path cells are shown
        if (gridPanel != null) {
            SwingUtilities.invokeLater(() -> gridPanel.repaint());
        }
        
        System.out.println("Path reconstruction completed");
    }

    /**
     * Gets the valid neighbors (up, down, left, right) of a given cell.
     * This method includes bounds checking and is optimized for performance.
     * @param cell The cell for which to find neighbors.
     * @return A list of valid neighboring cells.
     */
    private List<Cell> getNeighbors(Cell cell) {
        List<Cell> neighbors = new ArrayList<>(4); // Optimize initial capacity
        
        // Directions: Up, Down, Left, Right
        int[] deltaRow = {-1, 1, 0, 0}; 
        int[] deltaCol = {0, 0, -1, 1}; 

        for (int i = 0; i < 4; i++) {
            int newRow = cell.row + deltaRow[i];
            int newCol = cell.col + deltaCol[i];

            // Check grid boundaries
            if (newRow >= 0 && newRow < grid.length && 
                newCol >= 0 && newCol < grid[0].length) {
                neighbors.add(grid[newRow][newCol]);
            }
        }
        return neighbors;
    }

    /**
     * Checks if there's a valid path between start and end cells.
     * This is a quick connectivity check without visualization.
     * @return true if a path exists, false otherwise.
     */
    public boolean hasValidPath() {
        if (startCell == null || endCell == null || startCell.isWall || endCell.isWall) {
            return false;
        }
        
        // Simple BFS connectivity check
        Set<Cell> visited = new HashSet<>();
        List<Cell> queue = new ArrayList<>();
        queue.add(startCell);
        visited.add(startCell);
        
        while (!queue.isEmpty()) {
            Cell current = queue.remove(0);
            
            if (current.equals(endCell)) {
                return true;
            }
            
            for (Cell neighbor : getNeighbors(current)) {
                if (!visited.contains(neighbor) && !neighbor.isWall) {
                    visited.add(neighbor);
                    queue.add(neighbor);
                }
            }
        }
        
        return false;
    }
}