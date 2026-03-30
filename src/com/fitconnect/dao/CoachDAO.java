package com.fitconnect.dao;

import com.fitconnect.config.DatabaseConnection;
import com.fitconnect.models.Coach;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CoachDAO implements ICoachDAO {
    private Connection connection;
    
    public CoachDAO() throws SQLException {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }
    
    @Override
    public List<Coach> getAll() throws SQLException {
        List<Coach> coachs = new ArrayList<>();
        String query = "SELECT *, 0 AS nombre_clients FROM coach ORDER BY valide DESC, nom, prenom";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) coachs.add(mapResultSetToCoach(rs));
        }
        return coachs;
    }
    
    @Override
    public Coach getById(int id) throws SQLException {
        String query = "SELECT *, 0 AS nombre_clients FROM coach WHERE id_coach = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return mapResultSetToCoach(rs);
            }
        }
        return null;
    }
    
    @Override
    public int create(Coach coach) throws SQLException {
        // SQL nettoyé des champs inutiles
        String query = "INSERT INTO coach (nom, prenom, adresse, specialite, cv, valide, mail, mot_de_passe) " +
                      "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, coach.getNom());
            pstmt.setString(2, coach.getPrenom());
            pstmt.setString(3, coach.getAdresse());
            pstmt.setString(4, coach.getSpecialite());
            pstmt.setString(5, coach.getCv());
            pstmt.setBoolean(6, coach.isValide());
            pstmt.setString(7, coach.getEmail());
            pstmt.setString(8, "motdepasse");
            
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
    public boolean update(Coach coach) throws SQLException {
        // SQL nettoyé des champs inutiles
        String query = "UPDATE coach SET nom = ?, prenom = ?, adresse = ?, " +
                      "specialite = ?, cv = ?, mail = ? WHERE id_coach = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, coach.getNom());
            pstmt.setString(2, coach.getPrenom());
            pstmt.setString(3, coach.getAdresse());
            pstmt.setString(4, coach.getSpecialite());
            pstmt.setString(5, coach.getCv());
            pstmt.setString(6, coach.getEmail());
            pstmt.setInt(7, coach.getIdCoach());
            return pstmt.executeUpdate() > 0;
        }
    }
    
    @Override
    public boolean delete(int id) throws SQLException {
        String query = "DELETE FROM coach WHERE id_coach = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        }
    }
    
    @Override
    public boolean valider(int idCoach) throws SQLException {
        String query = "UPDATE coach SET valide = 1, date_validation = NOW() WHERE id_coach = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, idCoach);
            return pstmt.executeUpdate() > 0;
        }
    }
    
    @Override
    public boolean invalider(int idCoach) throws SQLException {
        String query = "UPDATE coach SET valide = 0, date_validation = NULL WHERE id_coach = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, idCoach);
            return pstmt.executeUpdate() > 0;
        }
    }
    
    @Override
    public List<Coach> getEnAttenteValidation() throws SQLException {
        List<Coach> coachs = new ArrayList<>();
        String query = "SELECT *, 0 AS nombre_clients FROM coach WHERE valide = 0 ORDER BY nom, prenom";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) coachs.add(mapResultSetToCoach(rs));
        }
        return coachs;
    }
    
    @Override
    public List<Coach> getValides() throws SQLException {
        List<Coach> coachs = new ArrayList<>();
        String query = "SELECT *, 0 AS nombre_clients FROM coach WHERE valide = 1 ORDER BY nom, prenom";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) coachs.add(mapResultSetToCoach(rs));
        }
        return coachs;
    }
    
    private Coach mapResultSetToCoach(ResultSet rs) throws SQLException {
        Coach coach = new Coach();
        coach.setIdCoach(rs.getInt("id_coach"));
        coach.setNom(rs.getString("nom"));
        coach.setPrenom(rs.getString("prenom"));
        coach.setAdresse(rs.getString("adresse"));
        coach.setSpecialite(rs.getString("specialite"));
        coach.setCv(rs.getString("cv"));
        coach.setTelephone(rs.getString("telephone"));
        coach.setValide(rs.getBoolean("valide"));
        coach.setDateValidation(rs.getTimestamp("date_validation"));
        coach.setEmail(rs.getString("mail")); 
        coach.setNombreClients(rs.getInt("nombre_clients"));
        return coach;
    }
}