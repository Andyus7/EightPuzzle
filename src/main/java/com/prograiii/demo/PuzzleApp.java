package com.prograiii.demo;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.List;

public class PuzzleApp extends Application {

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

    @Override
    public void start(Stage primaryStage) throws IOException {
        game = new PuzzleGame();
        buttons = new Button[GRID_SIZE][GRID_SIZE];

        // Layout principal
        BorderPane root = new BorderPane();

        // Panel para la información (timer, movimientos, ayuda)
        HBox infoPanel = new HBox(15);
        infoPanel.setAlignment(Pos.TOP_RIGHT);
        infoPanel.setPadding(new Insets(10));

        // Botón de ayuda
        Image lightbulbImage = new Image(getClass().getResourceAsStream("/icons/lightbulb.png"));
        ImageView lightbulbIcon = new ImageView(lightbulbImage);
        lightbulbIcon.setFitHeight(20);
        lightbulbIcon.setFitWidth(20);
        helpButton = new Button();
        helpButton.setGraphic(lightbulbIcon);
        helpButton.setOnAction(event -> suggestMove());
        infoPanel.getChildren().add(helpButton);

        // Timer
        Image clockImage = new Image(getClass().getResourceAsStream("/icons/clock.png"));
        ImageView clockIcon = new ImageView(clockImage);
        clockIcon.setFitHeight(20);
        clockIcon.setFitWidth(20);
        timeLabel = new Label("Tiempo: 00:00");
        infoPanel.getChildren().addAll(clockIcon, timeLabel);
        startTimer();

        // Movimientos
        Image handImage = new Image(getClass().getResourceAsStream("/icons/hand.png"));
        ImageView handIcon = new ImageView(handImage);
        handIcon.setFitHeight(20);
        handIcon.setFitWidth(20);
        movesLabel = new Label("Movimientos: 0");
        infoPanel.getChildren().addAll(handIcon, movesLabel);

        root.setTop(infoPanel);

        // Grid del puzzle
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20));
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setAlignment(Pos.CENTER);

        // Crear los botones y agregarlos a la cuadrícula
        int[][] board = game.getCurrentState().getBoard();
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                Button button = new Button(board[i][j] == 0 ? "" : String.valueOf(board[i][j]));
                button.setMinSize(80, 80);
                final int row = i;
                final int col = j;
                button.setOnAction(event -> handleTileClick(row, col));
                buttons[i][j] = button;
                gridPane.add(button, j, i);
            }
        }

        root.setCenter(gridPane);

        Scene scene = new Scene(root, 600, 650);
        primaryStage.setTitle("8-Puzzle");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void startTimer() {
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
                System.out.println("¡Puzzle resuelto en " + (seconds / 60) + " minutos y " + (seconds % 60) + " segundos con " + movesCount + " movimientos!");
                // Aquí podríamos añadir lógica para indicar que el juego terminó visualmente
            }
        }
    }

    private void updateBoard() {
        int[][] board = game.getCurrentState().getBoard();
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                buttons[i][j].setText(board[i][j] == 0 ? "" : String.valueOf(board[i][j]));
                buttons[i][j].setStyle(""); // Resetear cualquier estilo anterior
            }
        }
        highlightedButton = null; // Deseleccionar el botón previamente sugerido
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
                handleTileClick(movedRow, movedCol);
            }
        }
    }

    public static void main(String[] args) {
        launch();
    }
}