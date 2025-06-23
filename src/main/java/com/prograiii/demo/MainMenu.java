package com.prograiii.demo;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainMenu {

    private String username;
    private Stage primaryStage;
    private LoginRegisterScreen mainController;

    public MainMenu(String username, Stage primaryStage, LoginRegisterScreen mainController) {
        this.username = username;
        this.primaryStage = primaryStage;
        this.mainController = mainController;
    }

    public Scene createMainMenuScene() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #F8F8F8;");

        Label title = new Label("8- PUZZLE GAME");
        title.setStyle("-fx-font-size: 40px; -fx-font-weight: bold; -fx-text-fill: #333;");
        BorderPane.setAlignment(title, Pos.CENTER);
        root.setTop(title);

        Label welcomeLabel = new Label("Bienvenido, " + username + "!");
        welcomeLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: #555;");
        BorderPane.setAlignment(welcomeLabel, Pos.CENTER);
        // Eliminado del .setBottom, ahora se añade al VBox central

        VBox gameControls = new VBox(20);
        gameControls.setAlignment(Pos.CENTER);
        gameControls.setPadding(new Insets(20));

        Button startGameButton = new Button("Iniciar Juego");
        startGameButton.setPrefSize(200, 50);
        startGameButton.setStyle("-fx-font-size: 20px; -fx-background-color: #4CAF50; -fx-text-fill: white; -fx-background-radius: 5;");
        startGameButton.setOnAction(e -> {
            System.out.println("Navegando a selección de modo de juego...");
            // Navegar a la pantalla de selección de modo de juego
            GameModeSelectionScreen selectionScreen = new GameModeSelectionScreen(primaryStage, mainController, username);
            primaryStage.setScene(selectionScreen.createSelectionScene());
            primaryStage.setTitle("8-Puzzle - Seleccionar Modo");
        });

        Button scoresButton = new Button("Ver Puntajes");
        scoresButton.setPrefSize(200, 50);
        scoresButton.setStyle("-fx-font-size: 20px; -fx-background-color: #008CBA; -fx-text-fill: white; -fx-background-radius: 5;");
        scoresButton.setOnAction(e -> {
            System.out.println("Mostrando puntajes...");
            ScoreDisplayScreen scoreDisplay = new ScoreDisplayScreen(primaryStage, mainController, username); // Pasa el username
            primaryStage.setScene(scoreDisplay.createScoreScene());
            primaryStage.setTitle("8-Puzzle - Mejores Puntajes");
        });

        Button logoutButton = new Button("Cerrar Sesión");
        logoutButton.setPrefSize(200, 50);
        logoutButton.setStyle("-fx-font-size: 20px; -fx-background-color: #f44336; -fx-text-fill: white; -fx-background-radius: 5;");
        logoutButton.setOnAction(e -> {
            primaryStage.setScene(mainController.getLoginRegisterScene());
            System.out.println("Sesión cerrada.");
        });

        gameControls.getChildren().addAll(welcomeLabel, startGameButton, scoresButton, logoutButton);
        root.setCenter(gameControls);

        return new Scene(root, 600, 450); // Aumentado ligeramente el tamaño
    }

    public String getUsername() {
        return username;
    }
}