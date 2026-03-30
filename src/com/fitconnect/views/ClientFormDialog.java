package com.fitconnect.views;

import com.fitconnect.dao.ClientDAO;
import com.fitconnect.models.Client;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class ClientFormDialog extends JDialog {
    private JTextField txtNom, txtPrenom, txtEmail, txtTelephone, txtPoids, txtTaille, txtMotivation;
    private JPasswordField txtMotDePasse; // Le champ pour le mot de passe
    private JComboBox<String> comboObjectif;
    private Client clientToEdit;
    private ClientDAO clientDAO;
    private Runnable onSuccess;

    public ClientFormDialog(Window owner, Client clientToEdit, ClientDAO clientDAO, Runnable onSuccess) {
        super(owner, clientToEdit == null ? "Ajouter un Client" : "Modifier le Client", ModalityType.APPLICATION_MODAL);
        this.clientToEdit = clientToEdit;
        this.clientDAO = clientDAO;
        this.onSuccess = onSuccess;

        initComponents();
        if (clientToEdit != null) {
            remplirChamps();
        }

        // On agrandit un peu la fenêtre si on est en mode "Ajout" pour faire de la place au mot de passe
        setSize(400, clientToEdit == null ? 550 : 500);
        setLocationRelativeTo(owner);
    }

    private void initComponents() {
        boolean isAjout = (clientToEdit == null);
        int rows = isAjout ? 9 : 8; // 9 lignes si c'est un ajout (pour le MDP), sinon 8
        
        JPanel formPanel = new JPanel(new GridLayout(rows, 2, 10, 15)); 
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        formPanel.add(new JLabel("Nom :"));
        txtNom = new JTextField();
        formPanel.add(txtNom);

        formPanel.add(new JLabel("Prénom :"));
        txtPrenom = new JTextField();
        formPanel.add(txtPrenom);

        formPanel.add(new JLabel("Email :"));
        txtEmail = new JTextField();
        formPanel.add(txtEmail);

        formPanel.add(new JLabel("Téléphone :"));
        txtTelephone = new JTextField();
        formPanel.add(txtTelephone);

        // LE MOT DE PASSE : Uniquement à la création
        if (isAjout) {
            formPanel.add(new JLabel("Mot de passe :"));
            txtMotDePasse = new JPasswordField();
            formPanel.add(txtMotDePasse);
        }

        formPanel.add(new JLabel("Poids (kg) :"));
        txtPoids = new JTextField();
        formPanel.add(txtPoids);

        formPanel.add(new JLabel("Taille (cm) :"));
        txtTaille = new JTextField();
        formPanel.add(txtTaille);

        formPanel.add(new JLabel("Objectif :"));
        comboObjectif = new JComboBox<>(new String[]{"prise_masse", "perte_poids", "remise_forme"});
        formPanel.add(comboObjectif);

        formPanel.add(new JLabel("Motivation :"));
        txtMotivation = new JTextField();
        formPanel.add(txtMotivation);

        add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSave = new JButton("Enregistrer");
        btnSave.setBackground(new Color(46, 204, 113));
        btnSave.setForeground(Color.WHITE);
        btnSave.setOpaque(true);
        btnSave.setBorderPainted(false);
        
        JButton btnCancel = new JButton("Annuler");
        
        btnSave.addActionListener(e -> sauvegarder());
        btnCancel.addActionListener(e -> dispose());

        buttonPanel.add(btnCancel);
        buttonPanel.add(btnSave);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void remplirChamps() {
        txtNom.setText(clientToEdit.getNom());
        txtPrenom.setText(clientToEdit.getPrenom());
        txtEmail.setText(clientToEdit.getEmail());
        txtTelephone.setText(clientToEdit.getTelephone() != null ? clientToEdit.getTelephone() : "");
        txtPoids.setText(String.valueOf(clientToEdit.getPoids()));
        txtTaille.setText(String.valueOf(clientToEdit.getTaille()));
        comboObjectif.setSelectedItem(clientToEdit.getObjectif());
        txtMotivation.setText(clientToEdit.getMotivation());
    }

    private void sauvegarder() {
        // Contrôle de saisie de base
        if (txtNom.getText().trim().isEmpty() || txtPrenom.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Le nom et le prénom sont obligatoires.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Vérification que le mot de passe n'est pas vide pour un NOUVEAU client
        if (clientToEdit == null && txtMotDePasse.getPassword().length == 0) {
            JOptionPane.showMessageDialog(this, "Le mot de passe est obligatoire pour un nouveau client.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Client client = (clientToEdit == null) ? new Client() : clientToEdit;
            
            client.setNom(txtNom.getText().trim());
            client.setPrenom(txtPrenom.getText().trim());
            client.setEmail(txtEmail.getText().trim());
            client.setTelephone(txtTelephone.getText().trim());
            client.setPoids(Double.parseDouble(txtPoids.getText().trim()));
            client.setTaille(Integer.parseInt(txtTaille.getText().trim()));
            client.setObjectif((String) comboObjectif.getSelectedItem());
            client.setMotivation(txtMotivation.getText().trim());

            // Si c'est un ajout, on récupère le mot de passe tapé
            if (clientToEdit == null) {
                client.setMotDePasse(new String(txtMotDePasse.getPassword()));
            }

            boolean success;
            if (clientToEdit == null) {
                client.setIdUtilisateur(0); 
                success = clientDAO.create(client) > 0;
            } else {
                success = clientDAO.update(client);
            }

            if (success) {
                JOptionPane.showMessageDialog(this, "Opération réussie !", "Succès", JOptionPane.INFORMATION_MESSAGE);
                if (onSuccess != null) onSuccess.run();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Échec de l'opération en base de données.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Veuillez entrer des nombres valides pour le poids et la taille.", "Erreur", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erreur SQL : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}