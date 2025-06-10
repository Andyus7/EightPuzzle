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

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;


public class LoginScreen {
    private Scene menuScene;
    private LoginRegisterScreen mainController;
    private Stage primaryStage; // Añadir primaryStage como campo de instancia

    public LoginScreen(LoginRegisterScreen mainController, Stage primaryStage) {
        this.mainController = mainController;
        this.primaryStage = primaryStage; // Inicializar primaryStage en el constructor
    }

    public void goToMainMenu(String username) {
        // Pasar primaryStage y mainController al constructor de MainMenu
        MainMenu menuScreen = new MainMenu(username, this.primaryStage, this.mainController);
        this.menuScene = menuScreen.createMainMenuScene(); // createMainMenuScene ya no necesita primaryStage
        this.primaryStage.setScene(menuScene);
    }

    public Scene createLoginScreen() { // Ya no necesita primaryStage como parámetro aquí
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
                showAlert("Éxito", "Inicio de sesión exitoso.");
                goToMainMenu(username); // Navegar al menú principal
            } else {
                System.out.println("Usuario o contraseña incorrectos.");
                showAlert("Error", "Usuario o contraseña incorrectos.");
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
            showAlert("Error de Archivo", "No se pudo leer el archivo de usuarios.");
        }
        return false;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}