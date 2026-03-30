package com.fitconnect.views.panels;

import com.fitconnect.dao.ClientDAO;
import com.fitconnect.models.Client;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class GestionClientsPanel extends JPanel {
    private ClientDAO clientDAO;
    private JTable tableClients;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    
    public GestionClientsPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        
        try {
            clientDAO = new ClientDAO();
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
        searchButton.addActionListener(e -> rechercherClients());

        /* --- CODE DÉSACTIVÉ : BOUTONS AJOUTER ET MODIFIER ---
        JButton addButton = createButton("Ajouter", new Color(52, 152, 219));
        addButton.addActionListener(e -> {
            Window window = SwingUtilities.getWindowAncestor(this);
            new com.fitconnect.views.ClientFormDialog(window, null, clientDAO, this::refreshData).setVisible(true);
        });

        JButton editButton = createButton("Modifier", new Color(241, 196, 15));
        editButton.addActionListener(e -> modifierClient());
        ---------------------------------------------------- */
        
        JButton refreshButton = createButton("Actualiser", new Color(46, 204, 113));
        refreshButton.addActionListener(e -> refreshData());
        
        JButton deleteButton = createButton("Supprimer", new Color(231, 76, 60));
        deleteButton.addActionListener(e -> supprimerClient());
        
        toolbarPanel.add(searchField);
        toolbarPanel.add(searchButton);
        
        /* --- CODE DÉSACTIVÉ : AJOUT DES BOUTONS À LA BARRE ---
        toolbarPanel.add(addButton);
        toolbarPanel.add(editButton);
        ---------------------------------------------------- */
        
        toolbarPanel.add(refreshButton);
        toolbarPanel.add(deleteButton);
        
        headerPanel.add(toolbarPanel, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);
        
        String[] columnNames = {"ID", "Nom", "Prénom", "Email", "Téléphone", "Poids (kg)", "Taille (cm)", "Objectif", "Motivation", "Coach"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tableClients = new JTable(tableModel);
        tableClients.setFont(new Font("Arial", Font.PLAIN, 13));
        tableClients.setRowHeight(35);
        tableClients.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        tableClients.getTableHeader().setBackground(new Color(254, 112, 0));
        tableClients.getTableHeader().setForeground(Color.WHITE);
        tableClients.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        tableClients.getColumnModel().getColumn(0).setPreferredWidth(40);  
        tableClients.getColumnModel().getColumn(1).setPreferredWidth(100); 
        tableClients.getColumnModel().getColumn(2).setPreferredWidth(100); 
        tableClients.getColumnModel().getColumn(3).setPreferredWidth(160); 
        tableClients.getColumnModel().getColumn(4).setPreferredWidth(110); 
        tableClients.getColumnModel().getColumn(5).setPreferredWidth(80);  
        tableClients.getColumnModel().getColumn(6).setPreferredWidth(80);  
        tableClients.getColumnModel().getColumn(7).setPreferredWidth(100); 
        tableClients.getColumnModel().getColumn(8).setPreferredWidth(150); 
        tableClients.getColumnModel().getColumn(9).setPreferredWidth(120); 
        
        JScrollPane scrollPane = new JScrollPane(tableClients);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 30, 30, 30));
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        add(scrollPane, BorderLayout.CENTER);
        refreshData();
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
        if (clientDAO == null) return;
        tableModel.setRowCount(0);
        
        try {
            List<Client> clients = clientDAO.getAll();
            for (Client client : clients) {
                Object[] row = {
                    client.getIdClient(),
                    client.getNom(),
                    client.getPrenom(),
                    client.getEmail(),
                    client.getTelephone() != null ? client.getTelephone() : "",
                    String.format("%.1f", client.getPoids()),
                    client.getTaille(),
                    client.getObjectif(),
                    client.getMotivation() != null ? client.getMotivation() : "",
                    client.getNomCoach() != null ? client.getNomCoach() : "Non assigné"
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Erreur lors du chargement des clients: " + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void rechercherClients() {
        String recherche = searchField.getText().trim();
        if (recherche.isEmpty()) {
            refreshData();
            return;
        }
        
        tableModel.setRowCount(0);
        
        try {
            List<Client> clients = clientDAO.rechercher(recherche);
            for (Client client : clients) {
                Object[] row = {
                    client.getIdClient(),
                    client.getNom(),
                    client.getPrenom(),
                    client.getEmail(),
                    client.getTelephone() != null ? client.getTelephone() : "",
                    String.format("%.1f", client.getPoids()),
                    client.getTaille(),
                    client.getObjectif(),
                    client.getMotivation() != null ? client.getMotivation() : "",
                    client.getNomCoach() != null ? client.getNomCoach() : "Non assigné"
                };
                tableModel.addRow(row);
            }
            if (clients.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Aucun client trouvé pour la recherche : " + recherche,
                        "Résultat",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Erreur lors de la recherche: " + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /* --- CODE DÉSACTIVÉ : MÉTHODE MODIFIER CLIENT ---
    private void modifierClient() {
        int selectedRow = tableClients.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Veuillez sélectionner un client à modifier en cliquant sur sa ligne.",
                    "Attention",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            int idClient = (int) tableModel.getValueAt(selectedRow, 0);
            Client clientToEdit = clientDAO.getById(idClient);
            
            if (clientToEdit != null) {
                Window window = SwingUtilities.getWindowAncestor(this);
                new com.fitconnect.views.ClientFormDialog(window, clientToEdit, clientDAO, this::refreshData).setVisible(true);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la récupération du client : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
    ------------------------------------------------- */
    
    private void supprimerClient() {
        int selectedRow = tableClients.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Veuillez sélectionner un client à supprimer en cliquant sur sa ligne.",
                    "Attention",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int idClient = (int) tableModel.getValueAt(selectedRow, 0);
        String nom = (String) tableModel.getValueAt(selectedRow, 1);
        String prenom = (String) tableModel.getValueAt(selectedRow, 2);
        
        int confirmation = JOptionPane.showConfirmDialog(this,
                "Êtes-vous sûr de vouloir supprimer définitivement le client : " + prenom + " " + nom + " ?",
                "Confirmation de suppression",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
        
        if (confirmation == JOptionPane.YES_OPTION) {
            try {
                boolean deleted = clientDAO.delete(idClient);
                if (deleted) {
                    JOptionPane.showMessageDialog(this,
                            "Client supprimé avec succès.",
                            "Succès",
                            JOptionPane.INFORMATION_MESSAGE);
                    refreshData();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Erreur lors de la suppression du client.",
                            "Erreur",
                            JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this,
                        "Erreur SQL lors de la suppression : " + e.getMessage(),
                        "Erreur Fatale",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}