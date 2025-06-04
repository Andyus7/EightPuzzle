package com.prograiii.demo;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.*;

public class RegisterScreen {
    private LoginRegisterScreen mainController;

    public RegisterScreen(LoginRegisterScreen mainController) {
        this.mainController = mainController;
    }

    public Scene createRegisterScreen(Stage primaryStage) {
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
                System.out.println("El usuario ya existe.");
            } else {
                saveUser(username, password);
                System.out.println("Usuario registrado exitosamente.");
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
            // archivo no existe aún, no hay usuarios
        }
        return false;
    }

    private void saveUser(String username, String password) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("usuarios.txt", true))) {
            writer.write(username + ";" + password);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
