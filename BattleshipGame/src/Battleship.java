import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Battleship {
	public static final int rows = 10; // 10 X 10 Board
	public static final int cols = 10;
	public static int enemyShips = 5; // Used as a counter for deploying ships
	public static int playerShips = 5; // Used as a counter for deploying ships
	public static int enemyLives = 15; // 5 + 4 + 3 + 2 + 1 ship sizes = 15 = Total Lives
	public static int myLives = 15; 

	// Board to deploy your ships
	public static String[][] myBoard = new String[Battleship.rows + 1][Battleship.cols + 1];
	// Board used to deploy enemy ships
	public static String[][] enemyBoard = new String[Battleship.rows + 1][Battleship.cols + 1];
	// Board to store your guesses and output them on console
	public static String[][] guessBoard = new String[Battleship.rows + 1][Battleship.cols + 1];
	// Board used to store enemy guesses to avoid changing myBoard.
	public static String[][] enemyGuessBoard = new String[Battleship.rows + 1][Battleship.cols + 1];
	// Array list used to keep track of what ships have been deployed.
	public static ArrayList<Integer> remain = new ArrayList<Integer>();

	private static void startGame() {

		intro();

		// Sets up 5 ships for the player.
		while (Battleship.playerShips != 0) {
			setUpPlayerShips();
		}

		Battleship.remain.clear();
		Battleship.playerShips = 5;

		System.out.println("Your board is set! Now setting up your opponent's board randomly!");

		System.out.println("3...");

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		setUpEnemyBoard();

		// Sets up 5 enemy ships
		while (Battleship.enemyShips != 0) {
			setUpEnemyShips();
		}

		System.out.println("2...");

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		Battleship.enemyShips = 5;

		System.out.println("1...");

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println("Your opponent's board is all set up! The game starts now!");
		System.out.println("This is your opponents board with their ships hidden.");
		setUpGuessBoard();
		setUpEnemyGuessBoard();

		// Lives used to track who wins/loses. Alternates between player and computer
		// turn.
		while (Battleship.enemyLives != 0 && Battleship.myLives != 0) {

			try {
				Thread.sleep(1250);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			playerTurn();

			try {
				Thread.sleep(1250);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			opponentTurn();

		}

		result();

	}

	// Fills guessing board with "." and labels.
	private static void setUpEnemyGuessBoard() {
		for (int i = 0; i < rows + 1; i++) {
			for (int j = 0; j < cols + 1; j++) {
				Battleship.enemyGuessBoard[i][j] = Battleship.myBoard[i][j];
			}

		}

	}

	// Checks to see who won the game after either player lives or computer lives
	// reaches zero.
	private static void result() {

		if (Battleship.enemyLives == 0) {
			System.out.println("Congratulations! You sunk all the enemy ships!");
		} else {
			System.out.println("Oh, No! All your ships have sunk! We'll get em next time!");
		}

	}

	// Fills guessing board with "." and labels.
	private static void setUpGuessBoard() {
		for (int i = 0; i < rows + 1; i++) {
			for (int j = 0; j < cols + 1; j++) {
				Battleship.guessBoard[i][j] = ".";
			}

		}

		for (int i = 1; i < 11; i++) {
			char temp = (char) (64 + i);
			Battleship.guessBoard[i][0] = String.valueOf(temp);
			Battleship.guessBoard[0][i] = "" + (i - 1);

		}

	}

	// Tracker used in case the computer generates random numbers that have already
	// been used previously.
	private static void opponentTurn() {
		System.out.println("Opponent's Turn!");
		boolean turnFinished = false;

		do {

			int randRow = Math.abs(new Random().nextInt() % Battleship.rows) + 1; // random number for row.
			int randCol = Math.abs(new Random().nextInt() % Battleship.cols) + 1; // random number for col.

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			// If the randomly generated numbers were previously guessed.
			if (Battleship.enemyGuessBoard[randRow][randCol] == "X"
					|| Battleship.enemyGuessBoard[randRow][randCol] == "O") {

				turnFinished = false;
			}

			// If there is a ship "#" on myBoard, register as a hit "X" on guessBoard.
			else if (Battleship.myBoard[randRow][randCol] == "#") {

				System.out.println("Uh-Oh, One of your ships has been hit!");
				Battleship.enemyGuessBoard[randRow][randCol] = "X";
				turnFinished = true;
				// Reduce lives.
				Battleship.myLives--;
			}

			// Else, miss
			else {
				Battleship.enemyGuessBoard[randRow][randCol] = "O";
				System.out.println("Your opponent missed!");
				turnFinished = true;
			}

		} while (!turnFinished);

		// Print out guess board to console to show user what the enemy hit.
		for (int i = 0; i < 11; i++) {
			for (int j = 0; j < 11; j++) {
				System.out.print(Battleship.enemyGuessBoard[i][j] + "  ");
			}
			System.out.println();
		}

		System.out.println();
	}

	private static void playerTurn() {
		Scanner scnr = new Scanner(System.in);
		String attack = ""; // Temp string to store user input.
		String attackLetter = ""; // Row to be attacked
		int attackNumber = -1; // Col to be attacked.

		do {
			attack = "";
			System.out.println("Please enter a coordinate for attack! (Example: B6 (No Spaces))");
			for (int i = 0; i < 11; i++) {
				for (int j = 0; j < 11; j++) {
					System.out.print(Battleship.guessBoard[i][j] + "  ");
				}
				System.out.println();
			}

			// Get the first word of input.
			attack = scnr.nextLine();
			attack = attack.split(" ", 2)[0];

			// Makes sure its exactly 2 in length (Example: A4)
			if (attack.length() < 2 || attack.length() > 2) {
				System.out.println("Invalid Input! Input either too long or too short!");
				continue;
			}
			try {
				attackLetter = String.valueOf(attack.charAt(0)); // Row letter
				attackNumber = Integer.parseInt(String.valueOf(attack.charAt(1))); // Col Number
			} catch (NumberFormatException e) { // To check if two letters were inputed.
				System.out.println("Invalid Input. Please follow the correct format!");
				continue;
			}

			// Makes sure its an int.
			if (attackLetter.matches(".*\\d.*")) {
				System.out.println("Invalid Input. Please enter a letter and a number!");
				continue;
			}

			// Probably an inefficient way to filter out the useless inputs, but still
			// works.
		} while (!(attackLetter.contains("A") || attackLetter.contains("B") || attackLetter.contains("C")
				|| attackLetter.contains("D") || attackLetter.contains("E") || attackLetter.contains("F")
				|| attackLetter.contains("G") || attackLetter.contains("H") || attackLetter.contains("I")
				|| attackLetter.contains("J"))
				|| !((attackNumber == 0) || (attackNumber == 1) || (attackNumber == 2) || (attackNumber == 3)
						|| (attackNumber == 4) || (attackNumber == 5) || (attackNumber == 6) || (attackNumber == 7)
						|| (attackNumber == 8) || (attackNumber == 9)));
		// DEBUG: System.out.println(attack);

		int attackRow = letterToRow(attackLetter); // Using our helper method assign letter to board index.
		int attackCol = attackNumber + 1; // Since our board start with index 1.

		if (Battleship.enemyBoard[attackRow][attackCol] == "#") {
			System.out.println("BOOM! You Hit!");
			Battleship.guessBoard[attackRow][attackCol] = "X";
			Battleship.enemyBoard[attackRow][attackCol] = "X"; // Could use this to display enemy's final board after
																// the win.
			Battleship.enemyLives--;
			return;

		} else if (Battleship.guessBoard[attackRow][attackCol] == "X" // If you already attacked the spot, you miss your
																		// turn.
				|| (Battleship.guessBoard[attackRow][attackCol] == "O")) {
			System.out.println("Oops! You already fired at this spot!");
			return;
		} else {
			Battleship.guessBoard[attackRow][attackCol] = "O"; // O is miss, X is hit.
			System.out.println("You Missed!");
			return;
		}

	}

	private static void intro() {

		char temp = '@';

		System.out.println(
				"Welcome to Battleship! You will have 5 ships of 5 different sizes ranging from 1 to 5. This is how your board looks: ");

		for (int i = 0; i <= rows; i++) {
			for (int j = 0; j <= cols; j++) {
				// Header for Columns
				if (i == 0 && j > 0)
					Battleship.myBoard[i][j] = String.valueOf(j - 1);
				// Header for Rows
				else if (i > 0 && j == 0)
					Battleship.myBoard[i][j] = String.valueOf(temp);
				else
					Battleship.myBoard[i][j] = ".";

				// Print the value of each cell
				System.out.print(Battleship.myBoard[i][j] + "  ");
			}
			temp++;
			System.out.println();
		}

		System.out.println();
	}

	private static void setUpEnemyShips() {

		int size = Battleship.enemyShips;
		// DEBUG: System.out.println(Battleship.enemyShips);

		// Choosing random row and col:
		while (size != 0) {
			int randRow = Math.abs(new Random().nextInt() % Battleship.rows) + 1;
			int randCol = Math.abs(new Random().nextInt() % Battleship.cols) + 1;
			String orienDecider = "Vertical";
			// 50/50 odds between deciding if the ship will be placed vertically down or
			// horizontally right.
			if (Math.round(Math.random()) == 0) {
				orienDecider = "Horizontal";
			}
			// Makes sure no outOfBounds exception is thrown.
			if (orienDecider.equals("Horizontal")) {
				if ((randCol + size) >= Battleship.enemyBoard[0].length) {
					return;
				}
			}
			// Makes sure no outOfBounds exception is thrown.
			if (orienDecider.equals("Vertical")) {
				if ((randRow + size) >= Battleship.enemyBoard.length) {
					return;
				}
			}
			// Check space below desired location to make sure another ship doesn't already
			// exists there.
			if (orienDecider.equals("Vertical")) {
				for (int i = 0; i < size; i++) {
					if (Battleship.enemyBoard[randRow + i][randCol] == "#") {
						return;
					}
				}
			}
			// Check space to the right of desired location to make sure another ship
			// doesn't already exists there.
			if (orienDecider.equals("Horizontal")) {
				for (int i = 0; i < size; i++) {
					if (Battleship.enemyBoard[randRow][randCol + i] == "#") {
						return;
					}
				}
			}
			// Places ship vertically down.
			if (orienDecider.equals("Vertical")) {
				for (int j = 0; j < size; j++) {
					Battleship.enemyBoard[randRow + j][randCol] = "#";
				}
				size--;
				Battleship.enemyShips--;
			}
			// Places ship horizontally right.
			else {
				for (int j = 0; j < size; j++) {
					Battleship.enemyBoard[randRow][randCol + j] = "#";
				}
				size--;
				Battleship.enemyShips--;
			}

		}

	}

	private static void setUpEnemyBoard() {
		// Setting up enemy board.
		for (int i = 0; i < rows + 1; i++) {
			for (int j = 0; j < cols + 1; j++) {
				Battleship.enemyBoard[i][j] = ".";
			}

		}

		for (int i = 1; i < 11; i++) {
			// Capital Letters
			char temp = (char) (64 + i);
			Battleship.enemyBoard[i][0] = String.valueOf(temp);
			Battleship.enemyBoard[0][i] = "" + (i - 1);

		}

	}

	private static void setUpPlayerShips() {
		Scanner scnr = new Scanner(System.in);
		int col = -1;
		String rowLetter = null;
		int size = -1;
		boolean valid = false;
		String orien = null;

		do {
			System.out.println(
					"Place your ships in the correct format: <Ship# (1-5)> <Row# (A-J)> <Col# (1-9)> <Horizontal/Vertical (H/V)> Example (Capitals Only) : 3 G 5 H");

			if (scnr.hasNextLine()) {
				String line = scnr.nextLine();
				String[] ship = line.split(" ");

				try {
					// Makes sure there are 4 inputs.
					if (ship.length == 4) {
						// Size of ship.
						size = Integer.parseInt(ship[0]);
						// Row of ship.
						rowLetter = ship[1];
						// Column of ship.
						col = Integer.parseInt(ship[2]);
						// Orientation of ship
						orien = ship[3];
						// Makes sure the requirements of the ship are within limits.
						if (size < 1 || size > 5 || col < 0 || col > 9 || (int) rowLetter.charAt(0) < 65
								|| (int) rowLetter.charAt(0) > 75) {
							valid = false;
						} else {
							if (Battleship.remain.contains(size)) {
								System.out.println("You already have a ship of this size. Please choose another size");
								valid = false;
							} else {
								valid = true;
								// Arraylist to assure no two ships of the same size are placed.
								Battleship.remain.add(size);
							}
						}
					} else {
						System.out.println("Please follow the correct format. Example: 3 G 2 V");
						return;
					}
				} catch (Exception e) {
					valid = false;
				}
			}
		} while (valid == false);
		// Converting letter to the desired row using a helper method.
		int row = letterToRow(rowLetter);

		// Check to see if the ship crosses the boundary of the board horizontally.
		if (orien.equals("H") && (col + size) >= Battleship.myBoard[0].length) {
			System.out.println("The ship does not fit at the given location.");
			Battleship.remain.removeAll(Arrays.asList(size));
			return;
		}

		// Check to see if the ship crosses the boundary of the board vertically..
		if (orien.equals("V") && (row + size - 1) >= Battleship.myBoard.length) {
			System.out.println("The ship does not fit at the given location.");
			Battleship.remain.removeAll(Arrays.asList(size));
			return;
		}

		// Check to see below the desired spot to make sure the space is free to place a
		// ship.
		if (orien.equals("V")) {
			for (int i = 0; i < size; i++) {
				if (Battleship.myBoard[row + i][col + 1] == "#") {
					System.out.println("You already placed a ship here!");
					Battleship.remain.removeAll(Arrays.asList(size));
					return;
				}
			}
		}
		// Check to see to the right of the desired spot to make sure the space is free
		// to place a ship.

		if (orien.equals("H")) {
			for (int i = 0; i < size; i++) {
				if (Battleship.myBoard[row][col + 1 + i] == "#") {
					System.out.println("You already placed a ship here!");
					Battleship.remain.removeAll(Arrays.asList(size));
					return;
				}
			}
		}
		//If spot is free
		if ((Battleship.myBoard[row][col + 1] == ".")) {
			//Place Horizontally right or Vertically down.
			if (orien.equals("H")) {
				for (int i = 0; i < size; i++) {
					Battleship.myBoard[row][col + 1 + i] = "#";
				}
			}
			
			if (orien.equals("V")) {
				for (int i = 0; i < size; i++) {
					Battleship.myBoard[row + i][col + 1] = "#";
				}
			}
			
			//Print out board to show the player where they placed the ships.
			for (int i = 0; i < 11; i++) {
				for (int j = 0; j < 11; j++) {
					System.out.print(Battleship.myBoard[i][j] + "  ");
				}
				System.out.println();
			}
			Battleship.playerShips--;
			System.out.println("Ship has been placed. Ships remaining: " + Battleship.playerShips);
		}
	}
	//Helper method for Letter -> Number.
	private static int letterToRow(String letter) {
		if (letter.length() > 0)
			return (int) letter.charAt(0) - 64;
		else
			return -1;
	}

	public static void main(String[] args) {
		/*
		 * DEBUG: setUpEnemyBoard(); while (Battleship.enemyShips != 0) {
		 * setUpEnemyShips(); }
		 *
		 * for (int i = 0; i < 11; i++) { for (int j = 0; j < 11; j++) {
		 * System.out.print(enemyBoard[i][j] + " "); } System.out.println(); }
		 */

		/*
		 * setUpGuessBoard(); playerTurn(); playerTurn();
		 */

		startGame();
	}
}
