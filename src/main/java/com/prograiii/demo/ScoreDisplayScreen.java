package com.prograiii.demo;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;



import java.util.List;

public class ScoreDisplayScreen {

    private String currentUsername; // Añade este campo
    private Stage primaryStage;
    private LoginRegisterScreen mainController; // Para volver al menú

    public ScoreDisplayScreen(Stage primaryStage, LoginRegisterScreen mainController, String currentUsername) {
        this.primaryStage = primaryStage;
        this.mainController = mainController;
        this.currentUsername = currentUsername; // Almacenar el username
    }

    public Scene createScoreScene() {
        VBox root = new VBox(15);
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #e0f2f7;"); // Un fondo claro

        Label title = new Label("Mejores Puntuaciones");
        title.setStyle("-fx-font-size: 30px; -fx-font-weight: bold; -fx-text-fill: #007bff;");

        VBox scoresContainer = new VBox(10);
        scoresContainer.setPadding(new Insets(10));
        scoresContainer.setStyle("-fx-background-color: white; -fx-border-color: #b0e0e6; -fx-border-width: 2px; -fx-border-radius: 5px;");

        ScoreManager scoreManager = new ScoreManager();
        List<ScoreManager.UserScore> topScores = scoreManager.getTopScores(10); // Obtener el top 10

        if (topScores.isEmpty()) {
            scoresContainer.getChildren().add(new Label("¡Nadie ha jugado todavía! Sé el primero."));
        } else {
            // Encabezados
            HBox header = new HBox(50);
            header.setAlignment(Pos.CENTER_LEFT);
            header.setPadding(new Insets(5));
            header.setStyle("-fx-font-weight: bold; -fx-background-color: #f0f8ff; -fx-border-color: #b0e0e6; -fx-border-width: 0 0 1px 0;");
            header.getChildren().addAll(
                    createStyledLabel("Rank", 50),
                    createStyledLabel("Usuario", 150),
                    createStyledLabel("Tiempo", 100),
                    createStyledLabel("Movs", 80),
                    createStyledLabel("Puntuación", 120)
            );
            scoresContainer.getChildren().add(header);


            int rank = 1;
            for (ScoreManager.UserScore score : topScores) {
                HBox scoreEntry = new HBox(50);
                scoreEntry.setAlignment(Pos.CENTER_LEFT);
                scoreEntry.setPadding(new Insets(5));
                scoreEntry.setStyle("-fx-border-color: #eee; -fx-border-width: 0 0 1px 0;"); // Separador de líneas

                int minutes = score.getBestTime() / 60;
                int seconds = score.getBestTime() % 60;
                String timeFormatted = String.format("%02d:%02d", minutes, seconds);

                scoreEntry.getChildren().addAll(
                        createStyledLabel(String.valueOf(rank++), 50),
                        createStyledLabel(score.getUsername(), 150),
                        createStyledLabel(timeFormatted, 100),
                        createStyledLabel(String.valueOf(score.getBestMoves()), 80),
                        createStyledLabel(String.format("%.2f", score.getBestScore()), 120)
                );
                scoresContainer.getChildren().add(scoreEntry);
            }
        }

        ScrollPane scrollPane = new ScrollPane(scoresContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setMaxHeight(300); // Limitar altura del scroll

        Button backButton = new Button("Volver al Menú Principal");
        backButton.setPrefSize(200, 40);
        backButton.setStyle("-fx-font-size: 18px; -fx-background-color: #f44336; -fx-text-fill: white; -fx-background-radius: 5;");
        backButton.setOnAction(e -> primaryStage.setScene(mainController.getMainMenuScene(currentUsername)));

        root.getChildren().addAll(title, scrollPane, backButton);

        return new Scene(root, 650, 500); // Ajusta el tamaño de la ventana
    }

    private Label createStyledLabel(String text, double prefWidth) {
        Label label = new Label(text);
        label.setPrefWidth(prefWidth);
        label.setAlignment(Pos.CENTER_LEFT); // Alineación del texto
        label.setStyle("-fx-font-size: 16px;");
        return label;
    }
}
