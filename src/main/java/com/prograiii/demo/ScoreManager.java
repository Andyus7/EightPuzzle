package com.prograiii.demo;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ScoreManager {

    private static final String USERS_FILE = "usuarios.txt";

    // Clase interna para representar los datos de un usuario con puntuación
    public static class UserScore {
        String username;
        String password;
        int bestTime;
        int bestMoves;
        double bestScore;

        public UserScore(String username, String password, int bestTime, int bestMoves, double bestScore) {
            this.username = username;
            this.password = password;
            this.bestTime = bestTime;
            this.bestMoves = bestMoves;
            this.bestScore = bestScore;
        }

        // Getters
        public String getUsername() { return username; }
        public String getPassword() { return password; }
        public int getBestTime() { return bestTime; }
        public int getBestMoves() { return bestMoves; }
        public double getBestScore() { return bestScore; }

        @Override
        public String toString() {
            return String.format("%s;%s;%d;%d;%.2f", username, password, bestTime, bestMoves, bestScore);
        }
    }

    // Lee todos los usuarios del archivo
    public List<UserScore> getAllUsers() {
        List<UserScore> users = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length >= 2) { // Asegurarse de que al menos hay usuario y contraseña
                    String username = parts[0];
                    String password = parts[1];
                    int bestTime = 0; // Valores por defecto
                    int bestMoves = 0;
                    double bestScore = 0.0;

                    // Si hay suficientes partes, intentar parsear los datos de puntuación
                    if (parts.length >= 5) {
                        try {
                            bestTime = Integer.parseInt(parts[2]);
                            bestMoves = Integer.parseInt(parts[3]);
                            bestScore = Double.parseDouble(parts[4]);
                        } catch (NumberFormatException e) {
                            System.err.println("Advertencia: Datos de puntuación inválidos para el usuario " + username + " en la línea: " + line + ". Se usarán valores predeterminados. Error: " + e.getMessage());
                            // Si falla el parseo, los valores predeterminados (0, 0, 0.0) se mantendrán
                        }
                    }
                    users.add(new UserScore(username, password, bestTime, bestMoves, bestScore));
                } else {
                    System.err.println("Advertencia: Línea de usuario inválida (pocos campos): " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo de usuarios: " + e.getMessage());
            // Si el archivo no existe, simplemente devuelve una lista vacía, lo cual está bien.
        }
        return users;
    }

    // Actualiza la puntuación de un usuario específico
    public void updateUserScore(String usernameToUpdate, int newTime, int newMoves, double newScore) {
        List<UserScore> users = getAllUsers();
        boolean userFound = false;
        List<UserScore> updatedUsers = new ArrayList<>();

        for (UserScore user : users) {
            if (user.getUsername().equals(usernameToUpdate)) {
                userFound = true;
                // Solo actualiza si la nueva puntuación es mejor o si no había puntuación previa
                if (newScore > user.getBestScore() || user.getBestScore() == 0.0) {
                    updatedUsers.add(new UserScore(user.getUsername(), user.getPassword(), newTime, newMoves, newScore));
                    System.out.println("Puntuación de " + usernameToUpdate + " actualizada a: " + newScore);
                } else {
                    updatedUsers.add(user); // Mantiene la puntuación anterior si la nueva no es mejor
                    System.out.println("La puntuación de " + usernameToUpdate + " (" + newScore + ") no es mejor que la actual (" + user.getBestScore() + ").");
                }
            } else {
                updatedUsers.add(user);
            }
        }

        // Si el usuario no existía, podrías añadirlo (aunque se espera que ya exista por el login)
        if (!userFound) {
            System.out.println("Usuario " + usernameToUpdate + " no encontrado para actualizar puntuación. Considera añadirlo.");
            // Esto solo ocurriría si el archivo fue modificado manualmente o si el usuario no pasó por registro/login
            updatedUsers.add(new UserScore(usernameToUpdate, "", newTime, newMoves, newScore)); // Contraseña vacía por defecto
        }

        // Escribir todas las puntuaciones de nuevo al archivo
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USERS_FILE))) {
            for (UserScore user : updatedUsers) {
                writer.write(user.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error al escribir en el archivo de usuarios: " + e.getMessage());
        }
    }

    // Obtiene el top N de jugadores por puntuación
    public List<UserScore> getTopScores(int count) {
        return getAllUsers().stream()
                .filter(user -> user.getBestScore() > 0) // Solo usuarios con puntuación
                .sorted(Comparator.comparingDouble(UserScore::getBestScore).reversed()) // Orden descendente (mayor puntuación primero)
                .limit(count)
                .collect(Collectors.toList());
    }
}