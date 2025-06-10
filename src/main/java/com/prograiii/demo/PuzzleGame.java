package com.prograiii.demo;

import java.util.ArrayList;
import java.util.List;

public class PuzzleGame {
    private PuzzleState currentState;
    private final int[][] goalState = {
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 0}
    };
    private final int SIZE = 3;
    private String username;
    private boolean intelligentMode;

    // Constructor por defecto (modo normal sin usuario)
    public PuzzleGame() {
        this(null, false);
    }

    // Constructor personalizado (para recibir usuario y modo de juego)
    public PuzzleGame(String username, boolean intelligentMode) {
        this.username = username;
        this.intelligentMode = intelligentMode;

        if (intelligentMode) {
            // Inicializa con alguna lógica específica si lo necesitas
            this.currentState = new PuzzleState(); // Por ahora igual que el normal
        } else {
            startNewGame();
        }
    }

    public void startNewGame() {
        this.currentState = new PuzzleState();
    }

    public PuzzleState getCurrentState() {
        return currentState;
    }

    public boolean isGoalReached() {
        int[][] currentBoard = currentState.getBoard();
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (currentBoard[i][j] != goalState[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean moveTile(int row, int col) {
        int emptyRow = currentState.getEmptyRow();
        int emptyCol = currentState.getEmptyCol();

        if ((Math.abs(row - emptyRow) == 1 && col == emptyCol) ||
                (row == emptyRow && Math.abs(col - emptyCol) == 1)) {

            int[][] board = currentState.getBoard();
            int temp = board[row][col];
            board[row][col] = 0;
            board[emptyRow][emptyCol] = temp;

            currentState = new PuzzleState(board, row, col);
            return true;
        }
        return false;
    }

    public int[][] getGoalState() {
        int[][] copy = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            System.arraycopy(goalState[i], 0, copy[i], 0, SIZE);
        }
        return copy;
    }

    public int calculateManhattanDistance(PuzzleState state) {
        int distance = 0;
        int[][] board = state.getBoard();
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                int value = board[i][j];
                if (value != 0) {
                    int targetRow = (value - 1) / SIZE;
                    int targetCol = (value - 1) % SIZE;
                    distance += Math.abs(i - targetRow) + Math.abs(j - targetCol);
                }
            }
        }
        return distance;
    }

    public List<PuzzleState> getPossibleNextStates(PuzzleState state) {
        List<PuzzleState> nextStates = new ArrayList<>();
        int emptyRow = state.getEmptyRow();
        int emptyCol = state.getEmptyCol();
        int[][] board = state.getBoard();

        int[][] possibleMoves = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        for (int[] move : possibleMoves) {
            int newEmptyRow = emptyRow + move[0];
            int newEmptyCol = emptyCol + move[1];

            if (newEmptyRow >= 0 && newEmptyRow < SIZE && newEmptyCol >= 0 && newEmptyCol < SIZE) {
                int[][] newBoard = new int[SIZE][SIZE];
                for (int i = 0; i < SIZE; i++) {
                    System.arraycopy(board[i], 0, newBoard[i], 0, SIZE);
                }

                int movedTile = newBoard[newEmptyRow][newEmptyCol];
                newBoard[newEmptyRow][newEmptyCol] = 0;
                newBoard[emptyRow][emptyCol] = movedTile;

                nextStates.add(new PuzzleState(newBoard, newEmptyRow, newEmptyCol));
            }
        }

        return nextStates;
    }

    // Getters adicionales por si los necesitas en otras clases
    public String getUsername() {
        return username;
    }

    public boolean isIntelligentMode() {
        return intelligentMode;
    }
}
