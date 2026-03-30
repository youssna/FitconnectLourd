package com.fitconnect.views.panels;

import com.fitconnect.dao.CoachDAO;
import com.fitconnect.models.Coach;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class GestionCoachsPanel extends JPanel {
    private CoachDAO coachDAO;
    private JTable tableCoachs;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    
    public GestionCoachsPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        
        try {
            coachDAO = new CoachDAO();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Erreur d'initialisation: " + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
        }
        
        initComponents();
    }
    
    private void initComponents() {
        JPanel headerPanel = new JPanel(new BorderLayout(20, 20));
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));
        
        JPanel toolbarPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        toolbarPanel.setBackground(Color.WHITE);
        
        JLabel searchLabel = new JLabel("Recherche :");
        searchLabel.setFont(new Font("Arial", Font.BOLD, 13));
        toolbarPanel.add(searchLabel);
        
        searchField = new JTextField(15);
        searchField.setFont(new Font("Arial", Font.PLAIN, 14));
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        
        JButton searchButton = createButton("Rechercher", new Color(52, 152, 219));
        searchButton.addActionListener(e -> rechercherCoachs());

        JButton acceptButton = createButton("Accepter", new Color(46, 204, 113));
        acceptButton.addActionListener(e -> accepterCoach());
        
        JButton refreshButton = createButton("Actualiser", new Color(149, 165, 166));
        refreshButton.addActionListener(e -> refreshData());
        
        JButton deleteButton = createButton("Supprimer", new Color(231, 76, 60));
        deleteButton.addActionListener(e -> supprimerCoach());
        
        toolbarPanel.add(searchField);
        toolbarPanel.add(searchButton);
        toolbarPanel.add(acceptButton);
        toolbarPanel.add(refreshButton);
        toolbarPanel.add(deleteButton);
        
        headerPanel.add(toolbarPanel, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);
        
        // AJOUT DE LA COLONNE "Téléphone"
        String[] columnNames = {"ID", "Nom", "Prénom", "Email", "Téléphone", "Spécialité", "Statut"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tableCoachs = new JTable(tableModel);
        tableCoachs.setFont(new Font("Arial", Font.PLAIN, 13));
        tableCoachs.setRowHeight(35);
        tableCoachs.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        tableCoachs.getTableHeader().setBackground(new Color(254, 112, 0));
        tableCoachs.getTableHeader().setForeground(Color.WHITE);
        tableCoachs.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // RÉGLAGE DE LA LARGEUR DES COLONNES
        tableCoachs.getColumnModel().getColumn(0).setPreferredWidth(40);  // ID
        tableCoachs.getColumnModel().getColumn(3).setPreferredWidth(180); // Email
        tableCoachs.getColumnModel().getColumn(4).setPreferredWidth(120); // Téléphone
        
        JScrollPane scrollPane = new JScrollPane(tableCoachs);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 30, 30, 30));
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        add(scrollPane, BorderLayout.CENTER);
        refreshData();
    }

    private void accepterCoach() {
        int selectedRow = tableCoachs.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un coach.", "Attention", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int idCoach = (int) tableModel.getValueAt(selectedRow, 0);
        String statutActuel = (String) tableModel.getValueAt(selectedRow, 6); // Index 6 car on a ajouté Téléphone

        if (statutActuel.equals("Validé")) {
            JOptionPane.showMessageDialog(this, "Ce coach est déjà validé.");
            return;
        }

        int confirmation = JOptionPane.showConfirmDialog(this, "Autoriser ce coach ?", "Validation", JOptionPane.YES_NO_OPTION);
        if (confirmation == JOptionPane.YES_OPTION) {
            try {
                if (coachDAO.valider(idCoach)) {
                    JOptionPane.showMessageDialog(this, "Coach accepté !");
                    refreshData();
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Erreur validation: " + e.getMessage());
            }
        }
    }
    
    private JButton createButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true); 
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(110, 35));
        return button;
    }
    
    public void refreshData() {
        if (coachDAO == null) return;
        tableModel.setRowCount(0);
        try {
            List<Coach> coachs = coachDAO.getAll();
            for (Coach coach : coachs) {
                Object[] row = {
                    coach.getIdCoach(),
                    coach.getNom(),
                    coach.getPrenom(),
                    coach.getEmail(),
                    coach.getTelephone(), // Donnée ajoutée
                    coach.getSpecialite(),
                    coach.isValide() ? "Validé" : "En attente"
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur chargement: " + e.getMessage());
        }
    }

    private void rechercherCoachs() {
        String recherche = searchField.getText().trim().toLowerCase();
        if (recherche.isEmpty()) { refreshData(); return; }
        tableModel.setRowCount(0);
        try {
            List<Coach> coachs = coachDAO.getAll().stream()
                .filter(c -> c.getNom().toLowerCase().contains(recherche) || 
                             c.getPrenom().toLowerCase().contains(recherche) ||
                             (c.getEmail() != null && c.getEmail().toLowerCase().contains(recherche)) ||
                             (c.getTelephone() != null && c.getTelephone().contains(recherche)))
                .collect(Collectors.toList());
            for (Coach coach : coachs) {
                Object[] row = { 
                    coach.getIdCoach(), 
                    coach.getNom(), 
                    coach.getPrenom(), 
                    coach.getEmail(), 
                    coach.getTelephone(), 
                    coach.getSpecialite(), 
                    coach.isValide() ? "Validé" : "En attente" 
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur recherche: " + e.getMessage());
        }
    }

    private void supprimerCoach() {
        int selectedRow = tableCoachs.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Sélectionnez un coach.");
            return;
        }
        int idCoach = (int) tableModel.getValueAt(selectedRow, 0);
        if (JOptionPane.showConfirmDialog(this, "Supprimer ?", "Confirmation", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            try {
                if (coachDAO.delete(idCoach)) {
                    refreshData();
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Erreur: " + e.getMessage());
            }
        }
    }
}