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
    private Stage primaryStage; // Campo para primaryStage
    private LoginRegisterScreen mainController; // Campo para mainController

    // Constructor corregido para recibir Stage y LoginRegisterScreen
    public MainMenu(String username, Stage primaryStage, LoginRegisterScreen mainController) {
        this.username = username;
        this.primaryStage = primaryStage;
        this.mainController = mainController;
    }

    public Scene createMainMenuScene() { // No necesita primaryStage como parámetro, ya es un campo
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));

        // Título del juego
        Label title = new Label("8- PUZZLE GAME");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        BorderPane.setAlignment(title, Pos.CENTER);
        root.setTop(title);

        // Bienvenida al usuario
        Label welcomeLabel = new Label("Bienvenido, " + username + "!");
        welcomeLabel.setStyle("-fx-font-size: 16px;");
        BorderPane.setAlignment(welcomeLabel, Pos.CENTER);
        root.setBottom(welcomeLabel);

        // Controles del juego (Ejemplo, puedes expandirlo)
        VBox gameControls = new VBox(15);
        gameControls.setAlignment(Pos.CENTER);
        gameControls.setPadding(new Insets(20));

        Button startGameButton = new Button("Iniciar Juego");
        startGameButton.setPrefWidth(150);
        startGameButton.setOnAction(e -> {
            // Aquí deberías navegar a la escena del juego 8-puzzle
            System.out.println("Iniciando juego para " + username);
            // Ejemplo: Assuming you have a PuzzleGame class and a createPuzzleGameScene method
            // PuzzleGame puzzleGame = new PuzzleGame(primaryStage, this.mainController);
            // primaryStage.setScene(puzzleGame.createPuzzleGameScene());
        });

        Button logoutButton = new Button("Cerrar Sesión");
        logoutButton.setPrefWidth(150);
        logoutButton.setOnAction(e -> {
            // Regresar a la pantalla de Login/Registro
            primaryStage.setScene(mainController.getLoginRegisterScene());
            System.out.println("Sesión cerrada.");
        });

        gameControls.getChildren().addAll(startGameButton, logoutButton);
        root.setCenter(gameControls);

        return new Scene(root, 600, 400); // Tamaño de la ventana del menú principal
    }
}