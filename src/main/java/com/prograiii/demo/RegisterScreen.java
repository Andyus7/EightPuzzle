package com.prograiii.demo;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;


import java.io.*;

public class RegisterScreen {
    private LoginRegisterScreen mainController;
    private Stage primaryStage; // Añadir primaryStage como campo de instancia

    public RegisterScreen(LoginRegisterScreen mainController, Stage primaryStage) {
        this.mainController = mainController;
        this.primaryStage = primaryStage; // Inicializar primaryStage en el constructor
    }

    public Scene createRegisterScreen() { // Ya no necesita primaryStage como parámetro aquí
        VBox root = new VBox(10);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));

        Label titleLabel = new Label("Registrarse");
        TextField usernameField = new TextField();
        usernameField.setPromptText("Nuevo Usuario");
        TextField passwordField = new TextField();
        passwordField.setPromptText("Contraseña");
        Button registerButton = new Button("Registrar");
        Button backButton = new Button("Volver");

        root.getChildren().addAll(titleLabel, usernameField, passwordField, registerButton, backButton);

        backButton.setOnAction(e -> primaryStage.setScene(mainController.getLoginRegisterScene()));

        registerButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            if (userExists(username)) {
                showAlert("Error", "El usuario ya existe.");
            } else {
                saveUser(username, password);
                showAlert("Registro exitoso", "Usuario registrado correctamente.");
                primaryStage.setScene(mainController.getLoginRegisterScene());
            }
        });


        return new Scene(root, 300, 250);
    }

    private boolean userExists(String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader("usuarios.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] datos = line.split(";");
                if (datos.length > 0 && datos[0].equals(username)) {
                    return true;
                }
            }
        } catch (IOException e) {
            // archivo no existe aún, no hay usuarios, se puede ignorar o registrar
            System.out.println("Archivo de usuarios no encontrado. Se creará al registrar el primer usuario.");
        }
        return false;
    }

    private void saveUser(String username, String password) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("usuarios.txt", true))) {
            writer.write(username + ";" + password);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error de Guardado", "No se pudo guardar el usuario.");
        }
    }

    // Método showAlert añadido
    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}