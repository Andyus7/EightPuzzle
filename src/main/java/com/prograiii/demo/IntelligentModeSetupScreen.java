package com.prograiii.demo;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class IntelligentModeSetupScreen {

    private Stage primaryStage;
    private LoginRegisterScreen mainController;
    private String username;

    private TextField[][] startFields;
    private TextField[][] goalFields;
    private final int GRID_SIZE = 3;

    public IntelligentModeSetupScreen(Stage primaryStage, LoginRegisterScreen mainController, String username) {
        this.primaryStage = primaryStage;
        this.mainController = mainController;
        this.username = username;
        this.startFields = new TextField[GRID_SIZE][GRID_SIZE];
        this.goalFields = new TextField[GRID_SIZE][GRID_SIZE];
    }

    public Scene createSetupScene() {
        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #e6ffe6;"); // Fondo verde claro

        Label title = new Label("Configurar Modo Inteligente");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #333;");

        // --- Configuración del Estado Inicial ---
        VBox startSetup = new VBox(10);
        startSetup.setAlignment(Pos.CENTER);
        Label startLabel = new Label("Estado Inicial (0 para espacio vacío)");
        startLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        GridPane startGrid = createPuzzleGrid(startFields);
        startSetup.getChildren().addAll(startLabel, startGrid);

        // --- Configuración del Estado Objetivo ---
        VBox goalSetup = new VBox(10);
        goalSetup.setAlignment(Pos.CENTER);
        Label goalLabel = new Label("Estado Objetivo (0 para espacio vacío)");
        goalLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        GridPane goalGrid = createPuzzleGrid(goalFields);
        goalSetup.getChildren().addAll(goalLabel, goalGrid);

        HBox gridsContainer = new HBox(40);
        gridsContainer.setAlignment(Pos.CENTER);
        gridsContainer.getChildren().addAll(startSetup, goalSetup);

        Button startIntelligentGameButton = new Button("Iniciar Juego Inteligente");
        startIntelligentGameButton.setPrefSize(250, 50);
        startIntelligentGameButton.setStyle("-fx-font-size: 20px; -fx-background-color: #007bff; -fx-text-fill: white; -fx-background-radius: 5;");
        startIntelligentGameButton.setOnAction(e -> validateAndStartGame());

        Button backButton = new Button("Volver a Selección de Modo");
        backButton.setPrefSize(200, 40);
        backButton.setStyle("-fx-font-size: 18px; -fx-background-color: #f44336; -fx-text-fill: white; -fx-background-radius: 5;");
        backButton.setOnAction(e -> {
            GameModeSelectionScreen selectionScreen = new GameModeSelectionScreen(primaryStage, mainController, username);
            primaryStage.setScene(selectionScreen.createSelectionScene());
        });

        root.getChildren().addAll(title, gridsContainer, startIntelligentGameButton, backButton);

        return new Scene(root, 700, 600);
    }

    private GridPane createPuzzleGrid(TextField[][] fields) {
        GridPane grid = new GridPane();
        grid.setHgap(5);
        grid.setVgap(5);
        grid.setAlignment(Pos.CENTER);

        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                TextField field = new TextField();
                field.setPrefSize(60, 60);
                field.setAlignment(Pos.CENTER);
                field.setStyle("-fx-font-size: 24px; -fx-background-color: #fff; -fx-border-color: #ccc; -fx-border-width: 1px;");
                // Permite solo números de 0 a 8
                field.textProperty().addListener((observable, oldValue, newValue) -> {
                    if (!newValue.matches("\\d*")) {
                        field.setText(oldValue);
                    }
                    if (newValue.length() > 1) { // Limita a un solo dígito
                        field.setText(newValue.substring(0, 1));
                    }
                });
                fields[i][j] = field;
                grid.add(field, j, i);
            }
        }
        return grid;
    }

    private void validateAndStartGame() {
        int[][] startBoard = parseBoard(startFields);
        int[][] goalBoard = parseBoard(goalFields);

        if (startBoard == null || goalBoard == null) {
            showAlert("Error de Entrada", "Asegúrate de llenar todas las celdas con números válidos (0-8) y que no haya duplicados ni números fuera de rango en cada tablero.");
            return;
        }

        PuzzleState initialState = new PuzzleState(startBoard);
        PuzzleState goalState = new PuzzleState(goalBoard);

        // Aquí podrías añadir una validación para ver si el estado inicial y objetivo son resolubles
        // (es decir, tienen la misma paridad de inversiones)
        // Esto es más complejo y no se implementa aquí, pero es una mejora importante.

        mainController.goToGameScreen(username, true, initialState, goalState); // true para modo inteligente
    }

    private int[][] parseBoard(TextField[][] fields) {
        int[][] board = new int[GRID_SIZE][GRID_SIZE];
        boolean[] seenNumbers = new boolean[GRID_SIZE * GRID_SIZE]; // Para verificar duplicados y rango
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                String text = fields[i][j].getText();
                if (text.isEmpty()) {
                    return null; // Campo vacío
                }
                try {
                    int val = Integer.parseInt(text);
                    if (val < 0 || val >= GRID_SIZE * GRID_SIZE || seenNumbers[val]) {
                        return null; // Número fuera de rango o duplicado
                    }
                    board[i][j] = val;
                    seenNumbers[val] = true;
                } catch (NumberFormatException e) {
                    return null; // No es un número válido
                }
            }
        }
        return board;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}