package com.prograiii.demo;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class LoginRegisterScreen extends Application {

    private Stage primaryStage;
    private Scene loginRegisterScene;
    private LoginScreen loginScreen;
    private RegisterScreen registerScreen;
    private MainMenu mainMenu;
    private String currentUsername;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.initStyle(StageStyle.UNDECORATED);

        this.loginScreen = new LoginScreen(this, primaryStage);
        this.registerScreen = new RegisterScreen(this, primaryStage);

        loginRegisterScene = createLoginRegisterScene();

        primaryStage.setTitle("Bienvenido al 8-Puzzle");
        primaryStage.setScene(loginRegisterScene);
        primaryStage.show();
    }

    public Scene createLoginRegisterScene() {
        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));
        root.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));

        Label title = new Label("8-Puzzle");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 60));
        LinearGradient gradient = new LinearGradient(
                0, 0, 1, 0, true, null,
                new Stop[]{
                        new Stop(0, Color.GOLD),
                        new Stop(1, Color.DARKGOLDENROD)
                }
        );
        title.setTextFill(gradient);

        Button loginButton = new Button("Iniciar Sesión");
        loginButton.setPrefWidth(200);
        loginButton.setPrefHeight(40);
        loginButton.setStyle("-fx-font-size: 18px; -fx-background-color: #4CAF50; -fx-text-fill: white; -fx-background-radius: 5;");

        Button registerButton = new Button("Registrarse");
        registerButton.setPrefWidth(200);
        registerButton.setPrefHeight(40);
        registerButton.setStyle("-fx-font-size: 18px; -fx-background-color: #2196F3; -fx-text-fill: white; -fx-background-radius: 5;");

        Button exitButton = new Button("Salir");
        exitButton.setPrefWidth(100);
        exitButton.setPrefHeight(30);
        exitButton.setStyle("-fx-font-size: 16px; -fx-background-color: #f44336; -fx-text-fill: white; -fx-background-radius: 5;");

        VBox buttonsBox = new VBox(15);
        buttonsBox.setAlignment(Pos.CENTER);
        buttonsBox.getChildren().addAll(loginButton, registerButton);

        StackPane bottomPane = new StackPane();
        bottomPane.setAlignment(Pos.BOTTOM_RIGHT);
        bottomPane.setPadding(new Insets(10));
        bottomPane.getChildren().add(exitButton);

        BorderPane mainPane = new BorderPane();
        mainPane.setCenter(root);
        mainPane.setBottom(bottomPane);

        root.getChildren().addAll(title, buttonsBox);

        exitButton.setOnAction(e -> primaryStage.close());
        loginButton.setOnAction(e -> primaryStage.setScene(loginScreen.createLoginScreen()));
        registerButton.setOnAction(e -> primaryStage.setScene(registerScreen.createRegisterScreen()));

        return new Scene(mainPane, 600, 400);
    }

    public Scene getLoginRegisterScene() {
        return loginRegisterScene;
    }

    public Scene getMainMenuScene(String username) {
        this.currentUsername = username;
        if (mainMenu == null || !mainMenu.getUsername().equals(username)) {
            mainMenu = new MainMenu(username, primaryStage, this);
        }
        return mainMenu.createMainMenuScene();
    }

    public String getCurrentUsername() {
        return currentUsername;
    }

    // Nuevo método para navegar a la pantalla de juego (modo normal)
    public void goToGameScreen(String username, boolean intelligentMode) {
        this.currentUsername = username;
        PuzzleApp puzzleApp = new PuzzleApp(username, intelligentMode, primaryStage, this);
        primaryStage.setScene(puzzleApp.createGameScene());
        primaryStage.setTitle("8-Puzzle - Jugando como " + username);
    }

    // Nuevo método para navegar a la pantalla de juego (modo inteligente con estados definidos)
    public void goToGameScreen(String username, boolean intelligentMode, PuzzleState initialState, PuzzleState goalState) {
        this.currentUsername = username;
        PuzzleApp puzzleApp = new PuzzleApp(username, intelligentMode, primaryStage, this, initialState, goalState);
        primaryStage.setScene(puzzleApp.createGameScene());
        primaryStage.setTitle("8-Puzzle - Modo Inteligente (" + username + ")");
    }

    public static void main(String[] args) {
        launch();
    }
}