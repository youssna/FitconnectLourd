package com.fitconnect.services;

import com.fitconnect.config.DatabaseConnection;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class StatistiquesService {
    private Connection connection;
    
    public StatistiquesService() throws SQLException {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }
    
    public int getNombreClients() throws SQLException {
        String query = "SELECT COUNT(*) FROM client";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) return rs.getInt(1);
        }
        return 0;
    }
    
    public int getNombreCoaches() throws SQLException {
        String query = "SELECT COUNT(*) FROM coach";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) return rs.getInt(1);
        }
        return 0;
    }
    
    // --- MÉTHODES RÉTABLIES POUR TON DASHBOARD ---

    public int getNombreCoachesValides() throws SQLException {
        String query = "SELECT COUNT(*) FROM coach WHERE valide = 1";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) return rs.getInt(1);
        }
        return 0;
    }
    
    public int getNombreCoachesEnAttente() throws SQLException {
        String query = "SELECT COUNT(*) FROM coach WHERE valide = 0";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) return rs.getInt(1);
        }
        return 0;
    }

    public int getNombreProgrammes() throws SQLException {
        String query = "SELECT COUNT(*) FROM programme";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) return rs.getInt(1);
        }
        return 0;
    }

    public Map<String, Integer> getStatistiquesParObjectif() throws SQLException {
        Map<String, Integer> stats = new HashMap<>();
        String query = "SELECT objectif, COUNT(*) as nombre FROM client GROUP BY objectif";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) stats.put(rs.getString("objectif"), rs.getInt("nombre"));
        }
        return stats;
    }
    
    public Map<String, Object> getStatistiquesGlobales() throws SQLException {
        Map<String, Object> stats = new HashMap<>();
        stats.put("nombreClients", getNombreClients());
        stats.put("nombreCoaches", getNombreCoaches());
        stats.put("nombreCoachesValides", getNombreCoachesValides()); // Retravaille le null
        stats.put("nombreCoachesEnAttente", getNombreCoachesEnAttente()); // Retravaille le null
        stats.put("nombreProgrammes", getNombreProgrammes());
        stats.put("statsParObjectif", getStatistiquesParObjectif());
        return stats;
    }
}