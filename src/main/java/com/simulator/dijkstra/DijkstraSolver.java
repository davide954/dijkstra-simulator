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
}