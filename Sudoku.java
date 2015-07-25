/*
 * Author : Patricia Terol
 * Project: Sudoku
 */
 
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Sudoku {

	private int[][] table; // Sudoku
	private int[][] initialTable; // Initial information given to solve the
									// sudoku
	private int row; // Row of current position
	private int col; // Column of current position
	private boolean foward; // Direction of movement

	/**
	 * Constructor to read the input from the file and create a 2-D array
	 * containing the initial information of the sudoku.
	 * 
	 * @param filename
	 *            The name of the file the input has to be taken from.
	 * @throws FileNotFoundException
	 *             Exception thrown if the name of the file is not found.
	 */
	public Sudoku(String filename) throws FileNotFoundException {
		// Initialize the arrays, the file (given its name) and the scanner for
		// that file
		table = new int[9][9];
		initialTable = new int[9][9];
		Scanner reader = new Scanner(System.in);
		
		int counter = 0;
		//Fill in sudoku
		int row = 0;
		int col = 0;
		while(counter < 81){
			counter++;
			String number = reader.next();
			if(!number.equals("x")){
				table[row][col] = Integer.parseInt(number);
				initialTable[row][col] = Integer.parseInt(number);
			}
			if(col == 8){
				row++;
			}
			
			if(col == 8){
			    col = 0;
			} else {
				col++;
			}
		}
	}

	/**
	 * Method to initialize the variables and call the solver method.
	 * 
	 * @return The sudoku fully solved.
	 */
	public int[][] sudokuSolver() {
		// Initialize the row, column, and the direction of movement.
		row = 0;
		col = 0;
		foward = true;

		// Solve th sudoku, and return the solution
		solver();
		return table;
	}

	/**
	 * Method to solve the sudoku using a backtrack algorithm.
	 * 
	 * @return True if the sudoku has been solved for each position, false
	 *         otherwise.
	 */
	private boolean solver() {
		// Check if the sudoku has been fully solved
		if (sudokuIsDone()) {
			return true;
		}

		if (initialTable[row][col] == 0) {
			// If the content of the current position wasn't given on the
			// initial information, find a number for it
			for (int i = 1; i <= 9; i++) {
				if (numberCanBePlaced(row, col, i)) {
					table[row][col] = i;
					goFoward();
					if (solver()) {
						return true;
					}
					table[row][col] = 0;
				}
			}
		} else {
			// If the number was given at the initial information, move forward
			// or backwards given the direction the position was moving in
			if (foward) {
				goFoward();
			} else {
				table[row][col] = 0;
				goBackwards();
			}
			if (solver()) {
				return true;
			}
		}
		goBackwards();
		return false;
	}

	/**
	 * Method to go backwards one square the current position.
	 */
	private void goBackwards() {
		foward = false;
		if (col == 0) {
			row--;
			col = 8;
		} else {
			col--;
		}
		return;
	}

	/**
	 * Method to advance the position to the next space.
	 */
	private void goFoward() {
		foward = true;
		if (col == 8) {
			row++;
			col = 0;
		} else {
			col++;
		}
		return;
	}

	/**
	 * Method to check if the number can be placed on a given space, by the
	 * rules of the sudoku.
	 * 
	 * @param row
	 *            Row of the position that the number wants to be placed in.
	 * @param col
	 *            Column of the position that the number wants to be placed in.
	 * @param number
	 *            The number that wants to be placed.
	 * @return True if the number can be placed in the given space, false
	 *         otherwise.
	 */
	private boolean numberCanBePlaced(int row, int col, int number) {
		// Check if the number is not on the given column
		for (int i = 0; i < 9; i++) {
			if (table[i][col] == number) {
				return false;
			}
		}

		// Check if the number is not on the given row
		for (int j = 0; j < 9; j++) {
			if (table[row][j] == number) {
				return false;
			}
		}

		// Check if the number is not on the corresponding 3x3 square
		boolean check = true;
		if (row < 3) {
			if (col < 3) {
				check = checkForSquare(3, 3, number);
			} else if (col > 5) {
				check = checkForSquare(3, 9, number);
			} else {
				check = checkForSquare(3, 6, number);
			}
		} else if (row > 5) {
			if (col < 3) {
				check = checkForSquare(9, 3, number);
			} else if (col > 5) {
				check = checkForSquare(9, 9, number);
			} else {
				check = checkForSquare(9, 6, number);
			}
		} else {
			if (col < 3) {
				check = checkForSquare(6, 3, number);
			} else if (col > 5) {
				check = checkForSquare(6, 9, number);
			} else {
				check = checkForSquare(6, 6, number);
			}
		}

		return check;
	}

	/**
	 * Method to check if the given number is in the corresponding 3x3 square.
	 * 
	 * @param i
	 *            Last row of the corresponding square.
	 * @param j
	 *            Last column of the corresponding square.
	 * @param number
	 *            The number that is being found.
	 * @return True if the number is not in the square, false otherwise.
	 */
	private boolean checkForSquare(int i, int j, int number) {
		for (int k = i - 3; k < i; k++) {
			for (int l = j - 3; l < j; l++) {
				if (table[k][l] == number) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Method to check if all the positions in the sudoku have a number.
	 * 
	 * @return True if the sudoku is done; false otherwise
	 */
	private boolean sudokuIsDone() {
		// Check if all the positions in the 2-D array do not contain 0s
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				if (table[i][j] == 0) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Main method to take the file name and print the solved sudoku.
	 * 
	 * @param args
	 *            Arguments given at runtime.
	 * @throws FileNotFoundException
	 *             Exception thrown if the name of the file given cannot be
	 *             found.
	 */
	public static void main(String[] args) throws FileNotFoundException {
		// Create and solve the sudoku according to the given file
		Sudoku sudoku = new Sudoku("sudoku1.txt");
		int[][] table = sudoku.sudokuSolver();

		// Print the sudoku
		for (int i = 0; i < table.length; i++) {
			for (int j = 0; j < table.length; j++) {
				System.out.print(table[i][j] + " ");
			}
			System.out.println("");
		}

		System.out.println("------------------------");

	}
}
