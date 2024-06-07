
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
public class MagicMaze {
	String file;
	int rows;
	int cols;
	char maze [][];
	HashMap<Character, Integer[]> point1 = new HashMap<Character, Integer[]>();
	HashMap<Character, Integer[]> point2 = new HashMap<Character, Integer[]>();
	//Two hashMaps, one hash map stores the value of the first point and the second one the point of the other one.
	
	//Constructor
	public MagicMaze(String file, int rows, int cols){
		this.file = file;
		this.rows = rows;
		this.cols = cols;
		maze = new char[rows][cols];
		scan(file);
	}

	//Scan the text file and store in the string variable file
	public void scan(String file){
		int row = 0;
		try(BufferedReader read = new BufferedReader(new FileReader(file))){
			String check ;
			while ((check = read.readLine()) != null) {
				maze[row] = check.toCharArray();
				row++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//Store values in hash maps.
		for(int i = 0; i < rows; i++){
			for(int j = 0 ; j < cols; j++) {
				if(Character.isDigit(maze[i][j])) {
					Integer[] store = {i,j};
					if (point2.containsKey(maze[i][j])) {
						point1.put(maze[i][j], store);
					} 
					else {
						point2.put(maze[i][j], store);
					}
				}
			}

		}
	}
	//First main call to solve the maze
	public boolean solveMagicMaze(){
		boolean[][] used = new boolean[rows][cols];
		//Check the results
		return magicMazeR(used, rows-1, 0);
	}
	
	//recursive call to solve the maze
	public boolean magicMazeR(boolean[][] us, int row, int col) {
	    if (positionOk(row, col, maze, us)) {
	        us[row][col] = true;

	        // Solution found
	        if (maze[row][col] == 'X') {
	            return true;
	        }

	        // Check if there is a need to teleport
	        else if (Character.isDigit(maze[row][col])) {
	            int tempRow = row; // Store the original row
	            int tempCol = col; // Store the original col

	            // Check in which pair of points we are and switch to the other one.
	            if (point1.get(maze[row][col])[0] == row && point1.get(maze[row][col])[1] == col) {
	                row = point2.get(maze[row][col])[0];
	                col = point2.get(maze[tempRow][col])[1];
	            } else {
	                row = point1.get(maze[row][col])[0];
	                col = point1.get(maze[tempRow][col])[1];
	            }

	            // Recursive call to see if this is a path to the solution
	            if (magicMazeR(us, row, col)) {
	                return true;
	            }

	            // Restore original row and col values
	            row = tempRow;
	            col = tempCol;
	        }

	        // Backtrack-Recursive call to see if this is a path to the solution
	        if (magicMazeR(us, row + 1, col) ||
	            magicMazeR(us, row - 1, col) ||
	            magicMazeR(us, row, col + 1) ||
	            magicMazeR(us, row, col - 1)) {
	            return true;
	        }

	        //us[row][col] = false; // Backtrack
	    }

	    // No solution has been found
	    return false;
	}
	//Check the position is in bounces, not used, and not a @ (invalid) position
	public boolean positionOk(int row, int col, char[][] valid, boolean[][]used) {
		return row >= 0 && row < rows && col >= 0 && col < cols && valid[row][col] != '@' && !used[row][col];
	}

	}
	
