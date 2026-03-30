package com.fitconnect;

import org.mindrot.jbcrypt.BCrypt;

public class GenerateHash {
    public static void main(String[] args) {
        // Générer des hash pour différents mots de passe
        String[] passwords = {"password", "admin123", "admin", "test"};
        
        System.out.println("=== GÉNÉRATION DE HASH BCRYPT ===\n");
        
        for (String pwd : passwords) {
            String hash = BCrypt.hashpw(pwd, BCrypt.gensalt(10));
            System.out.println("Mot de passe : " + pwd);
            System.out.println("Hash        : " + hash);
            System.out.println();
        }
        
        // Test de vérification
        String testHash = BCrypt.hashpw("admin123", BCrypt.gensalt(10));
        boolean check = BCrypt.checkpw("admin123", testHash);
        System.out.println("Test de vérification : " + check);
    }
}
