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
    private Scene loginScene;
    private Scene registerScene;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.initStyle(StageStyle.UNDECORATED);

        loginRegisterScene = createLoginRegisterScene();

        LoginScreen loginScreen = new LoginScreen(this);
        loginScene = loginScreen.createLoginScreen(primaryStage);

        RegisterScreen registerScreen = new RegisterScreen(this);
        registerScene = registerScreen.createRegisterScreen(primaryStage);

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
        Button registerButton = new Button("Registrarse");
        Button exitButton = new Button("Salir");

        VBox buttonsBox = new VBox(10);
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
        loginButton.setOnAction(e -> primaryStage.setScene(loginScene));
        registerButton.setOnAction(e -> primaryStage.setScene(registerScene));

        return new Scene(mainPane, 600, 400);
    }

    public Scene getLoginRegisterScene() {
        return loginRegisterScene;
    }

    public static void main(String[] args) {
        launch();
    }
}
