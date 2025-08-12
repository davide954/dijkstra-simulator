package com.simulator.dijkstra;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * Dijkstra Pathfinding Visualizer
 * <p>
 * A Java Swing application that demonstrates the Dijkstra shortest path
 * algorithm
 * with real-time visualization. Users can create walls, move start/end points,
 * and watch the algorithm find the optimal path step by step.
 * <p>
 * Features:
 * - Interactive grid with drag-and-drop functionality
 * - Real-time algorithm visualization
 * - Color-coded cells for different states
 * - Step-by-step pathfinding animation
 * <p>
 * Controls:
 * - Left-click to create/remove walls
 * - Right-click to change cell weight (1 or 5)
 * - Drag green (start) or red (end) points to reposition
 * - Use control buttons to run algorithm, reset grid, or clear path
 *
 * @author bugubster13
 * @version 1.0
 * @since 2025
 */
public class DijkstraPathfinding extends JFrame {
    private static final int ROWS = 20;
    private static final int COLS = 30;
    private static final int CELL_SIZE = 30;
    private static final int DELAY = 5; // Milliseconds delay to visualize steps

    private Cell[][] grid;
    private Cell startCell;
    private Cell endCell;
    private GridPanel gridPanel;

    private boolean isRunning = false;
    private JButton startButton;
    private JLabel statusLabel;

    /**
     * Check if the algorithm is currently running.
     * @return true if running, false otherwise.
     */
    public boolean isRunning() {
        return isRunning;
    }