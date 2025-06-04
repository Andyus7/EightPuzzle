package com.prograiii.demo;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LoginScreen {
    private LoginRegisterScreen mainController;

    public LoginScreen(LoginRegisterScreen mainController) {
        this.mainController = mainController;
    }

    public Scene createLoginScreen(Stage primaryStage) {
        VBox root = new VBox(10);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));

        Label titleLabel = new Label("Iniciar Sesión");
        TextField usernameField = new TextField();
        usernameField.setPromptText("Usuario");
        TextField passwordField = new TextField();
        passwordField.setPromptText("Contraseña");
        Button loginButton = new Button("Iniciar");
        Button backButton = new Button("Volver");

        root.getChildren().addAll(titleLabel, usernameField, passwordField, loginButton, backButton);

        backButton.setOnAction(e -> primaryStage.setScene(mainController.getLoginRegisterScene()));

        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            if (verifyCredentials(username, password)) {
                System.out.println("Inicio de sesión exitoso.");
                // Aquí puedes cargar la pantalla del juego
            } else {
                System.out.println("Usuario o contraseña incorrectos.");
            }
        });

        return new Scene(root, 300, 250);
    }

    private boolean verifyCredentials(String username, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader("usuarios.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] datos = line.split(";");
                if (datos.length == 2 && datos[0].equals(username) && datos[1].equals(password)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
