package com.simulator.dijkstra;

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

    @Override
    public int compareTo(Cell other) {
        return Integer.compare(this.distance, other.distance);
    }
}
