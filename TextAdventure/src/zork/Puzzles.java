package zork;

import java.util.Random;
import java.util.Scanner;

public class Puzzles {
    public static boolean playHangman() {
        String[] words = { "apple", "banana", "cherry", "date", "elderberry", "fig", "grape" };
        Random random = new Random();
        String word = words[random.nextInt(words.length)];
        char[] guessedLetters = new char[word.length()];
        int attempts = 6;

        Scanner scanner = new Scanner(System.in);
        System.out.println("Let's play Hangman!");
        System.out.println("Attempts left: " + attempts);

        while (attempts > 0) {
            displayWordProgress(word, guessedLetters);

            System.out.print("Guess a letter: ");
            String input = scanner.nextLine().toLowerCase();

            if (input.length() != 1 || !Character.isLetter(input.charAt(0))) {
                System.out.println("Please enter a single alphabetical character.");
                continue;
            }

            char guess = input.charAt(0);

            if (isAlreadyGuessed(guess, guessedLetters)) {
                System.out.println("You already guessed that letter. Try again.");
                continue;
            }

            boolean isCorrectGuess = containsLetter(guess, word);
            if (isCorrectGuess) {
                updateGuessedLetters(guess, word, guessedLetters);
                System.out.println("Correct guess!");

                if (String.valueOf(guessedLetters).equals(word)) {
                    displayWordProgress(word, guessedLetters);
                    System.out.println("Congratulations! You won!");
                    return true;
                }
            } else {
                attempts--;
                System.out.println("Wrong guess! Attempts left: " + attempts);
            }
        }

        System.out.println("You lost! The word was: " + word);
        return false;
    }

    private static void displayWordProgress(String word, char[] guessedLetters) {
        System.out.print("Progress: ");
        for (char letter : word.toCharArray()) {
            if (isAlreadyGuessed(letter, guessedLetters)) {
                System.out.print(letter + " ");
            } else {
                System.out.print("_ ");
            }
        }
        System.out.println();
    }

    private static boolean isAlreadyGuessed(char guess, char[] guessedLetters) {
        for (char letter : guessedLetters) {
            if (letter == guess) {
                return true;
            }
        }
        return false;
    }

    private static boolean containsLetter(char letter, String word) {
        return word.indexOf(letter) >= 0;
    }

    private static void updateGuessedLetters(char guess, String word, char[] guessedLetters) {
        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) == guess) {
                guessedLetters[i] = guess;
            }
        }
    }

    public static boolean playTicTacToe() {
        char[][] board = new char[3][3];
        char currentPlayer = 'X';

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                board[row][col] = '-';
            }
        }

        boolean isGameOver = false;
        boolean gameWon = false;
        Scanner scanner = new Scanner(System.in);

        System.out.println("Let's play Tic-Tac-Toe!");

        while (!isGameOver) {
            System.out.println("\nCurrent Board:");
            printBoard(board);

            if (currentPlayer == 'X') {
                System.out.println("Player X, enter your move (row and column):");
                int row = scanner.nextInt() - 1;
                int col = scanner.nextInt() - 1;

                if (isValidMove(board, row, col)) {
                    board[row][col] = currentPlayer;

                    if (isWinningMove(board, row, col)) {
                        System.out.println("Player X wins!");
                        isGameOver = true;
                        gameWon = true;
                    } else if (isBoardFull(board)) {
                        System.out.println("It's a draw!");
                        isGameOver = true;
                    } else {
                        currentPlayer = 'O';
                    }
                } else {
                    System.out.println("Invalid move. Try again.");
                }
            } else {
                System.out.println("Player O (Bot) is making a move...");
                makeBotMove(board);
                printBoard(board);

                if (isWinningMove(board)) {
                    System.out.println("Player O (Bot) wins!");
                    isGameOver = true;
                } else if (isBoardFull(board)) {
                    System.out.println("It's a draw!");
                    isGameOver = true;
                } else {
                    currentPlayer = 'X';
                }
            }
        }

        System.out.println("\nFinal Board:");
        printBoard(board);
        return gameWon;
    }

    private static void printBoard(char[][] board) {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                System.out.print(board[row][col] + " ");
            }
            System.out.println();
        }
    }

    private static boolean isValidMove(char[][] board, int row, int col) {
        return row >= 0 && row < 3 && col >= 0 && col < 3 && board[row][col] == '-';
    }

    private static boolean isWinningMove(char[][] board, int row, int col) {
        char symbol = board[row][col];

        // Check row
        if (board[row][0] == symbol && board[row][1] == symbol && board[row][2] == symbol) {
            return true;
        }

        // Check column
        if (board[0][col] == symbol && board[1][col] == symbol && board[2][col] == symbol) {
            return true;
        }

        // Check diagonals
        if ((row == col && board[0][0] == symbol && board[1][1] == symbol && board[2][2] == symbol)
                || (row + col == 2 && board[0][2] == symbol && board[1][1] == symbol && board[2][0] == symbol)) {
            return true;
        }

        return false;
    }

    private static boolean isWinningMove(char[][] board) {
        // Check rows and columns for winning move
        for (int i = 0; i < 3; i++) {
            if ((board[i][0] == board[i][1] && board[i][1] == board[i][2] && board[i][0] != '-')
                    || (board[0][i] == board[1][i] && board[1][i] == board[2][i] && board[0][i] != '-')) {
                return true;
            }
        }

        // Check diagonals for winning move
        if ((board[0][0] == board[1][1] && board[1][1] == board[2][2] && board[0][0] != '-')
                || (board[0][2] == board[1][1] && board[1][1] == board[2][0] && board[0][2] != '-')) {
            return true;
        }

        return false;
    }

    private static boolean isBoardFull(char[][] board) {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (board[row][col] == '-') {
                    return false;
                }
            }
        }
        return true;
    }

    private static void makeBotMove(char[][] board) {
        Random random = new Random();

        while (true) {
            int row = random.nextInt(3);
            int col = random.nextInt(3);

            if (isValidMove(board, row, col)) {
                board[row][col] = 'O';
                break;
            }
        }
    }

    public static boolean playNumberGuessingGame() {
        Random random = new Random();
        int secretNumber = random.nextInt(100) + 1;
        int attempts = 0;

        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to the Number Guessing Game!");
        System.out.println("Guess a number between 1 and 100 in 5 or less attempts.");

        while (true) {
            if (attempts >= 5) {
                System.out.println("Attempts up, game lost!");
                return false;
            }

            System.out.print("Enter your guess: ");
            int guess = scanner.nextInt();
            attempts++;

            if (guess == secretNumber) {
                System.out.println("Congratulations! You guessed the correct number in " + attempts + " attempts.");
                return true;
            } else if (guess < secretNumber) {
                System.out.println("Too low! Try again.");
            } else {
                System.out.println("Too high! Try again.");
            }
        }
    }

    public void playMathGame() {
        // Timed math addition/multiplication/subtraction game implementation
    }

    public void playRockPaperScissors() {
        // Rock, Paper, Scissors game implementation
    }

    public void playBlackjack() {
        // Blackjack game implementation
    }
}