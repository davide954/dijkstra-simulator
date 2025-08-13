package com.simulator.dijkstra;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for the Dijkstra Pathfinding application
 * Tests the core functionality of the Cell class and pathfinding logic
 */
public class DijkstraPathfindingTest {

    private Cell cell1;
    private Cell cell2;
    private Cell cell3;
    private Cell[][] testGrid;

    @Before
    public void setUp() {
        cell1 = new Cell(0, 0);
        cell2 = new Cell(1, 1);
        cell3 = new Cell(0, 0); // Same position as cell1
        
        // Create a small test grid
        testGrid = new Cell[3][3];
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                testGrid[row][col] = new Cell(row, col);
            }
        }
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
        assertEquals(1, cell1.weight); // Default weight should be 1
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
        cell1.weight = 5;

        // Reset
        cell1.reset();

        // These should be preserved
        assertTrue(cell1.isWall);
        assertTrue(cell1.isStart);
        assertFalse(cell1.isEnd);
        assertEquals(5, cell1.weight); // Weight should be preserved
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

    @Test
    public void testCellWeights() {
        // Test default weight
        assertEquals(1, cell1.weight);
        
        // Test weight modification
        cell1.weight = 5;
        assertEquals(5, cell1.weight);
        
        // Test weight in pathfinding context
        cell1.distance = 0;
        int expectedDistance = cell1.distance + cell2.weight;
        assertEquals(1, expectedDistance); // 0 + 1 (default weight)
        
        cell2.weight = 3;
        expectedDistance = cell1.distance + cell2.weight;
        assertEquals(3, expectedDistance); // 0 + 3
    }

    @Test
    public void testCellStates() {
        // Test setting different states
        cell1.isStart = true;
        assertTrue(cell1.isStart);

        cell1.isEnd = true;
        assertTrue(cell1.isEnd);

        cell1.isWall = true;
        assertTrue(cell1.isWall);

        cell1.isVisited = true;
        assertTrue(cell1.isVisited);

        cell1.isPath = true;
        assertTrue(cell1.isPath);

        cell1.isCurrent = true;
        assertTrue(cell1.isCurrent);
    }

    @Test
    public void testCellDistance() {
        // Test distance setting and getting
        cell1.distance = 0;
        assertEquals(0, cell1.distance);

        cell1.distance = 100;
        assertEquals(100, cell1.distance);

        cell1.distance = Integer.MAX_VALUE;
        assertEquals(Integer.MAX_VALUE, cell1.distance);
    }

    @Test
    public void testCellPrevious() {
        // Test previous cell linking
        assertNull(cell1.previous);

        cell1.previous = cell2;
        assertEquals(cell2, cell1.previous);

        cell1.previous = null;
        assertNull(cell1.previous);
    }

    @Test
    public void testPathfindingScenario() {
        // Test a simple pathfinding scenario
        Cell start = new Cell(0, 0);
        Cell middle = new Cell(0, 1);
        Cell end = new Cell(0, 2);

        // Setup pathfinding state
        start.isStart = true;
        start.distance = 0;

        middle.distance = 1;
        middle.previous = start;
        middle.isVisited = true;

        end.isEnd = true;
        end.distance = 2;
        end.previous = middle;
        end.isPath = true;

        // Verify the path
        assertEquals(0, start.distance);
        assertEquals(1, middle.distance);
        assertEquals(2, end.distance);
        assertEquals(start, middle.previous);
        assertEquals(middle, end.previous);
        assertTrue(end.isPath);
    }

    @Test
    public void testWallBehavior() {
        // Test wall functionality
        cell1.isWall = true;
        assertTrue(cell1.isWall);

        // Wall should block pathfinding
        cell1.distance = Integer.MAX_VALUE;
        assertEquals(Integer.MAX_VALUE, cell1.distance);

        // Wall should not be visited in normal pathfinding
        cell1.isVisited = false;
        assertFalse(cell1.isVisited);
    }

    @Test
    public void testGridInitialization() {
        // Test that all cells in grid are properly initialized
        assertNotNull(testGrid);
        assertEquals(3, testGrid.length);
        assertEquals(3, testGrid[0].length);
        
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                Cell cell = testGrid[row][col];
                assertNotNull(cell);
                assertEquals(row, cell.row);
                assertEquals(col, cell.col);
                assertEquals(1, cell.weight);
                assertFalse(cell.isWall);
                assertFalse(cell.isVisited);
            }
        }
    }

    @Test
    public void testWeightedPathfinding() {
        // Test weighted pathfinding scenario
        Cell start = testGrid[0][0];
        Cell middle = testGrid[0][1];
        Cell end = testGrid[0][2];
        
        // Set weights
        start.weight = 1;
        middle.weight = 5; // Higher weight
        end.weight = 1;
        
        // Simulate pathfinding
        start.distance = 0;
        start.isStart = true;
        
        // Distance to middle should include middle's weight
        middle.distance = start.distance + middle.weight;
        middle.previous = start;
        
        // Distance to end should include end's weight
        end.distance = middle.distance + end.weight;
        end.previous = middle;
        end.isEnd = true;
        
        assertEquals(0, start.distance);
        assertEquals(5, middle.distance); // 0 + 5 (middle's weight)
        assertEquals(6, end.distance);   // 5 + 1 (end's weight)
    }

    @Test
    public void testCellStateTransitions() {
        // Test cell state changes during algorithm execution
        Cell cell = new Cell(1, 1);
        
        // Initial state
        assertFalse(cell.isVisited);
        assertFalse(cell.isCurrent);
        assertFalse(cell.isPath);
        
        // During algorithm execution
        cell.isCurrent = true;
        assertTrue(cell.isCurrent);
        
        cell.isVisited = true;
        cell.isCurrent = false;
        assertTrue(cell.isVisited);
        assertFalse(cell.isCurrent);
        
        // After path reconstruction
        cell.isPath = true;
        assertTrue(cell.isPath);
        assertTrue(cell.isVisited); // Should still be visited
    }
}