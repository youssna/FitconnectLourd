package com.fitconnect.models;

import java.sql.Timestamp;


public class Utilisateur {
    private int idUtilisateur;
    private String email;
    private String motDePasse;
    private String role; 
    private boolean actif;
    private Timestamp dateCreation;
    private Timestamp dateModification;
    public Utilisateur() {}
    
    public Utilisateur(String email, String motDePasse, String role) {
        this.email = email;
        this.motDePasse = motDePasse;
        this.role = role;
        this.actif = true;
    }
    
    public int getIdUtilisateur() {
        return idUtilisateur;
    }
    
    public void setIdUtilisateur(int idUtilisateur) {
        this.idUtilisateur = idUtilisateur;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getMotDePasse() {
        return motDePasse;
    }
    
    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }
    
    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
    
    public boolean isActif() {
        return actif;
    }
    
    public void setActif(boolean actif) {
        this.actif = actif;
    }
    
    public Timestamp getDateCreation() {
        return dateCreation;
    }
    
    public void setDateCreation(Timestamp dateCreation) {
        this.dateCreation = dateCreation;
    }
    
    public Timestamp getDateModification() {
        return dateModification;
    }
    
    public void setDateModification(Timestamp dateModification) {
        this.dateModification = dateModification;
    }
    
    @Override
    public String toString() {
        return "Utilisateur{" +
                "id=" + idUtilisateur +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                ", actif=" + actif +
                '}';
    }
}
