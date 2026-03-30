package com.fitconnect.views.panels;

import com.fitconnect.services.StatistiquesService;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.Map;

/**
 * Panel du tableau de bord affichant les statistiques principales
 */
public class DashboardPanel extends JPanel {
    private StatistiquesService statsService;
    
    // Labels pour les statistiques
    private JLabel lblNombreClients;
    private JLabel lblNombreCoaches;
    private JLabel lblCoachesValides;
    private JLabel lblCoachesEnAttente;
    private JLabel lblNombreProgrammes;
    
    public DashboardPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        
        try {
            statsService = new StatistiquesService();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Erreur d'initialisation: " + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
        }
        
        initComponents();
        refreshData();
    }
    
    private void initComponents() {
        // En-tête
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));
        
        JLabel titleLabel = new JLabel("Tableau de Bord");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(45, 52, 54));
        headerPanel.add(titleLabel);
        
        add(headerPanel, BorderLayout.NORTH);
        
        // Panel principal avec les statistiques
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        
        // Première ligne
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(createStatCard("Clients", "0", new Color(52, 152, 219)), gbc);
        
        gbc.gridx = 1;
        mainPanel.add(createStatCard("Coachs", "0", new Color(155, 89, 182)), gbc);
        
        gbc.gridx = 2;
        mainPanel.add(createStatCard("Validés", "0", new Color(46, 204, 113)), gbc);
        
        // Deuxième ligne
        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(createStatCard("En attente", "0", new Color(230, 126, 34)), gbc);
        
        gbc.gridx = 1;
        mainPanel.add(createStatCard("Programmes", "0", new Color(231, 76, 60)), gbc);
        
        add(mainPanel, BorderLayout.CENTER);
    }
    
    private JPanel createStatCard(String title, String value, Color color) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        // Barre colorée en haut
        JPanel topBar = new JPanel();
        topBar.setBackground(color);
        topBar.setPreferredSize(new Dimension(0, 5));
        card.add(topBar, BorderLayout.NORTH);
        
        // Contenu
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        titleLabel.setForeground(new Color(100, 100, 100));
        contentPanel.add(titleLabel, BorderLayout.NORTH);
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 36));
        valueLabel.setForeground(new Color(45, 52, 54));
        valueLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        contentPanel.add(valueLabel, BorderLayout.CENTER);
        
        // Stocker les labels pour mise à jour
        if (title.contains("Clients")) {
            lblNombreClients = valueLabel;
        } else if (title.contains("Coachs") && !title.contains("Validés") && !title.contains("attente")) {
            lblNombreCoaches = valueLabel;
        } else if (title.contains("Validés")) {
            lblCoachesValides = valueLabel;
        } else if (title.contains("attente")) {
            lblCoachesEnAttente = valueLabel;
        } else if (title.contains("Programmes")) {
            lblNombreProgrammes = valueLabel;
        }
        
        card.add(contentPanel, BorderLayout.CENTER);
        
        return card;
    }
    
    public void refreshData() {
        if (statsService == null) return;
        
        try {
            Map<String, Object> stats = statsService.getStatistiquesGlobales();
            
            lblNombreClients.setText(String.valueOf(stats.get("nombreClients")));
            lblNombreCoaches.setText(String.valueOf(stats.get("nombreCoaches")));
            lblCoachesValides.setText(String.valueOf(stats.get("nombreCoachesValides")));
            lblCoachesEnAttente.setText(String.valueOf(stats.get("nombreCoachesEnAttente")));
            lblNombreProgrammes.setText(String.valueOf(stats.get("nombreProgrammes")));
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Erreur lors du chargement des statistiques: " + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
