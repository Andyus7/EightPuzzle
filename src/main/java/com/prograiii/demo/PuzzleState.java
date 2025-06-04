package com.prograiii.demo;

import java.util.Arrays;
import java.util.Random;

public class PuzzleState {
    private int[][] board;
    private int emptyRow;
    private int emptyCol;
    private final int SIZE = 3;

    public PuzzleState(int[][] initialBoard, int emptyRow, int emptyCol) {
        this.board = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            System.arraycopy(initialBoard[i], 0, this.board[i], 0, SIZE);
        }
        this.emptyRow = emptyRow;
        this.emptyCol = emptyCol;
    }

    public PuzzleState() {
        this.board = new int[SIZE][SIZE];
        int[] numbers = new int[SIZE * SIZE];
        for (int i = 0; i < SIZE * SIZE - 1; i++) {
            numbers[i] = i + 1;
        }
        numbers[SIZE * SIZE - 1] = 0; // 0 representa el espacio vacío

        // Barajar los números aleatoriamente
        Random random = new Random();
        for (int i = numbers.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            int temp = numbers[i];
            numbers[i] = numbers[j];
            numbers[j] = temp;
        }

        // Llenar el tablero
        int index = 0;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                this.board[i][j] = numbers[index++];
                if (this.board[i][j] == 0) {
                    this.emptyRow = i;
                    this.emptyCol = j;
                }
            }
        }
    }

    public int[][] getBoard() {
        int[][] copy = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            System.arraycopy(this.board[i], 0, copy[i], 0, SIZE);
        }
        return copy;
    }

    public int getEmptyRow() {
        return emptyRow;
    }

    public int getEmptyCol() {
        return emptyCol;
    }

    public void setEmptyRow(int emptyRow) {
        this.emptyRow = emptyRow;
    }

    public void setEmptyCol(int emptyCol) {
        this.emptyCol = emptyCol;
    }

    public int getSize() {
        return SIZE;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PuzzleState that = (PuzzleState) o;
        return emptyRow == that.emptyRow && emptyCol == that.emptyCol && Arrays.deepEquals(board, that.board);
    }

    @Override
    public int hashCode() {
        int result = Arrays.deepHashCode(board);
        result = 31 * result + emptyRow;
        result = 31 * result + emptyCol;
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                sb.append(String.format("%-3d", board[i][j]));
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}