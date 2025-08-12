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

    @Test
    public void testCellReset() {
        // Set some states
        cell1.isVisited = true;
        cell1.isPath = true;
        cell1.isCurrent = true;
        cell1.distance = 5;
        cell1.previous = cell2;

        // Reset and verify
        cell1.reset();
        assertFalse(cell1.isVisited);
        assertFalse(cell1.isPath);
        assertFalse(cell1.isCurrent);
        assertEquals(Integer.MAX_VALUE, cell1.distance);
        assertNull(cell1.previous);
    }

    @Test
    public void testCellResetPreservesWallAndStartEnd() {
        // Set wall and start states
        cell1.isWall = true;
        cell1.isStart = true;
        cell1.isEnd = false;

        // Reset
        cell1.reset();

        // These should be preserved
        assertTrue(cell1.isWall);
        assertTrue(cell1.isStart);
        assertFalse(cell1.isEnd);
    }

    @Test
    public void testCellEquals() {
        assertTrue(cell1.equals(cell3)); // Same position
        assertFalse(cell1.equals(cell2)); // Different position
        assertFalse(cell1.equals(null));
        assertFalse(cell1.equals("not a cell"));
        assertTrue(cell1.equals(cell1)); // Same object
    }

    @Test
    public void testCellHashCode() {
        assertEquals(cell1.hashCode(), cell3.hashCode()); // Same position
        assertNotEquals(cell1.hashCode(), cell2.hashCode()); // Different position
    }

    @Test
    public void testCellComparison() {
        cell1.distance = 5;
        cell2.distance = 10;
        cell3.distance = 5;

        assertTrue(cell1.compareTo(cell2) < 0); // cell1 has shorter distance
        assertTrue(cell2.compareTo(cell1) > 0); // cell2 has longer distance
        assertEquals(0, cell1.compareTo(cell3)); // Equal distances
    }
}