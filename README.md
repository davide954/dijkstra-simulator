# Dijkstra Pathfinding Visualizer

A Java Swing application that demonstrates Dijkstra's shortest path algorithm with real-time visualization. The application provides an interactive grid where users can create obstacles, adjust weights, and watch the algorithm find the optimal path step by step.

## Features

- **Interactive Grid Interface**: Click and drag to create walls and modify the environment
- **Real-time Algorithm Visualization**: Watch Dijkstra's algorithm explore the grid in real-time
- **Weighted Pathfinding**: Support for cells with different traversal costs (weights 1 and 5)
- **Dynamic Start/End Points**: Drag and drop start and end positions anywhere on the grid
- **Step-by-step Animation**: Configurable delay to observe algorithm progression
- **Color-coded Visualization**: Clear visual distinction between different cell states

## Requirements

- Java 17 or higher
- Maven 3.6.0 or higher

## Installation

### Clone the Repository

```bash
git clone https://github.com/davide954/dijkstra-simulator.git
cd dijkstra-simulator
```

### Build with Maven

```bash
mvn clean compile
```

### Run the Application

```bash
mvn exec:java -Dexec.mainClass="com.simulator.dijkstra.DijkstraPathfinding"
```

### Create Executable JAR

```bash
mvn clean package
java -jar target/dijkstra-pathfinding-1.4.0.jar
```

## Usage

### Basic Controls

- **Shift + Left-click**: Set start point (green circle)
- **Ctrl + Left-click**: Set end point (red square)  
- **Left-click and drag**: Create or remove walls (black cells)
- **Right-click**: Toggle cell weight between 1 and 5
- **Drag green/red points**: Reposition start and end points

### Interface Elements

- **Start Dijkstra**: Execute the pathfinding algorithm
- **Reset Grid**: Clear all walls, weights, and return to default state
- **Clear Path**: Remove pathfinding results while preserving walls

### Visual Legend

| Color | Meaning |
|-------|---------|
| White | Empty cell (weight 1) |
| Gray numbers | Cell weight (when > 1) |
| Black | Wall (impassable) |
| Green circle | Start point |
| Red square | End point |
| Light Blue | Visited cells |
| Orange | Currently processing |
| Yellow | Optimal path |

## Algorithm Details

The implementation uses Dijkstra's algorithm with the following characteristics:

- **Time Complexity**: O((V + E) log V) where V is vertices and E is edges
- **Space Complexity**: O(V) for storing distances and visited states
- **Optimality**: Guarantees shortest path when all edge weights are non-negative
- **Grid Movement**: 4-directional movement (up, down, left, right)

## Project Structure

```
src/
├── main/java/com/simulator/dijkstra/
│   ├── DijkstraPathfinding.java    # Main application class
│   ├── DijkstraSolver.java         # Core algorithm implementation
│   ├── GridPanel.java              # UI grid component
│   └── Cell.java                   # Grid cell data structure
└── test/java/com/simulator/dijkstra/
    └── DijkstraPathfindingTest.java # Unit tests
```

## Testing

Run the test suite:

```bash
mvn test
```

Generate test reports:

```bash
mvn surefire-report:report
```

## Development

### Code Style

The project follows standard Java conventions:

- Use meaningful variable and method names
- Maintain consistent indentation (4 spaces)
- Include JavaDoc for public methods
- Follow Maven standard directory layout

### Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

### Building for Development

```bash
# Compile and run tests
mvn clean test

# Run with debugging
mvn exec:java -Dexec.mainClass="com.simulator.dijkstra.DijkstraPathfinding" -Dexec.args="-Xdebug"

# Generate documentation
mvn javadoc:javadoc
```

## Technical Details

### Dependencies

- **JUnit 4.13.2**: Unit testing framework
- **Java Swing**: GUI framework (built-in)
- **Maven Shade Plugin**: Creates executable JAR with dependencies

### Configuration

The application uses these default settings:

- Grid size: 20 rows × 30 columns
- Cell size: 30×30 pixels
- Animation delay: 5 milliseconds (configurable in code)
- Default cell weight: 1
- Alternative cell weight: 5

These can be modified in the source code constants section.

## Known Limitations

- Algorithm visualization may be too fast on high-performance systems
- Grid size is fixed at compile time
- Only supports 4-directional movement (no diagonal)
- Single algorithm implementation (Dijkstra only)
- Cell weights are limited to values 1 and 5

## License

This project is licensed under the MIT License. See the `LICENSE` file for details.

## Acknowledgments

- Algorithm visualization inspired by pathfinding educational tools
- Built using Java Swing for cross-platform compatibility
- Follows Maven standard project structure

## Version History

- **v1.4.0**: Current version with improved algorithm efficiency and visualization
- **v1.3**: Added weighted cells and improved visualization
- **v1.2**: Enhanced UI with drag-and-drop functionality  
- **v1.1**: Basic pathfinding with wall creation
- **v1.0**: Initial release with core algorithm

## Support

For questions, issues, or contributions, please use the GitHub issue tracker.