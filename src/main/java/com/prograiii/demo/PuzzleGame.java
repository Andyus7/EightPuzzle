package com.prograiii.demo;

import java.util.*;

public class PuzzleGame {
    private PuzzleState currentState;
    private PuzzleState goalState; // Nuevo campo para el estado objetivo
    private boolean intelligentMode; // Nuevo campo para el modo

    // Constructor para Modo Normal (estado inicial aleatorio, meta por defecto)
    public PuzzleGame(String username, boolean intelligentMode) {
        this.intelligentMode = intelligentMode;
        // Generar un estado inicial resoluble aleatorio
        this.currentState = generateRandomSolvableState();
        // Estado objetivo por defecto (1, 2, 3, 4, 5, 6, 7, 8, 0)
        this.goalState = new PuzzleState(new int[][]{
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 0}
        });
    }

    // Constructor para Modo Inteligente (estados inicial y objetivo definidos por el usuario)
    public PuzzleGame(String username, boolean intelligentMode, PuzzleState initialState, PuzzleState goalState) {
        this.intelligentMode = intelligentMode;
        this.currentState = initialState;
        this.goalState = goalState;
    }

    public PuzzleState getCurrentState() {
        return currentState;
    }

    public void setCurrentState(PuzzleState newState) {
        this.currentState = newState;
    }

    public boolean moveTile(int row, int col) {
        int emptyRow = -1, emptyCol = -1;
        int[][] board = currentState.getBoard();

        // Encontrar el espacio vacío (0)
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == 0) {
                    emptyRow = i;
                    emptyCol = j;
                    break;
                }
            }
            if (emptyRow != -1) break;
        }

        // Verificar si la pieza clicada está adyacente al espacio vacío
        if (Math.abs(row - emptyRow) + Math.abs(col - emptyCol) == 1) {
            // Realizar el movimiento
            int temp = board[row][col];
            board[row][col] = board[emptyRow][emptyCol];
            board[emptyRow][emptyCol] = temp;
            currentState = new PuzzleState(board); // Crear un nuevo estado con el tablero modificado
            return true;
        }
        return false;
    }

    public boolean isGoalReached() {
        return currentState.equals(goalState);
    }

    // Método para generar un estado inicial resoluble aleatorio
    private PuzzleState generateRandomSolvableState() {
        int[] flatBoard = new int[9];
        for (int i = 0; i < 9; i++) {
            flatBoard[i] = i;
        }
        Random rand = new Random();

        // Shuffle hasta que sea resoluble
        while (true) {
            for (int i = 0; i < 9; i++) {
                int randomIndex = rand.nextInt(9);
                int temp = flatBoard[i];
                flatBoard[i] = flatBoard[randomIndex];
                flatBoard[randomIndex] = temp;
            }

            int[][] board2D = new int[3][3];
            for (int i = 0; i < 9; i++) {
                board2D[i / 3][i % 3] = flatBoard[i];
            }
            PuzzleState potentialState = new PuzzleState(board2D);

            // Verificar si es resoluble
            // (La paridad de inversiones debe ser la misma entre el estado inicial y el objetivo)
            // Para un 8-puzzle (3x3), un estado es resoluble si el número de inversiones es par.
            if (getInversions(flatBoard) % 2 == 0) {
                return potentialState;
            }
        }
    }

    // Calcula el número de inversiones en un arreglo plano (para la verificación de resolubilidad)
    private int getInversions(int[] board) {
        int inversions = 0;
        for (int i = 0; i < board.length - 1; i++) {
            if (board[i] == 0) continue; // El 0 no cuenta para inversiones
            for (int j = i + 1; j < board.length; j++) {
                if (board[j] == 0) continue;
                if (board[i] > board[j]) {
                    inversions++;
                }
            }
        }
        return inversions;
    }

    // Calcula la distancia de Manhattan para un estado dado
    public int calculateManhattanDistance(PuzzleState state) {
        int distance = 0;
        int[][] currentBoard = state.getBoard();
        int[][] goalBoard = goalState.getBoard(); // Usa el goalState de la instancia de PuzzleGame

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int value = currentBoard[i][j];
                if (value == 0) continue; // El espacio vacío no contribuye a la distancia

                // Encontrar la posición de 'value' en el estado objetivo
                for (int goalI = 0; goalI < 3; goalI++) {
                    for (int goalJ = 0; goalJ < 3; goalJ++) {
                        if (goalBoard[goalI][goalJ] == value) {
                            distance += Math.abs(i - goalI) + Math.abs(j - goalJ);
                            break;
                        }
                    }
                }
            }
        }
        return distance;
    }

    // Obtiene todos los estados posibles a partir del estado actual
    public List<PuzzleState> getPossibleNextStates(PuzzleState state) {
        List<PuzzleState> nextStates = new ArrayList<>();
        int[][] currentBoard = state.getBoard();
        int emptyRow = -1, emptyCol = -1;

        // Encontrar el espacio vacío
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (currentBoard[i][j] == 0) {
                    emptyRow = i;
                    emptyCol = j;
                    break;
                }
            }
            if (emptyRow != -1) break;
        }

        int[] dr = {-1, 1, 0, 0}; // Cambios de fila: arriba, abajo, izquierda, derecha
        int[] dc = {0, 0, -1, 1}; // Cambios de columna

        for (int i = 0; i < 4; i++) {
            int newRow = emptyRow + dr[i];
            int newCol = emptyCol + dc[i];

            if (newRow >= 0 && newRow < 3 && newCol >= 0 && newCol < 3) {
                int[][] newBoard = new int[3][3];
                for (int r = 0; r < 3; r++) {
                    System.arraycopy(currentBoard[r], 0, newBoard[r], 0, 3);
                }

                // Intercambiar la pieza adyacente con el espacio vacío
                int temp = newBoard[newRow][newCol];
                newBoard[newRow][newCol] = newBoard[emptyRow][emptyCol];
                newBoard[emptyRow][emptyCol] = temp;

                nextStates.add(new PuzzleState(newBoard));
            }
        }
        return nextStates;
    }

    // Algoritmo de búsqueda BFS para encontrar la solución
    public List<PuzzleState> findSolutionBFS() {
        if (currentState.equals(goalState)) {
            return Arrays.asList(currentState); // Ya está resuelto
        }

        Queue<Node> queue = new LinkedList<>();
        Set<PuzzleState> visited = new HashSet<>(); // Para evitar ciclos
        Map<PuzzleState, PuzzleState> parentMap = new HashMap<>(); // Para reconstruir el camino

        Node initialNode = new Node(currentState, null);
        queue.add(initialNode);
        visited.add(currentState);

        while (!queue.isEmpty()) {
            Node currentNode = queue.poll();
            PuzzleState currentPuzzleState = currentNode.state;

            if (currentPuzzleState.equals(goalState)) {
                return reconstructPath(parentMap, currentPuzzleState);
            }

            for (PuzzleState nextState : getPossibleNextStates(currentPuzzleState)) {
                if (!visited.contains(nextState)) {
                    visited.add(nextState);
                    parentMap.put(nextState, currentPuzzleState);
                    queue.add(new Node(nextState, currentNode));
                }
            }
        }
        return null; // No se encontró solución
    }

    // Clase auxiliar para el algoritmo BFS (almacena el estado y su padre)
    private static class Node {
        PuzzleState state;
        Node parent; // Para reconstruir el camino

        public Node(PuzzleState state, Node parent) {
            this.state = state;
            this.parent = parent;
        }
    }

    // Reconstruye el camino desde el estado objetivo hasta el estado inicial
    private List<PuzzleState> reconstructPath(Map<PuzzleState, PuzzleState> parentMap, PuzzleState goal) {
        List<PuzzleState> path = new ArrayList<>();
        PuzzleState current = goal;
        while (current != null) {
            path.add(current);
            current = parentMap.get(current);
        }
        Collections.reverse(path); // El camino está invertido, así que lo revertimos
        return path;
    }
}