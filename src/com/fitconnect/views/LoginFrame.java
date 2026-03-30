package com.fitconnect.views;

import com.fitconnect.models.Utilisateur;
import com.fitconnect.services.AuthService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;

/**
 * Fenêtre de connexion pour les administrateurs
 */
public class LoginFrame extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private AuthService authService;
    
    public LoginFrame() {
        setTitle("FitConnect - Administration");
        setSize(450, 400); // Augmenté légèrement pour laisser respirer le bouton
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        try {
            authService = new AuthService();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Erreur de connexion à la base de données:\n" + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
        
        initComponents();
    }
    
    private void initComponents() {
        // Panel principal avec gradient
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                GradientPaint gradient = new GradientPaint(
                        0, 0, new Color(254, 112, 0),
                        0, getHeight(), new Color(255, 153, 51)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new GridBagLayout());
        
        // Panel de connexion
        JPanel loginPanel = new JPanel();
        loginPanel.setBackground(Color.WHITE);
        loginPanel.setLayout(new GridBagLayout());
        loginPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
                BorderFactory.createEmptyBorder(30, 40, 30, 40)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Titre
        JLabel titleLabel = new JLabel("Administration FitConnect");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(new Color(254, 112, 0));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 25, 0);
        gbc.anchor = GridBagConstraints.CENTER;
        loginPanel.add(titleLabel, gbc);
        
        // Email
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.insets = new Insets(5, 0, 5, 10);
        JLabel emailLabel = new JLabel("Email :");
        emailLabel.setFont(new Font("Arial", Font.BOLD, 13));
        loginPanel.add(emailLabel, gbc);
        
        gbc.gridx = 1;
        emailField = new JTextField(20);
        emailField.setFont(new Font("Arial", Font.PLAIN, 14));
        emailField.setPreferredSize(new Dimension(200, 30));
        loginPanel.add(emailField, gbc);
        
        // Mot de passe
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel passwordLabel = new JLabel("Mot de passe :");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 13));
        loginPanel.add(passwordLabel, gbc);
        
        gbc.gridx = 1;
        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.setPreferredSize(new Dimension(200, 30));
        loginPanel.add(passwordField, gbc);
        
        // --- BOUTON DE CONNEXION (CORRIGÉ) ---
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(25, 0, 0, 0);
        
        loginButton = new JButton("Se connecter");
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        
        // Ces trois lignes sont CRUCIALES pour que le bouton soit visible et orange
        loginButton.setBackground(new Color(254, 112, 0));
        loginButton.setForeground(Color.WHITE);
        loginButton.setOpaque(true); 
        loginButton.setBorderPainted(false);
        
        loginButton.setFocusPainted(false);
        loginButton.setPreferredSize(new Dimension(200, 40));
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        loginButton.addActionListener(this::handleLogin);
        passwordField.addActionListener(this::handleLogin);
        
        loginPanel.add(loginButton, gbc);
        
        mainPanel.add(loginPanel);
        add(mainPanel);
    }
    
    private void handleLogin(ActionEvent e) {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());
        
        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Veuillez remplir tous les champs",
                    "Attention",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            Utilisateur user = authService.login(email, password);
            
            if (user != null) {
                dispose();
                SwingUtilities.invokeLater(() -> {
                    MainAdminFrame mainFrame = new MainAdminFrame(user);
                    mainFrame.setVisible(true);
                });
            } else {
                JOptionPane.showMessageDialog(this,
                        "Email ou mot de passe incorrect",
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
                passwordField.setText("");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erreur SQL: " + ex.getMessage());
        }
    }
    
    public static void main(String[] args) {
        // Optionnel : On peut forcer un Look and Feel plus moderne (Nimbus ou Metal)
        // si le SystemLookAndFeel pose encore des soucis de visibilité sur ton OS.
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            LoginFrame frame = new LoginFrame();
            frame.setVisible(true);
        });
    }
}