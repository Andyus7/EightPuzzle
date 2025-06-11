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
import java.util.List;

public class PuzzleApp { // Ya no extiende Application directamente, es una clase de escena

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
    private Stage primaryStage; // Referencia al Stage principal
    private LoginRegisterScreen mainController; // Referencia al controlador principal

    // Constructor actualizado para recibir Stage y mainController
    public PuzzleApp(String username, boolean intelligentMode, Stage primaryStage, LoginRegisterScreen mainController) {
        this.username = username;
        this.intelligentMode = intelligentMode;
        this.primaryStage = primaryStage;
        this.mainController = mainController;
        this.game = new PuzzleGame(username, intelligentMode);
    }

    // Método para crear la escena del juego
    public Scene createGameScene() {
        buttons = new Button[GRID_SIZE][GRID_SIZE];

        BorderPane root = new BorderPane();

        // PANEL SUPERIOR (infoPanel)
        HBox infoPanel = new HBox(15);
        infoPanel.setAlignment(Pos.TOP_RIGHT);
        infoPanel.setPadding(new Insets(10));

        try { // Manejo de errores para las imágenes
            Image lightbulbImage = new Image(getClass().getResourceAsStream("/icons/lightbulb.png"));
            ImageView lightbulbIcon = new ImageView(lightbulbImage);
            lightbulbIcon.setFitHeight(20);
            lightbulbIcon.setFitWidth(20);
            helpButton = new Button();
            helpButton.setGraphic(lightbulbIcon);
            helpButton.setOnAction(event -> suggestMove());
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
            // Fallback en caso de que los íconos no se encuentren
            helpButton = new Button("Ayuda");
            helpButton.setOnAction(event -> suggestMove());
            timeLabel = new Label("Tiempo: 00:00");
            movesLabel = new Label("Movimientos: 0");
            infoPanel.getChildren().addAll(helpButton, timeLabel, movesLabel);
        }

        startTimer(); // Iniciar el temporizador al crear la escena

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
                button.setStyle("-fx-font-size: 24px; -fx-background-color: #f0f0f0; -fx-border-color: #ccc; -fx-border-width: 1px;"); // Estilo inicial
                final int row = i;
                final int col = j;
                button.setOnAction(event -> handleTileClick(row, col));
                buttons[i][j] = button;
                gridPane.add(button, j, i);
            }
        }
        root.setCenter(gridPane);

        // PANEL INFERIOR (Botón de Regresar)
        HBox bottomPanel = new HBox(10);
        bottomPanel.setAlignment(Pos.CENTER);
        bottomPanel.setPadding(new Insets(10));
        Button backToMenuButton = new Button("Volver al Menú Principal");
        backToMenuButton.setOnAction(e -> {
            timer.stop(); // Detener el temporizador al salir del juego
            primaryStage.setScene(mainController.getLoginRegisterScene()); // O mainController.getMainMenuScene() si la tienes
        });
        bottomPanel.getChildren().add(backToMenuButton);
        root.setBottom(bottomPanel);


        Scene scene = new Scene(root, 600, 650);
        return scene;
    }

    private void startTimer() {
        seconds = 0; // Resetear tiempo al iniciar el juego
        movesCount = 0; // Resetear movimientos
        timeLabel.setText("Tiempo: 00:00");
        movesLabel.setText("Movimientos: 0");

        if (timer != null) {
            timer.stop(); // Detener si ya estaba corriendo
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
        if (game.moveTile(row, col)) {
            movesCount++;
            movesLabel.setText("Movimientos: " + movesCount);
            updateBoard();
            if (game.isGoalReached()) {
                timer.stop();
                double score = calculateScore(seconds, movesCount);
                String message = String.format("¡Puzzle resuelto por %s en %02d:%02d con %d movimientos!\nTu puntuación: %.2f",
                        username, (seconds / 60), (seconds % 60), movesCount, score);

                // Guardar la puntuación en el archivo
                ScoreManager scoreManager = new ScoreManager();
                scoreManager.updateUserScore(username, seconds, movesCount, score);

                showAlert("¡Felicidades!", message);
            }
        }
    }

    private void updateBoard() {
        int[][] board = game.getCurrentState().getBoard();
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                buttons[i][j].setText(board[i][j] == 0 ? "" : String.valueOf(board[i][j]));
                buttons[i][j].setStyle("-fx-font-size: 24px; -fx-background-color: #f0f0f0; -fx-border-color: #ccc; -fx-border-width: 1px;"); // Resetear estilo
            }
        }
        highlightedButton = null;
    }

    private void suggestMove() {
        PuzzleState currentState = game.getCurrentState();
        int currentManhattanDistance = game.calculateManhattanDistance(currentState);
        List<PuzzleState> nextPossibleStates = game.getPossibleNextStates(currentState);
        PuzzleState bestNextState = null;
        int minManhattanDistance = Integer.MAX_VALUE;

        for (PuzzleState nextState : nextPossibleStates) {
            int distance = game.calculateManhattanDistance(nextState);
            if (distance < minManhattanDistance) {
                minManhattanDistance = distance;
                bestNextState = nextState;
            }
        }

        if (bestNextState != null && minManhattanDistance < currentManhattanDistance) {
            int movedRow = -1, movedCol = -1;
            int[][] boardNow = currentState.getBoard();

            for (int i = 0; i < GRID_SIZE; i++) {
                for (int j = 0; j < GRID_SIZE; j++) {
                    if (boardNow[i][j] != bestNextState.getBoard()[i][j] && boardNow[i][j] != 0) {
                        movedRow = i;
                        movedCol = j;
                        break;
                    }
                }
                if (movedRow != -1) break;
            }

            if (movedRow != -1 && movedCol != -1) {
                // Simula un click para mover la pieza sugerida
                handleTileClick(movedRow, movedCol);
            }
        } else {
            showAlert("Sugerencia", "No hay un mejor movimiento inmediato disponible.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Calcula una puntuación: menos tiempo y menos movimientos = mayor puntuación
    private double calculateScore(int timeInSeconds, int moves) {
        // Una fórmula de ejemplo:
        // Puntuación base alta, penalizada por tiempo y movimientos.
        // Los movimientos tienen un peso ligeramente mayor que el tiempo.
        double baseScore = 10000.0;
        double timePenalty = timeInSeconds * 1.5; // Cada segundo resta 1.5 puntos
        double movesPenalty = moves * 5.0; // Cada movimiento resta 5 puntos

        double score = baseScore - timePenalty - movesPenalty;

        // Asegurarse de que la puntuación no sea negativa
        return Math.max(0, score);
    }
}