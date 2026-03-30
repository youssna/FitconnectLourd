package com.fitconnect.dao;

import com.fitconnect.config.DatabaseConnection;
import com.fitconnect.models.Client;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientDAO implements IClientDAO {
    private Connection connection;
    
    public ClientDAO() throws SQLException {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }
    
    @Override
    public List<Client> getAll() throws SQLException {
        List<Client> clients = new ArrayList<>();
    
        String query = "SELECT c.*, CONCAT(co.prenom, ' ', co.nom) AS nom_coach " +
                      "FROM client c " +
                      "LEFT JOIN coach co ON c.id_coach = co.id_coach " +
                      "ORDER BY c.date_inscription DESC";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                clients.add(mapResultSetToClient(rs));
            }
        }
        return clients;
    }
    
    @Override
    public Client getById(int id) throws SQLException {
        String query = "SELECT c.*, CONCAT(co.prenom, ' ', co.nom) AS nom_coach " +
                      "FROM client c " +
                      "LEFT JOIN coach co ON c.id_coach = co.id_coach " +
                      "WHERE c.id_client = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return mapResultSetToClient(rs);
            }
        }
        return null;
    }
    
    @Override
    public int create(Client client) throws SQLException {
       
        String query = "INSERT INTO client (nom, prenom, poids, taille, " +
                      "objectif, motivation, mail, telephone, mot_de_passe) " +
                      "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, client.getNom());
            pstmt.setString(2, client.getPrenom());
            pstmt.setDouble(3, client.getPoids());
            pstmt.setInt(4, client.getTaille());
            pstmt.setString(5, client.getObjectif());
            pstmt.setString(6, client.getMotivation());
            pstmt.setString(7, client.getEmail());
            pstmt.setString(8, client.getTelephone());
            
        
            String rawPassword = client.getMotDePasse();
            if (rawPassword != null && !rawPassword.trim().isEmpty()) {
                String hashedPassword = BCrypt.hashpw(rawPassword, BCrypt.gensalt(10));
                pstmt.setString(9, hashedPassword);
            } else {
                pstmt.setString(9, ""); 
            }
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) return generatedKeys.getInt(1);
                }
            }
        }
        return 0;
    }
    
    @Override
    public boolean update(Client client) throws SQLException {
        String query = "UPDATE client SET nom = ?, prenom = ?, poids = ?, taille = ?, " +
                      "objectif = ?, motivation = ?, id_coach = ?, mail = ?, telephone = ? " +
                      "WHERE id_client = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, client.getNom());
            pstmt.setString(2, client.getPrenom());
            pstmt.setDouble(3, client.getPoids());
            pstmt.setInt(4, client.getTaille());
            pstmt.setString(5, client.getObjectif());
            pstmt.setString(6, client.getMotivation());
            
            if (client.getIdCoach() != null) pstmt.setInt(7, client.getIdCoach());
            else pstmt.setNull(7, Types.INTEGER);

            pstmt.setString(8, client.getEmail());
            pstmt.setString(9, client.getTelephone());
            pstmt.setInt(10, client.getIdClient());
            
            return pstmt.executeUpdate() > 0;
        }
    }
    
    @Override
    public boolean delete(int id) throws SQLException {
        String query = "DELETE FROM client WHERE id_client = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        }
    }
    
    @Override
    public List<Client> getByCoach(int idCoach) throws SQLException {
        List<Client> clients = new ArrayList<>();
        String query = "SELECT c.*, CONCAT(co.prenom, ' ', co.nom) AS nom_coach " +
                       "FROM client c LEFT JOIN coach co ON c.id_coach = co.id_coach " +
                       "WHERE c.id_coach = ? ORDER BY c.nom, c.prenom";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, idCoach);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) clients.add(mapResultSetToClient(rs));
            }
        }
        return clients;
    }
    
    @Override
    public boolean assignerCoach(int idClient, int idCoach) throws SQLException {
        String query = "UPDATE client SET id_coach = ? WHERE id_client = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            if (idCoach > 0) pstmt.setInt(1, idCoach);
            else pstmt.setNull(1, Types.INTEGER);
            pstmt.setInt(2, idClient);
            return pstmt.executeUpdate() > 0;
        }
    }
    
    @Override
    public List<Client> rechercher(String recherche) throws SQLException {
        List<Client> clients = new ArrayList<>();
        String query = "SELECT c.*, CONCAT(co.prenom, ' ', co.nom) AS nom_coach " +
                       "FROM client c LEFT JOIN coach co ON c.id_coach = co.id_coach " +
                       "WHERE c.nom LIKE ? OR c.prenom LIKE ? OR c.mail LIKE ? OR c.telephone LIKE ? " +
                       "ORDER BY c.nom, c.prenom";
        String searchPattern = "%" + recherche + "%";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            pstmt.setString(3, searchPattern);
            pstmt.setString(4, searchPattern); 
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) clients.add(mapResultSetToClient(rs));
            }
        }
        return clients;
    }
    
 
    private Client mapResultSetToClient(ResultSet rs) throws SQLException {
        Client client = new Client();
        client.setIdClient(rs.getInt("id_client"));   
        client.setNom(rs.getString("nom"));
        client.setPrenom(rs.getString("prenom"));
        client.setPoids(rs.getDouble("poids"));
        client.setTaille(rs.getInt("taille"));
        client.setObjectif(rs.getString("objectif"));
        client.setMotivation(rs.getString("motivation"));
        
        int idCoach = rs.getInt("id_coach");
        client.setIdCoach(rs.wasNull() ? null : idCoach);
        
        client.setEmail(rs.getString("mail")); 
        client.setTelephone(rs.getString("telephone")); 
        
        client.setDateInscription(rs.getTimestamp("date_inscription"));
        
        String nomCoach = rs.getString("nom_coach");
        client.setNomCoach(nomCoach != null ? nomCoach : "Non assigné");
        
        return client;
    }
}