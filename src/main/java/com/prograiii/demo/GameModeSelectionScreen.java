package com.prograiii.demo;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GameModeSelectionScreen {

    private Stage primaryStage;
    private LoginRegisterScreen mainController;
    private String username;

    public GameModeSelectionScreen(Stage primaryStage, LoginRegisterScreen mainController, String username) {
        this.primaryStage = primaryStage;
        this.mainController = mainController;
        this.username = username;
    }

    public Scene createSelectionScene() {
        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #f0f8ff;"); // Fondo azul claro

        Label title = new Label("Selecciona Modo de Juego");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #333;");

        Button normalModeButton = new Button("Modo Normal");
        normalModeButton.setPrefSize(250, 60);
        normalModeButton.setStyle("-fx-font-size: 22px; -fx-background-color: #4CAF50; -fx-text-fill: white; -fx-background-radius: 8;");
        normalModeButton.setOnAction(e -> {
            mainController.goToGameScreen(username, false); // false para modo normal
        });

        Button intelligentModeButton = new Button("Modo Inteligente");
        intelligentModeButton.setPrefSize(250, 60);
        intelligentModeButton.setStyle("-fx-font-size: 22px; -fx-background-color: #2196F3; -fx-text-fill: white; -fx-background-radius: 8;");
        intelligentModeButton.setOnAction(e -> {
            // Navegar a la pantalla de configuración del modo inteligente
            IntelligentModeSetupScreen setupScreen = new IntelligentModeSetupScreen(primaryStage, mainController, username);
            primaryStage.setScene(setupScreen.createSetupScene());
            primaryStage.setTitle("8-Puzzle - Configuración Inteligente");
        });

        Button backButton = new Button("Volver al Menú Principal");
        backButton.setPrefSize(200, 40);
        backButton.setStyle("-fx-font-size: 18px; -fx-background-color: #f44336; -fx-text-fill: white; -fx-background-radius: 5;");
        backButton.setOnAction(e -> primaryStage.setScene(mainController.getMainMenuScene(username)));

        root.getChildren().addAll(title, normalModeButton, intelligentModeButton, backButton);

        return new Scene(root, 600, 450);
    }
}