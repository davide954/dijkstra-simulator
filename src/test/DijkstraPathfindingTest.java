package com.simulator.dijkstra;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

/**
 * Unit tests for the Dijkstra Pathfinding application
 * Tests the core functionality of the Cell class and pathfinding logic
 */
public class DijkstraPathfindingTest {

    private Cell cell1;
    private Cell cell2;
    private Cell cell3;

    @Before
    public void setUp() {
        cell1 = new Cell(0, 0);
        cell2 = new Cell(1, 1);
        cell3 = new Cell(0, 0); // Same position as cell1
    }

    @Test
    public void testCellCreation() {
        assertEquals(0, cell1.row);
        assertEquals(0, cell1.col);
        assertFalse(cell1.isStart);
        assertFalse(cell1.isEnd);
        assertFalse(cell1.isWall);
        assertFalse(cell1.isVisited);
        assertFalse(cell1.isPath);
        assertFalse(cell1.isCurrent);
        assertEquals(Integer.MAX_VALUE, cell1.distance);
        assertNull(cell1.previous);
    }
}