package com.fitconnect.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Singleton pour gérer la connexion à la base de données MySQL
 * Compatible avec la version PHP (même structure de tables)
 */
public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;
    
    // Configuration (à modifier selon votre environnement)
    private static final String URL = "jdbc:mysql://127.0.0.1:8889/fitconnect?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String USER = "root";
    private static final String PASSWORD = "root";
    
    /**
     * Constructeur privé (pattern Singleton)
     */
    private DatabaseConnection() throws SQLException {
        try {
            // Chargement du driver MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("✓ Connexion à la base de données établie");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver MySQL introuvable. Assurez-vous que mysql-connector-java est dans le classpath.", e);
        } catch (SQLException e) {
            throw new SQLException("Erreur de connexion à la base de données : " + e.getMessage(), e);
        }
    }
    
    /**
     * Récupérer l'instance unique de la connexion
     * @return Instance DatabaseConnection
     * @throws SQLException
     */
    public static DatabaseConnection getInstance() throws SQLException {
        if (instance == null || instance.getConnection().isClosed()) {
            instance = new DatabaseConnection();
        }
        return instance;
    }
    
    /**
     * Obtenir la connexion JDBC
     * @return Connection
     */
    public Connection getConnection() {
        return connection;
    }
    
    /**
     * Fermer la connexion
     */
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("✓ Connexion fermée");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la fermeture : " + e.getMessage());
        }
    }
    
    /**
     * Test de connexion
     * @return true si la connexion est active
     */
    public boolean testConnection() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
}
