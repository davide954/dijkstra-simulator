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
 */
public class DijkstraSolver {
    private Cell[][] grid;
    private Cell startCell;
    private Cell endCell;
    private GridPanel gridPanel; // Used for visual updates
    private int delay; // Delay for visualization steps

    /**
     * Constructs a DijkstraSolver.
     * 
     * @param grid      The 2D array of Cell objects.
     * @param startCell The starting cell for the pathfinding.
     * @param endCell   The target cell for the pathfinding.
     * @param gridPanel The GridPanel instance to request repaints for
     *                  visualization.
     * @param delay     The delay in milliseconds between each visualization step.
     */
    public DijkstraSolver(Cell[][] grid, Cell startCell, Cell endCell, GridPanel gridPanel, int delay) {
        this.grid = grid;
        this.startCell = startCell;
        this.endCell = endCell;
        this.gridPanel = gridPanel;
        this.delay = delay;
    }
    
    /**
     * Executes Dijkstra's algorithm to find the shortest path.
     * Visualizes the process step by step.
     * @return true if a path is found, false otherwise.
     * @throws InterruptedException if the thread is interrupted during the delay.
     */
    public boolean solve() throws InterruptedException {
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
                return true;
            }

            // If the cell has already been processed, skip it
            if (closedSet.contains(current)) {
                continue;
            }

            closedSet.add(current);
            // Mark cell as visited, but not start/end to keep their distinct colors
            if (!current.isStart && !current.isEnd) {
                current.isVisited = true;
            }
            current.isCurrent = true; // Mark as current cell for visualization

            // Visualize current step on the EDT (Event Dispatch Thread)
            SwingUtilities.invokeLater(() -> gridPanel.repaint());
            Thread.sleep(delay); // Pause for visualization

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

                    // Remove and re-add to update its priority in the PriorityQueue.
                    // This simulates a "decrease-key" operation.
                    openSet.remove(neighbor);
                    openSet.add(neighbor);
                }
            }
        }
        return false; // No path found
    }

}