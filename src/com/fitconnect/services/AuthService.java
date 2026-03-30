package com.fitconnect.services;

import com.fitconnect.config.DatabaseConnection;
import com.fitconnect.models.Utilisateur;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;

/**
 * Service gérant l'authentification des administrateurs
 */
public class AuthService {
    private Connection connection;
    
    public AuthService() throws SQLException {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }
    
    /**
     * Authentifier un administrateur
     * @param email Email de l'admin
     * @param password Mot de passe en clair
     * @return Utilisateur si authentification réussie, null sinon
     * @throws SQLException
     */
    public Utilisateur login(String email, String password) throws SQLException {
        String query = "SELECT * FROM utilisateur WHERE email = ? AND role = 'ADMIN' AND actif = 1";
        
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, email);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String hashedPassword = rs.getString("mot_de_passe");
                    
                    // AJOUT : Afficher les infos pour debug
                    System.out.println("=== DEBUG LOGIN ===");
                    System.out.println("Email trouvé : " + email);
                    System.out.println("Hash en BDD : " + hashedPassword);
                    System.out.println("Password saisi : " + password);
                    System.out.println("Longueur hash : " + hashedPassword.length());
                    System.out.println("Commence par $2a$ ? " + hashedPassword.startsWith("$2a$"));
                    
                    // Vérification du mot de passe avec BCrypt
                    try {
                        boolean isValid = BCrypt.checkpw(password, hashedPassword);
                        System.out.println("Résultat BCrypt : " + isValid);
                        
                        if (isValid) {
                            Utilisateur user = new Utilisateur();
                            user.setIdUtilisateur(rs.getInt("id_utilisateur"));
                            user.setEmail(rs.getString("email"));
                            user.setRole(rs.getString("role"));
                            user.setActif(rs.getBoolean("actif"));
                            user.setDateCreation(rs.getTimestamp("date_creation"));
                            
                            return user;
                        }
                    } catch (Exception e) {
                        System.out.println("ERREUR BCrypt : " + e.getMessage());
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("Aucun utilisateur trouvé avec cet email");
                }
            }
        }
        
        return null;
    }
    
    /**
     * Vérifier si un email existe déjà
     * @param email Email à vérifier
     * @return true si l'email existe
     * @throws SQLException
     */
    public boolean emailExists(String email) throws SQLException {
        String query = "SELECT COUNT(*) FROM utilisateur WHERE email = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, email);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        
        return false;
    }
    
    /**
     * Créer un nouvel utilisateur
     * @param email Email
     * @param password Mot de passe en clair
     * @param role Rôle (CLIENT, COACH, ADMIN)
     * @return ID de l'utilisateur créé ou 0 si échec
     * @throws SQLException
     */
    public int createUser(String email, String password, String role) throws SQLException {
        String query = "INSERT INTO utilisateur (email, mot_de_passe, role, actif) VALUES (?, ?, ?, 1)";
        
        // Hacher le mot de passe avec BCrypt (compatible avec PHP password_hash)
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(10));
        
        try (PreparedStatement pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, email);
            pstmt.setString(2, hashedPassword);
            pstmt.setString(3, role);
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    }
                }
            }
        }
        
        return 0;
    }
}
