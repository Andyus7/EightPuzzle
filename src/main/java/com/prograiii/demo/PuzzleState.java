package com.prograiii.demo;

import java.util.Arrays;

public class PuzzleState {
    private int[][] board; // El tablero 3x3

    public PuzzleState(int[][] board) {
        // Copia profunda para evitar modificaciones externas
        this.board = new int[3][3];
        for (int i = 0; i < 3; i++) {
            System.arraycopy(board[i], 0, this.board[i], 0, 3);
        }
    }

    public int[][] getBoard() {
        // Devuelve una copia para evitar que el tablero interno sea modificado externamente
        int[][] copy = new int[3][3];
        for (int i = 0; i < 3; i++) {
            System.arraycopy(this.board[i], 0, copy[i], 0, 3);
        }
        return copy;
    }

    // Implementación crucial para algoritmos de búsqueda y colecciones
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PuzzleState that = (PuzzleState) o;
        // Compara los tableros elemento por elemento
        for (int i = 0; i < 3; i++) {
            if (!Arrays.equals(this.board[i], that.board[i])) {
                return false;
            }
        }
        return true;
    }

    // Implementación crucial para algoritmos de búsqueda y colecciones
    @Override
    public int hashCode() {
        // Un hash basado en los valores del tablero.
        // Esto es eficiente y necesario para HashSet y HashMap.
        return Arrays.deepHashCode(board);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            sb.append(Arrays.toString(board[i])).append("\n");
        }
        return sb.toString();
    }
}