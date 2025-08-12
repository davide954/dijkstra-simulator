package com.simulator.dijkstra;

import java.util.Objects;

public class Cell implements Comparable<Cell> {
    public int row;
    public int col;
    public int distance;
    public Cell previous;
    public boolean isVisited;
    public boolean isWall;
    public boolean isStart;
    public boolean isEnd;
    public boolean isPath = false; // To indicate if the cell is part of the final path
    public boolean isCurrent; // To highlight the cell currently being processed
    public int weight; // New: weight to traverse the cell

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
     * Resets the cell's state for a new algorithm execution,
     * while preserving its wall, start, and end status.
     */
    public void reset() {
        this.distance = Integer.MAX_VALUE;
        this.previous = null;
        this.isVisited = false;
        this.isPath = false;
        this.isCurrent = false;
    }

    /**
     * Compares cells based on their distance for the PriorityQueue.
     * 
     * @param other The other cell to compare to.
     * @return A negative integer, zero, or a positive integer as this object
     *         is less than, equal to, or greater than the specified object.
     */
    @Override
    public int compareTo(Cell other) {
        return Integer.compare(this.distance, other.distance);
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     * Required for correct functioning with Set and Map collections.
     * 
     * @param o The reference object with which to compare.
     * @return true if this object is the same as the obj argument; false otherwise.
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
     * Returns a hash code value for the object.
     * Required for correct functioning with Set and Map collections.
     * 
     * @return A hash code value for this object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }
}