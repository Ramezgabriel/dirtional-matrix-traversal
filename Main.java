import java.util.*;
import java.util.regex.*;

public class MyClass {

    public static void main(String args[]) {
        String[][] matrix = {
                {"HI", "1", "2", "3", "v4"},
                {">9", "10", "11", "v12", "13"},
                {"^14", "15", "16", "17", "<18"}
        };
        String output = DirectionalMatrixTraversal.traverseMatrix(matrix);
        System.out.println(output);
    }
}

enum Direction {
    UP("^"),
    DOWN("v"),
    RIGHT(">"),
    LEFT("<");
    
    private final String direction;
    private static final Pattern directionsPattern = Pattern.compile("^[<>^v].*");
    private Direction(String direction) {
        this.direction = direction;
    }
    
    public static String cleanDirections(String value) {
        if (value == null) {
            return "";
        }
        return value.replaceAll("[<>^v]", "");
    }
    
    public static boolean isDirection(String value) {
        return directionsPattern.matcher(value).matches();
    }
    
    public static Direction extractDirection(Direction currentDirection, String possibleDirection) {
        return Arrays.stream(Direction.values())
            .filter(v -> possibleDirection !=null? possibleDirection.substring(0,1).equalsIgnoreCase(v.toString()) : false)
            .findAny()
            .orElse(currentDirection);
    }
    
    public String toString() {
        return this.direction;
    }
    
}

 class DirectionalMatrixTraversal {
    private static final String LOOP = "LOOP";
	private static final String COMMA = ",";
    
	public static String traverseMatrix(String[][] matrix) {
		Set<String> visited = new HashSet<>(); // set to keep track of visited cells
        StringBuilder output = new StringBuilder(); // string builder to build the output string
        int numRows = matrix.length;
        int numCols = matrix[0].length;
        int row = 0, col = 0; // starting position
        Direction direction = Direction.RIGHT; // initial direction
        boolean loopDetected = false; // flag to detect loops
        while (row >= 0 && row < numRows && col >= 0 && col < numCols) { // while within matrix boundaries
            String currentValue = matrix[row][col];
            String cell = Direction.cleanDirections(currentValue); // remove control characters
			String visiting = row + COMMA + col;
            if (visited.contains(visiting)) { // check for loops
                output.append(DirectionalMatrixTraversal.LOOP);
                loopDetected = true;
                break;
            }
            output.append(cell).append(COMMA).append(" "); // add cell to output string
            visited.add(visiting); // mark cell as visited
            direction = Direction.isDirection(currentValue)?Direction.extractDirection(direction, currentValue):direction;
            
            col = DirectionalMatrixTraversal.move(direction, Direction.RIGHT, Direction.LEFT, col);
            row = DirectionalMatrixTraversal.move(direction, Direction.DOWN, Direction.UP, row);            
            
        }
        if (!loopDetected) { // remove trailing comma and space
            output.delete(output.length() - 2, output.length());
        }
        return output.toString();
    }
    
    private static int move(Direction direction, Direction fowardDirection, Direction backwardDirection, int currentValue) {
        int returned = currentValue;
        if (backwardDirection == direction) {
            returned--;
        } else if (fowardDirection == direction) {
            returned++;
        }
        return returned;
    }

}