package zork;

import java.util.*;

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
        return (row == col && board[0][0] == symbol && board[1][1] == symbol && board[2][2] == symbol)
                || (row + col == 2 && board[0][2] == symbol && board[1][1] == symbol && board[2][0] == symbol);
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
        return (board[0][0] == board[1][1] && board[1][1] == board[2][2] && board[0][0] != '-')
                || (board[0][2] == board[1][1] && board[1][1] == board[2][0] && board[0][2] != '-');
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

    private static boolean gameFinished = false;

    public static boolean playMathGame(int numQuestions, int timeLimitSeconds) {
        int numCorrectAnswers = 0;

        Random random = new Random();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to the Math Game!");
        System.out.println("You have to answer " + numQuestions + " math questions within " + timeLimitSeconds + " seconds.");
        System.out.println("Press enter to start the game.");

        scanner.nextLine();

        Thread timerThread = new Thread(() -> {
            try {
                Thread.sleep(timeLimitSeconds * 1000L);
            } catch (InterruptedException e) {
                // Thread interrupted
            }
            gameFinished = true;
        });

        timerThread.start();

        while (!gameFinished && numCorrectAnswers < numQuestions) {
            int num1 = random.nextInt(10) + 1;
            int num2 = random.nextInt(10) + 1;
            int result = num1 + num2;

            System.out.print(num1 + " + " + num2 + " = ");
            int answer = scanner.nextInt();

            if (answer == result) {
                System.out.println("Correct!");
                numCorrectAnswers++;
            } else {
                System.out.println("Incorrect!");
            }
        }

        timerThread.interrupt();

        if (numCorrectAnswers == numQuestions) {
            System.out.println("Congratulations! You answered all the questions within the time limit.");
            return true;
        } else {
            System.out.println("Time's up! You didn't answer all the questions within the time limit.");
            return false;
        }
    }

    public static boolean playRockPaperScissors() {
        String[] choices = { "rock", "paper", "scissors" };
        Random random = new Random();
        Scanner scanner = new Scanner(System.in);

        int playerWins = 0;
        int botWins = 0;

        System.out.println("Welcome to Rock Paper Scissors! Best of five rounds.");

        for (int round = 1; round <= 5; round++) {
            System.out.println("\nRound " + round);
            System.out.println("Choose: 0 for rock, 1 for paper, 2 for scissors");

            int playerChoice = scanner.nextInt();
            scanner.nextLine();

            if (playerChoice < 0 || playerChoice >= choices.length) {
                System.out.println("Invalid choice! Please choose a number between 0 and " + (choices.length - 1) + ".");
                round--;
                continue;
            }

            String playerMove = choices[playerChoice];
            System.out.println("You chose: " + playerMove);

            int botChoice = random.nextInt(choices.length);
            String botMove = choices[botChoice];
            System.out.println("Bot chose: " + botMove);

            if (playerMove.equals(botMove)) {
                System.out.println("It's a tie!");
                round--;
                continue;
            }

            if ((playerMove.equals("rock") && botMove.equals("scissors"))
                    || (playerMove.equals("paper") && botMove.equals("rock"))
                    || (playerMove.equals("scissors") && botMove.equals("paper"))) {
                System.out.println("You win this round!");
                playerWins++;
            } else {
                System.out.println("You lose this round.");
                botWins++;
            }

            System.out.println("Player wins: " + playerWins);
            System.out.println("Bot wins: " + botWins);

            if (playerWins == 3) {
                System.out.println("You win!");
                break;
            }
            else if (botWins == 3) {
                System.out.println("You lose.");
                break;
            }
        }

        return playerWins > botWins;
    }

    public static boolean playBlackjack() {
        List<String> deck = new ArrayList<>();
        List<String> playerHand = new ArrayList<>();
        List<String> dealerHand = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();

        for (int i = 2; i <= 10; i++) {
            deck.add(Integer.toString(i));
        }
        deck.add("J");
        deck.add("Q");
        deck.add("K");
        deck.add("A");

        for (int i = 0; i < 2; i++) {
            playerHand.add(deck.remove(random.nextInt(deck.size())));
            dealerHand.add(deck.remove(random.nextInt(deck.size())));
        }

        while (true) {
            System.out.println("Player's hand: " + playerHand);
            System.out.println("Dealer's hand: [?, " + dealerHand.get(1) + "]");
            System.out.println("Choose an action: (h)it or (s)tand");

            String action = scanner.nextLine().toUpperCase();

            if (action.equals("H") || action.equals("HIT")) {
                String card = deck.remove(random.nextInt(deck.size()));
                playerHand.add(card);
                if (getHandValue(playerHand) > 21) {
                    System.out.println("Player's hand: " + playerHand);
                    System.out.println("Player busts! You lose.");
                    return false;
                }
            } else if (action.equals("S") || action.equals("STAND")) {
                break;
            }
            else {
                System.out.println("Invalid option, please choose (h)it or (s)tand.");
            }
        }

        while (getHandValue(dealerHand) < 17) {
            dealerHand.add(deck.remove(random.nextInt(deck.size())));
        }

        System.out.println("Player's hand: " + playerHand);
        System.out.println("Dealer's hand: " + dealerHand);

        int playerValue = getHandValue(playerHand);
        int dealerValue = getHandValue(dealerHand);

        if (dealerValue > 21) {
            System.out.println("Dealer busts! You win.");
            return true;
        } else if (playerValue > dealerValue) {
            System.out.println("You win!");
            return true;
        } else if (playerValue < dealerValue) {
            System.out.println("You lose.");
            return false;
        } else {
            System.out.println("It's a tie. Push.");
            return false;
        }
    }

    private static int getHandValue(List<String> hand) {
        int value = 0;
        int numAces = 0;

        for (String card : hand) {
            if (card.equals("A")) {
                value += 11;
                numAces++;
            } else if (card.equals("K") || card.equals("Q") || card.equals("J")) {
                value += 10;
            } else {
                value += Integer.parseInt(card);
            }
        }

        while (value > 21 && numAces > 0) {
            value -= 10;
            numAces--;
        }

        return value;
    }

    public static boolean playConnectFour() {
        char[][] board = new char[6][7];
        for (char[] row : board) {
            Arrays.fill(row, ' ');
        }

        char playerSymbol = 'X';
        char botSymbol = 'O';

        Scanner scanner = new Scanner(System.in);

        while (true) {
            printBoardC4(board);
            int playerColumn = getPlayerMove(scanner, board);
            dropPiece(board, playerColumn, playerSymbol);

            if (isWinningMove(board, playerSymbol)) {
                printBoardC4(board);
                System.out.println("Congratulations! You win.");
                return true;
            }

            int botColumn = getBotMove(board);
            dropPiece(board, botColumn, botSymbol);

            if (isWinningMove(board, botSymbol)) {
                printBoardC4(board);
                System.out.println("Sorry! You lose.");
                return false;
            }
        }
    }

    private static void printBoardC4(char[][] board) {
        for (int row = board.length - 1; row >= 0; row--) {
            for (int col = 0; col < board[row].length; col++) {
                System.out.print("| " + board[row][col] + " ");
            }
            System.out.println("|");
        }
        System.out.println("-----------------------------");
        System.out.println("  1   2   3   4   5   6   7  ");
        System.out.println();
    }

    private static int getPlayerMove(Scanner scanner, char[][] board) {
        int column;
        while (true) {
            System.out.print("Enter column (1-7): ");
            column = scanner.nextInt();
            scanner.nextLine();

            if (column < 1 || column > 7 || board[5][column - 1] != ' ') {
                System.out.println("Invalid move. Try again.");
            } else {
                break;
            }
        }
        return column - 1;
    }

    private static void dropPiece(char[][] board, int column, char symbol) {
        for (int row = 0; row < board.length; row++) {
            if (board[row][column] == ' ') {
                board[row][column] = symbol;
                break;
            }
        }
    }

    private static boolean isWinningMove(char[][] board, char symbol) {
        // Check rows
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 4; col++) {
                if (board[row][col] == symbol &&
                        board[row][col + 1] == symbol &&
                        board[row][col + 2] == symbol &&
                        board[row][col + 3] == symbol) {
                    return true;
                }
            }
        }

        // Check columns
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 7; col++) {
                if (board[row][col] == symbol &&
                        board[row + 1][col] == symbol &&
                        board[row + 2][col] == symbol &&
                        board[row + 3][col] == symbol) {
                    return true;
                }
            }
        }

        // Check diagonals (top-left to bottom-right)
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 4; col++) {
                if (board[row][col] == symbol &&
                        board[row + 1][col + 1] == symbol &&
                        board[row + 2][col + 2] == symbol &&
                        board[row + 3][col + 3] == symbol) {
                    return true;
                }
            }
        }

        // Check diagonals (bottom-left to top-right)
        for (int row = 3; row < 6; row++) {
            for (int col = 0; col < 4; col++) {
                if (board[row][col] == symbol &&
                        board[row - 1][col + 1] == symbol &&
                        board[row - 2][col + 2] == symbol &&
                        board[row - 3][col + 3] == symbol) {
                    return true;
                }
            }
        }

        return false;
    }

    private static int getBotMove(char[][] board) {
        int column;
        Random random = new Random();

        do {
            column = random.nextInt(7);
        } while (!isValidMove(board, column));

        return column;
    }

    private static boolean isValidMove(char[][] board, int column) {
        return column >= 0 && column < board[0].length && board[5][column] == ' ';
    }
}