package com.simulator.dijkstra;

import java.util.Objects;

/**
 * Represents a single cell in the pathfinding grid.
 * <p>
 * Each cell contains position information, pathfinding state, and visual properties
 * used by Dijkstra's algorithm for navigation and visualization.
 * </p>
 * 
 * @author dijkstra-simulator
 * @version 1.4.0
 * @since 1.0.0
 */
public class Cell implements Comparable<Cell> {
    
    /** The row position of this cell in the grid (0-based). */
    public int row;
    
    /** The column position of this cell in the grid (0-based). */
    public int col;
    
    /** 
     * The shortest distance from the start cell to this cell.
     * Initially set to Integer.MAX_VALUE (infinity).
     */
    public int distance;
    
    /** 
     * Reference to the previous cell in the shortest path.
     * Used for path reconstruction after algorithm completion.
     */
    public Cell previous;
    
    /** 
     * Indicates whether this cell has been visited by the pathfinding algorithm.
     * Used for visualization purposes.
     */
    public boolean isVisited;
    
    /** 
     * Indicates whether this cell is an impassable wall.
     * Walls block pathfinding and are rendered in black.
     */
    public boolean isWall;
    
    /** 
     * Indicates whether this cell is the starting point for pathfinding.
     * Only one cell should have this flag set to true.
     */
    public boolean isStart;
    
    /** 
     * Indicates whether this cell is the destination point for pathfinding.
     * Only one cell should have this flag set to true.
     */
    public boolean isEnd;
    
    /** 
     * Indicates whether this cell is part of the final optimal path.
     * Set during path reconstruction after algorithm completion.
     */
    public boolean isPath = false;
    
    /** 
     * Indicates whether this cell is currently being processed by the algorithm.
     * Used for real-time visualization highlighting.
     */
    public boolean isCurrent;
    
    /** 
     * The cost to traverse this cell.
     * Higher weights make the cell more expensive to pass through.
     * Default value is 1.
     */
    public int weight;

    /**
     * Constructs a new Cell with the specified grid position.
     * <p>
     * Initializes the cell with default values:
     * <ul>
     * <li>Distance: Integer.MAX_VALUE (infinity)</li>
     * <li>Weight: 1 (standard traversal cost)</li>
     * <li>All boolean flags: false</li>
     * <li>Previous cell: null</li>
     * </ul>
     * </p>
     * 
     * @param row the row position in the grid (0-based)
     * @param col the column position in the grid (0-based)
     */
    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
        this.distance = Integer.MAX_VALUE; // Initially infinite distance
        this.previous = null;
        this.isVisited = false;
        this.isWall = false;
        this.isStart = false;
        this.isEnd = false;
        this.isPath = false;
        this.isCurrent = false;
        this.weight = 1; // Default: weight 1 for all cells
    }

    /**
     * Resets the cell's pathfinding state for a new algorithm execution.
     * <p>
     * Clears all algorithm-specific properties while preserving the cell's
     * structural properties (wall status, start/end designation, weight).
     * </p>
     * <p>
     * Properties reset:
     * <ul>
     * <li>distance → Integer.MAX_VALUE</li>
     * <li>previous → null</li>
     * <li>isVisited → false</li>
     * <li>isPath → false</li>
     * <li>isCurrent → false</li>
     * </ul>
     * </p>
     * <p>
     * Properties preserved:
     * <ul>
     * <li>row, col (position)</li>
     * <li>isWall, isStart, isEnd (structural)</li>
     * <li>weight (traversal cost)</li>
     * </ul>
     * </p>
     */
    public void reset() {
        this.distance = Integer.MAX_VALUE;
        this.previous = null;
        this.isVisited = false;
        this.isPath = false;
        this.isCurrent = false;
    }

    /**
     * Compares this cell with another cell based on their distances.
     * <p>
     * This comparison is used by priority queues in pathfinding algorithms
     * to process cells in order of shortest distance first.
     * </p>
     * 
     * @param other the other cell to compare to
     * @return a negative integer if this cell has shorter distance,
     *         zero if distances are equal,
     *         a positive integer if this cell has longer distance
     * @throws NullPointerException if the other cell is null
     */
    @Override
    public int compareTo(Cell other) {
        return Integer.compare(this.distance, other.distance);
    }

    /**
     * Indicates whether another object is "equal to" this cell.
     * <p>
     * Two cells are considered equal if they have the same row and column position,
     * regardless of their other properties. This is essential for correct functioning
     * with Set and Map collections.
     * </p>
     * 
     * @param o the reference object with which to compare
     * @return {@code true} if this cell has the same position as the other object;
     *         {@code false} otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Cell cell = (Cell) o;
        return row == cell.row && col == cell.col;
    }

    /**
     * Returns a hash code value for this cell.
     * <p>
     * The hash code is based on the cell's position (row and column),
     * ensuring that cells with the same position have the same hash code.
     * This is required for correct functioning with Set and Map collections.
     * </p>
     * 
     * @return a hash code value for this cell
     */
    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }

    /**
     * Returns a string representation of this cell.
     * <p>
     * The string includes the cell's position, distance, and current state flags.
     * Useful for debugging and logging purposes.
     * </p>
     * 
     * @return a string representation of this cell
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Cell[").append(row).append(",").append(col).append("]");
        sb.append(" dist=").append(distance == Integer.MAX_VALUE ? "∞" : distance);
        sb.append(" weight=").append(weight);
        
        if (isStart) sb.append(" START");
        if (isEnd) sb.append(" END");
        if (isWall) sb.append(" WALL");
        if (isVisited) sb.append(" VISITED");
        if (isPath) sb.append(" PATH");
        if (isCurrent) sb.append(" CURRENT");
        
        return sb.toString();
    }
}