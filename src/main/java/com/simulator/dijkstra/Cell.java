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
    public boolean isPath = false;
    public boolean isCurrent;

    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
        this.distance = Integer.MAX_VALUE;
        this.previous = null;
        this.isVisited = false;
        this.isWall = false;
        this.isStart = false;
        this.isEnd = false;
        this.isPath = false;
        this.isCurrent = false;
    }

    @Override
    public int compareTo(Cell other) {
        return Integer.compare(this.distance, other.distance);
    }
}
