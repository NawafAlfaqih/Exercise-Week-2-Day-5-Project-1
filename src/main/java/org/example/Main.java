package org.example;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Random;

/*
    I Was Planning to add "Ultimate TicTacToe" mode, but I prioritized cleaning the code and method calls
    The Planned "Ultimate TicTacToe" mode rules:
    - 9 sub-boards, each 3x3 inside a main board
    - After a move, the chosen cell determines which sub-board the opponent must play next
    - We check win on each sub-board and then check win on a 3x3 main board

    But unfortunately couldn't make it in time, and I deleted the code related to the mode :(

 */

public class Main {
    public static void main(String[] args) {

        startGame();

    }

    public static void startGame(){
        System.out.println("----------------- TicTacToe Game -----------------");
        System.out.println("1. Start Game.");
        System.out.println("2. Close The Game.");
        int choice = readIntInRange(1, 2, "Enter a number to choose from the menu -> ");

        switch (choice) {
            case 1:
                normalMode();
                break;
            case 2:
                System.out.println("Closing the game......");
                System.exit(0);
                break;
        }
    }

    public static void normalMode() {

        System.out.println("----------------- TicTacToe -----------------");
        System.out.println("Opponent: ");
        System.out.println("1. Player1 vs Player2.");
        System.out.println("2. Player1 vs Computer.");
        System.out.println("3. Return Back to Main Menu.");
        int choice = readIntInRange(1, 3, "Enter a number to choose from the menu -> ");

        switch (choice) {
            case 1:
                pvpMode();
                break;
            case 2:
                cpuMode();
                break;
            case 3:
                startGame();
                break;
        }

    }

    public static void pvpMode() {
        String player1Name = readString("Enter X Player's name: ");
        String player2Name = readString("Enter O Player's name: ");
        playGame(player1Name, player2Name, true, true);

    }

    public static void cpuMode() {
        String player1Name = readString("Enter The Player name: ");
        System.out.println("Play as: ");
        System.out.println("1. X.");
        System.out.println("2. O.");
        int choice = readIntInRange(1, 2, "-> ");

        switch (choice) {
            case 1:
                playGame(player1Name, "CPU", true, false);
                break;
            case 2:
                playGame( "CPU", player1Name, false, true);
                break;
        }
    }

    public static void playGame(String xPlayerName,String oPlayerName ,boolean xPlayerFlag, boolean oPlayerFlag) {
        int numberOfRounds = readIntInRange(1, 99999, "How Many rounds: ");

        char[][] gameBoard = new char[3][3];
        clearBoard(gameBoard);
        int[] gameScore = {0, 0, 0};
        for(int i = 1; i <= numberOfRounds; i++) {
            playRound(gameBoard, gameScore, xPlayerName, oPlayerName, xPlayerFlag, oPlayerFlag);
            clearBoard(gameBoard);

            if(gameScore[0] > numberOfRounds/2 || gameScore[1] > numberOfRounds/2)
                break; //exit if player has won more than half.
        }
        finalScore(xPlayerName, oPlayerName, gameScore);

        startGame();
    }

    private static void finalScore(String xPlayerName, String oPlayerName, int[] gameScore) {
        String winner = "";
        System.out.println("------------------- Final Score -------------------");
        System.out.println(xPlayerName +" won "+ gameScore[0]+" times.");
        System.out.println(oPlayerName +" won "+ gameScore[1]+" times.");
        System.out.println("Draw "+ gameScore[2]+" times.");

        if (gameScore[0] > gameScore[1])
            winner = xPlayerName;
        else if (gameScore[1] > gameScore[0])
            winner = oPlayerName;
        else
            winner = "Draw";
        System.out.println("------------------- The Final Winner: " + winner);
        System.out.println("---------------------------------------------------------");
    }

    public static void playRound(char[][] gameBoard, int[] gameScore,
                                 String xPlayerName, String oPlayerName, boolean xPlayerFlag, boolean oPlayerFlag) {

        while (true)
        {
            playTurn(gameBoard, xPlayerFlag, 'X', xPlayerName);
            if(winCheck(gameBoard ,'X')) {
                boardPrinter(gameBoard);
                System.out.println("Player "+ xPlayerName+" Win!!");
                gameScore[0]++;
                break;
            } else if (drawCheck(gameBoard)) {
                boardPrinter(gameBoard);
                System.out.println("The Game is Draw!!");
                gameScore[2]++;
                break;
            }


            playTurn(gameBoard, oPlayerFlag, 'O', oPlayerName);
            if(winCheck(gameBoard, 'O')) {
                boardPrinter(gameBoard);
                System.out.println("Player "+ oPlayerName+" Win!!");
                gameScore[1]++;
                break;
            } else if (drawCheck(gameBoard)) {
                boardPrinter(gameBoard);
                System.out.println("The Game is Draw!!");
                gameScore[2]++;
                break;
            }
        }
    }

    public static void playTurn(char[][] gameBoard, boolean playerFlag, char playerSymbol, String playerName) {

        int boardLocation = 0;
        boolean isCorrectPlay = false;

        if (playerFlag) {
            boardPrinter(gameBoard);
            do {
                boardLocation = readIntInRange(1, 9, playerSymbol+" Player ("+playerName+") -> choose location on the board (1-9): ");
                isCorrectPlay = checkPlay(gameBoard, boardLocation, playerFlag);
            }while(!isCorrectPlay);

            playOnBoard(gameBoard, boardLocation, playerSymbol);
        } else {
            boardLocation = checkRandomPlay(1, 9, gameBoard);
            System.out.println("Computer Has Played '"+playerSymbol+"' in "+boardLocation);
            playOnBoard(gameBoard, boardLocation, playerSymbol);
        }

    }

    public static boolean checkPlay(char[][] gameBoard, int boardLocation ,boolean playerFlag) {
        if (boardLocation > 6) {
            if (gameBoard[2][(boardLocation - 6) - 1] == '-')
                return true;
        } else if (boardLocation > 3) {
            if (gameBoard[1][(boardLocation - 3) - 1] == '-')
                return true;
        } else {
            if (gameBoard[0][boardLocation - 1] == '-')
                return true;
        }

        if(playerFlag)
            System.out.println("Choice '"+boardLocation+"' is not available, try again");

        return false;
    }

    public static void playOnBoard(char[][] gameBoard, int boardLocation, char playerSymbol) {
        if (boardLocation > 6) {
                gameBoard[2][(boardLocation - 6) - 1] = playerSymbol;
        } else if (boardLocation > 3) {
                gameBoard[1][(boardLocation - 3) - 1] = playerSymbol;
        } else {
                gameBoard[0][boardLocation - 1] = playerSymbol;
        }
    }

    public static void clearBoard(char[][] gameBoard) {

        for(int i = 0; i < gameBoard.length; i++)
            for (int j = 0; j < gameBoard.length; j++)
                gameBoard[i][j] = '-';
    }

    public static boolean winCheck(char[][] gameBoard, char playerSymbol) {

        for(int i = 0; i < gameBoard.length; i++) {
            if (gameBoard[i][0] == playerSymbol && gameBoard[i][1] == playerSymbol && gameBoard[i][2] == playerSymbol) {
                return true;
            } else if (gameBoard[0][i] == playerSymbol && gameBoard[1][i] == playerSymbol && gameBoard[2][i] == playerSymbol) {
                return true;
            }
        }
             if (gameBoard[0][0] == playerSymbol && gameBoard[1][1] == playerSymbol && gameBoard[2][2] == playerSymbol) {
                return true;
            } else if (gameBoard[0][2] == playerSymbol && gameBoard[1][1] == playerSymbol && gameBoard[2][0] == playerSymbol) {
                return true;
            }

        return false;
    }

    public static boolean drawCheck(char[][] gameBoard) {
        for (int i  = 0; i < gameBoard.length; i++)
            for (int j = 0; j < gameBoard.length; j++)
                if(gameBoard[i][j] == '-')
                    return false;
        return true;
    }

    public static void boardPrinter(char[][] gameBoard) {
        int elementCount = 0;
        System.out.print("\n---------------------------------");
        for(int i = 0; i < gameBoard.length; i++) {
            rowPrinter();
            for(int j = 0; j < gameBoard[i].length; j++){

                elementCount++;
                if(gameBoard[i][j] == '-')
                    System.out.print("|    "+elementCount+"    |");
                else
                    System.out.print("|   ~"+ gameBoard[i][j]+"~   |");
            }
            rowPrinter();
            System.out.print("---------------------------------");
        }
        System.out.println();
    }

    public static void rowPrinter() {
        System.out.print("\n|         |");
        System.out.print("|         |");
        System.out.print("|         |\n");
    }

    public static int checkRandomPlay(int from, int to, char[][] gameBoard) throws IllegalArgumentException {

        try {
            if(from > to)
                throw new IllegalArgumentException("Invalid Range for random!!!");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            startGame(); //restart game
        }

        int location;
        Random random = new Random();
        boolean correct = false;

        do {
            location = random.nextInt(1, to + 1);
            correct = checkPlay(gameBoard, location, false);
        } while (!correct);

        return location;

    }

    public static int readIntInRange(int from, int to, String message) throws IllegalArgumentException{
        Scanner input = new Scanner(System.in);

        try {
            if(from > to)
                throw new IllegalArgumentException("Invalid Range argument for reading int!!!");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            startGame(); //restart game
        }

        boolean valid = false;
        int number = 0;
        do {
            try {
                System.out.print(message);
                number = input.nextInt();
                if(number < from || number > to)
                    throw new ArithmeticException("Number is not in range ("+from+"-"+to+"), try again.");
                valid = true;
            }
            catch (InputMismatchException e) {
                System.out.println("Not a number, try again.");
                input.next();
            }
            catch (ArithmeticException e){
                System.out.println(e.getMessage());
            }
        } while (!valid);

        return number;
    }

    public static String readString(String message) {

        Scanner input = new Scanner(System.in);

        boolean valid = false;
        String string = "";
        do {
            try {
                System.out.print(message);
                string = input.nextLine();
                if (string.isBlank())
                    throw new IOException("The string is empty");
                valid = true;
            } catch (IOException e) {
                System.out.println(e.getMessage());
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } while (!valid);

        return string;
    }
}