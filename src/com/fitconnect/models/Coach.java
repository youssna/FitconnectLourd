package com.fitconnect.models;

import java.sql.Timestamp;


public class Coach {
    private int idCoach;
    private int idUtilisateur;
    private String nom;
    private String prenom;
    private String adresse;
    private String telephone;
    private String specialite;
    private String cv;
    private String certification;
    private int anneesExperience;
    private boolean valide;
    private Timestamp dateValidation;
    private String email;
    private int nombreClients;
    public Coach() {}
    
    public Coach(String nom, String prenom, String adresse, String specialite, String cv) {
        this.nom = nom;
        this.prenom = prenom;
        this.adresse = adresse;
        this.specialite = specialite;
        this.cv = cv;
        this.valide = false;
    }
   
    public int getIdCoach() {
        return idCoach;
    }
    
    public void setIdCoach(int idCoach) {
        this.idCoach = idCoach;
    }
    
    public int getIdUtilisateur() {
        return idUtilisateur;
    }
    
    public void setIdUtilisateur(int idUtilisateur) {
        this.idUtilisateur = idUtilisateur;
    }
    
    public String getNom() {
        return nom;
    }
    
    public void setNom(String nom) {
        this.nom = nom;
    }
    
    public String getPrenom() {
        return prenom;
    }
    
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }
    
    public String getAdresse() {
        return adresse;
    }
    
    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }
    
    public String getTelephone() {
        return telephone;
    }
    
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
    
    public String getSpecialite() {
        return specialite;
    }
    
    public void setSpecialite(String specialite) {
        this.specialite = specialite;
    }
    
    public String getCv() {
        return cv;
    }
    
    public void setCv(String cv) {
        this.cv = cv;
    }
    
    public String getCertification() {
        return certification;
    }
    
    public void setCertification(String certification) {
        this.certification = certification;
    }
    
    public int getAnneesExperience() {
        return anneesExperience;
    }
    
    public void setAnneesExperience(int anneesExperience) {
        this.anneesExperience = anneesExperience;
    }
    
    public boolean isValide() {
        return valide;
    }
    
    public void setValide(boolean valide) {
        this.valide = valide;
    }
    
    public Timestamp getDateValidation() {
        return dateValidation;
    }
    
    public void setDateValidation(Timestamp dateValidation) {
        this.dateValidation = dateValidation;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public int getNombreClients() {
        return nombreClients;
    }
    
    public void setNombreClients(int nombreClients) {
        this.nombreClients = nombreClients;
    }
    
    public String getNomComplet() {
        return prenom + " " + nom;
    }
    
    public String getStatutValidation() {
        return valide ? "Validé" : "En attente";
    }
    
    @Override
    public String toString() {
        return "Coach{" +
                "id=" + idCoach +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", specialite='" + specialite + '\'' +
                ", valide=" + valide +
                '}';
    }
}
