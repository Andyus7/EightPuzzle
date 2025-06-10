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
    private LoginScreen loginScreen; // Instancia de LoginScreen
    private RegisterScreen registerScreen; // Instancia de RegisterScreen

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.initStyle(StageStyle.UNDECORATED); // Elimina el marco de la ventana

        // Crear instancias de LoginScreen y RegisterScreen, pasándoles 'this' (el controlador principal)
        // y el primaryStage, según los constructores actualizados.
        this.loginScreen = new LoginScreen(this, primaryStage);
        this.registerScreen = new RegisterScreen(this, primaryStage);

        // Crear las escenas
        loginRegisterScene = createLoginRegisterScene();
        // createLoginScreen() y createRegisterScreen() ya no necesitan el primaryStage como parámetro
        loginScene = loginScreen.createLoginScreen();
        registerScene = registerScreen.createRegisterScreen();

        primaryStage.setTitle("Bienvenido al 8-Puzzle");
        primaryStage.setScene(loginRegisterScene); // Mostrar la escena inicial de login/registro
        primaryStage.show();
    }

    // Campo para almacenar la escena de login, para que LoginScreen pueda acceder a ella
    private Scene loginScene;
    // Campo para almacenar la escena de registro, para que RegisterScreen pueda acceder a ella
    private Scene registerScene;


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
        loginButton.setPrefWidth(200); // Establecer un ancho preferido para los botones
        loginButton.setPrefHeight(40); // Establecer una altura preferida
        loginButton.setStyle("-fx-font-size: 18px; -fx-background-color: #4CAF50; -fx-text-fill: white; -fx-background-radius: 5;");


        Button registerButton = new Button("Registrarse");
        registerButton.setPrefWidth(200);
        registerButton.setPrefHeight(40);
        registerButton.setStyle("-fx-font-size: 18px; -fx-background-color: #2196F3; -fx-text-fill: white; -fx-background-radius: 5;");


        Button exitButton = new Button("Salir");
        exitButton.setPrefWidth(100);
        exitButton.setPrefHeight(30);
        exitButton.setStyle("-fx-font-size: 16px; -fx-background-color: #f44336; -fx-text-fill: white; -fx-background-radius: 5;");


        VBox buttonsBox = new VBox(15); // Aumenta el espaciado entre botones
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

        // Manejadores de eventos para los botones
        exitButton.setOnAction(e -> primaryStage.close());
        loginButton.setOnAction(e -> primaryStage.setScene(loginScene)); // Cambia a la escena de login
        registerButton.setOnAction(e -> primaryStage.setScene(registerScene)); // Cambia a la escena de registro

        return new Scene(mainPane, 600, 400);
    }

    // Método para obtener la escena principal de login/registro (usado por LoginScreen y RegisterScreen)
    public Scene getLoginRegisterScene() {
        return loginRegisterScene;
    }

    // Métodos para acceder a las escenas de login y registro
    public Scene getLoginScene() {
        return loginScene;
    }

    public Scene getRegisterScene() {
        return registerScene;
    }

    public static void main(String[] args) {
        launch();
    }
}