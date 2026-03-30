package com.fitconnect.views;

import com.fitconnect.models.Utilisateur;
import com.fitconnect.views.panels.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Fenêtre principale de l'application d'administration
 */
public class MainAdminFrame extends JFrame {
    private Utilisateur currentUser;
    private JPanel contentPanel;
    private CardLayout cardLayout;
    
    // Panels
    private DashboardPanel dashboardPanel;
    private GestionClientsPanel gestionClientsPanel;
    private GestionCoachsPanel gestionCoachsPanel;
    
    public MainAdminFrame(Utilisateur user) {
        this.currentUser = user;
        
        setTitle("FitConnect - Administration");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                handleExit();
            }
        });
        
        initComponents();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        
        // Barre de menu latérale
        JPanel sidebarPanel = createSidebar();
        add(sidebarPanel, BorderLayout.WEST);
        
        // Panel de contenu principal avec CardLayout
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(Color.WHITE);
        
        // Initialiser les panels
        dashboardPanel = new DashboardPanel();
        gestionClientsPanel = new GestionClientsPanel();
        gestionCoachsPanel = new GestionCoachsPanel();
        
        contentPanel.add(dashboardPanel, "dashboard");
        contentPanel.add(gestionClientsPanel, "clients");
        contentPanel.add(gestionCoachsPanel, "coachs");
        
        add(contentPanel, BorderLayout.CENTER);
        
        // Afficher le dashboard par défaut
        cardLayout.show(contentPanel, "dashboard");
    }
    
    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(45, 52, 54));
        sidebar.setPreferredSize(new Dimension(250, getHeight()));
        
        // En-tête
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(254, 112, 0));
        headerPanel.setMaximumSize(new Dimension(250, 80));
        headerPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        
        JLabel titleLabel = new JLabel("FitConnect Admin");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        headerPanel.add(titleLabel);
        
        sidebar.add(headerPanel);
        sidebar.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Info utilisateur
        JPanel userPanel = new JPanel();
        userPanel.setBackground(new Color(45, 52, 54));
        userPanel.setMaximumSize(new Dimension(250, 60));
        userPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 10));
        
        JLabel userLabel = new JLabel("👤 " + currentUser.getEmail());
        userLabel.setForeground(Color.WHITE);
        userLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        userPanel.add(userLabel);
        
        sidebar.add(userPanel);
        sidebar.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Boutons de menu
        JButton dashboardBtn = createMenuButton("Tableau de bord", "dashboard");
        JButton clientsBtn = createMenuButton("Gestion Clients", "clients");
        JButton coachsBtn = createMenuButton("Gestion Coachs", "coachs");
        JButton logoutBtn = createMenuButton("Déconnexion", "logout");
        
        sidebar.add(dashboardBtn);
        sidebar.add(Box.createRigidArea(new Dimension(0, 5)));
        sidebar.add(clientsBtn);
        sidebar.add(Box.createRigidArea(new Dimension(0, 5)));
        sidebar.add(coachsBtn);
        sidebar.add(Box.createRigidArea(new Dimension(0, 5)));
        
        // Spacer
        sidebar.add(Box.createVerticalGlue());
        
        sidebar.add(logoutBtn);
        sidebar.add(Box.createRigidArea(new Dimension(0, 20)));
        
        return sidebar;
    }
    
    private JButton createMenuButton(String text, String action) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setMaximumSize(new Dimension(250, 45));
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(45, 52, 54));
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(254, 112, 0));
                button.setOpaque(true);
            }
            
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setOpaque(false);
            }
        });
        
        button.addActionListener(e -> {
            switch (action) {
                case "dashboard":
                    dashboardPanel.refreshData();
                    cardLayout.show(contentPanel, "dashboard");
                    break;
                case "clients":
                    gestionClientsPanel.refreshData();
                    cardLayout.show(contentPanel, "clients");
                    break;
                case "coachs":
                    gestionCoachsPanel.refreshData();
                    cardLayout.show(contentPanel, "coachs");
                    break;
                case "logout":
                    handleLogout();
                    break;
            }
        });
        
        return button;
    }
    
    private void handleLogout() {
        int choice = JOptionPane.showConfirmDialog(this,
                "Êtes-vous sûr de vouloir vous déconnecter ?",
                "Déconnexion",
                JOptionPane.YES_NO_OPTION);
        
        if (choice == JOptionPane.YES_OPTION) {
            dispose();
            SwingUtilities.invokeLater(() -> {
                LoginFrame loginFrame = new LoginFrame();
                loginFrame.setVisible(true);
            });
        }
    }
    
    private void handleExit() {
        int choice = JOptionPane.showConfirmDialog(this,
                "Êtes-vous sûr de vouloir quitter l'application ?",
                "Quitter",
                JOptionPane.YES_NO_OPTION);
        
        if (choice == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
}
