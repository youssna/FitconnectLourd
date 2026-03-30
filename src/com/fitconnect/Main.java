package com.fitconnect;

import com.fitconnect.config.DatabaseConnection;
import com.fitconnect.views.LoginFrame;

import javax.swing.*;
import java.sql.SQLException;

/**
 * Classe principale de l'application FitConnect Admin
 * 
 * @author Votre Nom
 * @version 1.0
 */
public class Main {
    
    public static void main(String[] args) {
        // Test de connexion à la base de données
        System.out.println("===========================================");
        System.out.println("   FitConnect - Application Admin Java    ");
        System.out.println("===========================================");
        System.out.println();
        
        try {
            // Vérifier la connexion
            DatabaseConnection db = DatabaseConnection.getInstance();
            if (db.testConnection()) {
                System.out.println("✓ Connexion à la base de données réussie");
            } else {
                System.err.println("✗ Échec de connexion à la base de données");
                System.exit(1);
            }
        } catch (SQLException e) {
            System.err.println("✗ Erreur de connexion : " + e.getMessage());
            System.err.println("\nVeuillez vérifier :");
            System.err.println("  1. MySQL est bien démarré");
            System.err.println("  2. La base de données 'fitconnect' existe");
            System.err.println("  3. Les identifiants dans DatabaseConnection.java sont corrects");
            System.err.println("  4. Le driver MySQL Connector est dans le classpath");
            System.exit(1);
        }
        
        System.out.println();
        System.out.println("Démarrage de l'interface graphique...");
        System.out.println();
        
        // Définir le Look & Feel système
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Avertissement : impossible de définir le Look & Feel système");
        }
        
        // Lancer l'interface graphique
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
}
