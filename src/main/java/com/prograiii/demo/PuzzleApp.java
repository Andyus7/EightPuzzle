package com.prograiii.demo;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.*;

public class PuzzleApp {

    private PuzzleGame game;
    private Button[][] buttons;
    private final int GRID_SIZE = 3;
    private Label timeLabel;
    private Label movesLabel;
    private int movesCount = 0;
    private Timeline timer;
    private int seconds = 0;
    private Button helpButton;
    private Button highlightedButton = null;

    private String username;
    private boolean intelligentMode;
    private Stage primaryStage;
    private LoginRegisterScreen mainController;

    private List<PuzzleState> solutionPath;
    private int currentSolutionStep = 0;
    private Timeline solutionTimeline;

    // AÑADIR ESTE CAMPO DE INSTANCIA para el botón Resolver
    private Button solveButton;

    // Constructor para Modo Normal (random)
    public PuzzleApp(String username, boolean intelligentMode, Stage primaryStage, LoginRegisterScreen mainController) {
        this.username = username;
        this.intelligentMode = intelligentMode;
        this.primaryStage = primaryStage;
        this.mainController = mainController;
        this.game = new PuzzleGame(username, intelligentMode);
    }

    // Constructor para Modo Inteligente (con estados definidos)
    public PuzzleApp(String username, boolean intelligentMode, Stage primaryStage, LoginRegisterScreen mainController, PuzzleState initialState, PuzzleState goalState) {
        this.username = username;
        this.intelligentMode = intelligentMode;
        this.primaryStage = primaryStage;
        this.mainController = mainController;
        this.game = new PuzzleGame(username, intelligentMode, initialState, goalState);
    }

    public Scene createGameScene() {
        buttons = new Button[GRID_SIZE][GRID_SIZE];

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #F8F8F8;");

        // PANEL SUPERIOR (infoPanel)
        HBox infoPanel = new HBox(15);
        infoPanel.setAlignment(Pos.TOP_RIGHT);
        infoPanel.setPadding(new Insets(10));

        try {
            Image lightbulbImage = new Image(getClass().getResourceAsStream("/icons/lightbulb.png"));
            ImageView lightbulbIcon = new ImageView(lightbulbImage);
            lightbulbIcon.setFitHeight(20);
            lightbulbIcon.setFitWidth(20);
            helpButton = new Button();
            helpButton.setGraphic(lightbulbIcon);
            helpButton.setOnAction(event -> suggestMove());
            helpButton.setDisable(intelligentMode);
            infoPanel.getChildren().add(helpButton);

            Image clockImage = new Image(getClass().getResourceAsStream("/icons/clock.png"));
            ImageView clockIcon = new ImageView(clockImage);
            clockIcon.setFitHeight(20);
            clockIcon.setFitWidth(20);
            timeLabel = new Label("Tiempo: 00:00");
            infoPanel.getChildren().addAll(clockIcon, timeLabel);

            Image handImage = new Image(getClass().getResourceAsStream("/icons/hand.png"));
            ImageView handIcon = new ImageView(handImage);
            handIcon.setFitHeight(20);
            handIcon.setFitWidth(20);
            movesLabel = new Label("Movimientos: 0");
            infoPanel.getChildren().addAll(handIcon, movesLabel);

        } catch (Exception e) {
            System.err.println("Error cargando íconos: " + e.getMessage());
            helpButton = new Button("Ayuda");
            helpButton.setOnAction(event -> suggestMove());
            helpButton.setDisable(intelligentMode);
            timeLabel = new Label("Tiempo: 00:00");
            movesLabel = new Label("Movimientos: 0");
            infoPanel.getChildren().addAll(helpButton, timeLabel, movesLabel);
        }

        startTimer();

        root.setTop(infoPanel);

        // PANEL DEL TABLERO
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20));
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setAlignment(Pos.CENTER);

        int[][] board = game.getCurrentState().getBoard();
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                Button button = new Button(board[i][j] == 0 ? "" : String.valueOf(board[i][j]));
                button.setMinSize(80, 80);
                button.setStyle("-fx-font-size: 24px; -fx-background-color: #f0f0f0; -fx-border-color: #ccc; -fx-border-width: 1px;");
                final int row = i;
                final int col = j;
                button.setOnAction(event -> handleTileClick(row, col));
                buttons[i][j] = button;
                gridPane.add(button, j, i);
            }
        }
        root.setCenter(gridPane);

        // Panel inferior con botones de control
        HBox bottomPanel = new HBox(20);
        bottomPanel.setAlignment(Pos.CENTER);
        bottomPanel.setPadding(new Insets(10));

        Button replayButton = new Button("Volver a Jugar");
        replayButton.setPrefWidth(180);
        replayButton.setPrefHeight(40);
        replayButton.setStyle("-fx-font-size: 16px; -fx-background-color: #FFA000; -fx-text-fill: white; -fx-background-radius: 5;");
        replayButton.setOnAction(e -> {
            timer.stop();
            if (solutionTimeline != null) solutionTimeline.stop();
            if (intelligentMode) {
                IntelligentModeSetupScreen setupScreen = new IntelligentModeSetupScreen(primaryStage, mainController, username);
                primaryStage.setScene(setupScreen.createSetupScene());
            } else {
                PuzzleApp newGame = new PuzzleApp(username, false, primaryStage, mainController);
                primaryStage.setScene(newGame.createGameScene());
            }
        });

        Button backToMenuButton = new Button("Menú Principal");
        backToMenuButton.setPrefWidth(180);
        backToMenuButton.setPrefHeight(40);
        backToMenuButton.setStyle("-fx-font-size: 16px; -fx-background-color: #f44336; -fx-text-fill: white; -fx-background-radius: 5;");
        backToMenuButton.setOnAction(e -> {
            timer.stop();
            if (solutionTimeline != null) solutionTimeline.stop();
            primaryStage.setScene(mainController.getMainMenuScene(username));
        });

        if (intelligentMode) {
            // ASIGNAR LA INSTANCIA AL CAMPO DE CLASE
            solveButton = new Button("Resolver");
            solveButton.setPrefWidth(180);
            solveButton.setPrefHeight(40);
            solveButton.setStyle("-fx-font-size: 16px; -fx-background-color: #008CBA; -fx-text-fill: white; -fx-background-radius: 5;");
            solveButton.setOnAction(e -> solvePuzzleIntelligentMode());
            bottomPanel.getChildren().addAll(solveButton, replayButton, backToMenuButton);
        } else {
            bottomPanel.getChildren().addAll(replayButton, backToMenuButton);
        }

        root.setBottom(bottomPanel);

        Scene scene = new Scene(root, 600, 700);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

        return scene;
    }

    private void startTimer() {
        seconds = 0;
        movesCount = 0;
        timeLabel.setText("Tiempo: 00:00");
        movesLabel.setText("Movimientos: 0");

        if (timer != null) {
            timer.stop();
        }
        timer = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            seconds++;
            int minutes = seconds / 60;
            int secs = seconds % 60;
            timeLabel.setText(String.format("Tiempo: %02d:%02d", minutes, secs));
        }));
        timer.setCycleCount(Animation.INDEFINITE);
        timer.play();
    }

    private void handleTileClick(int row, int col) {
        if (!intelligentMode) {
            if (game.moveTile(row, col)) {
                movesCount++;
                movesLabel.setText("Movimientos: " + movesCount);
                updateBoard();
                if (game.isGoalReached()) {
                    gameCompleted();
                }
            }
        }
    }

    private void updateBoard() {
        int[][] board = game.getCurrentState().getBoard();
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                buttons[i][j].setText(board[i][j] == 0 ? "" : String.valueOf(board[i][j]));
                buttons[i][j].setStyle("-fx-font-size: 24px; -fx-background-color: #f0f0f0; -fx-border-color: #ccc; -fx-border-width: 1px;");
            }
        }
        highlightedButton = null;
    }

    private void gameCompleted() {
        timer.stop();
        if (solutionTimeline != null) solutionTimeline.stop();

        double score = calculateScore(seconds, movesCount);
        String message = String.format("¡Puzzle resuelto por %s en %02d:%02d con %d movimientos!\nTu puntuación: %.2f",
                username, (seconds / 60), (seconds % 60), movesCount, score);

        ScoreManager scoreManager = new ScoreManager();
        scoreManager.updateUserScore(username, seconds, movesCount, score);

        showAlert("¡Felicidades!", message, Alert.AlertType.INFORMATION);
    }

    private void suggestMove() {
        if (!intelligentMode) {
            PuzzleState currentState = game.getCurrentState();
            List<PuzzleState> nextPossibleStates = game.getPossibleNextStates(currentState);

            PuzzleState bestNextState = null;
            int minManhattanDistance = game.calculateManhattanDistance(currentState);

            for (PuzzleState nextState : nextPossibleStates) {
                int distance = game.calculateManhattanDistance(nextState);
                if (distance < minManhattanDistance) {
                    minManhattanDistance = distance;
                    bestNextState = nextState;
                }
            }

            if (bestNextState != null && minManhattanDistance < game.calculateManhattanDistance(currentState)) {
                int movedRow = -1, movedCol = -1;
                int[][] boardNow = currentState.getBoard();
                int[][] boardNext = bestNextState.getBoard();

                for (int r = 0; r < GRID_SIZE; r++) {
                    for (int c = 0; c < GRID_SIZE; c++) {
                        if (boardNow[r][c] != boardNext[r][c] && boardNow[r][c] != 0) {
                            movedRow = r;
                            movedCol = c;
                            break;
                        }
                    }
                    if (movedRow != -1) break;
                }

                if (movedRow != -1 && movedCol != -1) {
                    if (highlightedButton != null) {
                        highlightedButton.setStyle("-fx-font-size: 24px; -fx-background-color: #f0f0f0; -fx-border-color: #ccc; -fx-border-width: 1px;");
                    }
                    highlightedButton = buttons[movedRow][movedCol];
                    highlightedButton.setStyle("-fx-font-size: 24px; -fx-background-color: #90EE90; -fx-border-color: #006400; -fx-border-width: 2px;");
                }
            } else {
                showAlert("Sugerencia", "No hay un movimiento que mejore la distancia de Manhattan en este momento.", Alert.AlertType.INFORMATION);
            }
        }
    }


    private void solvePuzzleIntelligentMode() {
        timer.stop();
        // Deshabilitar todos los botones del tablero
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                buttons[i][j].setDisable(true);
            }
        }
        // Deshabilitar el botón de "Resolver" usando la referencia directa
        if (solveButton != null) { // Agrega esta verificación por seguridad
            solveButton.setDisable(true);
        }


        solutionPath = game.findSolutionBFS();

        if (solutionPath == null) {
            showAlert("No se encontró solución", "No se pudo encontrar una solución para el estado inicial y objetivo dados. Asegúrese de que el puzzle sea resoluble.", Alert.AlertType.ERROR);
            // Re-habilitar botones si no hay solución para que el usuario pueda volver al menú
            for (int i = 0; i < GRID_SIZE; i++) {
                for (int j = 0; j < GRID_SIZE; j++) {
                    buttons[i][j].setDisable(false);
                }
            }
            if (solveButton != null) { // Re-habilitar el botón de resolver
                solveButton.setDisable(false);
            }
            return;
        }

        currentSolutionStep = 0;
        seconds = 0;
        movesCount = 0;

        solutionTimeline = new Timeline(new KeyFrame(Duration.seconds(0.5), e -> {
            if (currentSolutionStep < solutionPath.size()) {
                PuzzleState nextState = solutionPath.get(currentSolutionStep);
                game.setCurrentState(nextState);
                updateBoard();

                if (currentSolutionStep > 0) {
                    movesCount++;
                    movesLabel.setText("Movimientos: " + movesCount);
                }
                seconds++;
                int minutes = seconds / 60;
                int secs = seconds % 60;
                timeLabel.setText(String.format("Tiempo: %02d:%02d", minutes, secs));

                currentSolutionStep++;
            } else {
                solutionTimeline.stop();
                gameCompleted();
            }
        }));
        solutionTimeline.setCycleCount(solutionPath.size());
        solutionTimeline.play();
    }


    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private double calculateScore(int timeInSeconds, int moves) {
        double baseScore = 10000.0;
        double timePenalty = timeInSeconds * 1.5;
        double movesPenalty = moves * 5.0;
        double score = baseScore - timePenalty - movesPenalty;
        return Math.max(0, score);
    }
}