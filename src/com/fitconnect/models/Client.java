package com.fitconnect.models;

import java.sql.Timestamp;

public class Client {
 
    private int idClient;           
    private String nom;             
    private String prenom;         
    private double poids;           
    private int taille;            
    private String objectif;        
    private String motivation;      
    private Integer idCoach;        
    private Timestamp dateInscription;
    
  
    private String email;          
    private String telephone;      
    private String motDePasse;     
    private String nomCoach;        
    
    public Client() {}
    
    public Client(String nom, String prenom, double poids, int taille, String objectif, String motivation, String email, String telephone) {
        this.nom = nom;
        this.prenom = prenom;
        this.poids = poids;
        this.taille = taille;
        this.objectif = objectif;
        this.motivation = motivation;
        this.email = email;
        this.telephone = telephone;
    }
    
    public int getIdClient() {
        return idClient;
    }
    
    public void setIdClient(int idClient) {
        this.idClient = idClient;
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
    
    public double getPoids() {
        return poids;
    }
    
    public void setPoids(double poids) {
        this.poids = poids;
    }
    
    public int getTaille() {
        return taille;
    }
    
    public void setTaille(int taille) {
        this.taille = taille;
    }
    
    public String getObjectif() {
        return objectif;
    }
    
    public void setObjectif(String objectif) {
        this.objectif = objectif;
    }
    
    public String getMotivation() {
        return motivation;
    }
    
    public void setMotivation(String motivation) {
        this.motivation = motivation;
    }
    
    public Integer getIdCoach() {
        return idCoach;
    }
    
    public void setIdCoach(Integer idCoach) {
        this.idCoach = idCoach;
    }
    
    
    public Timestamp getDateInscription() {
        return dateInscription;
    }
    
    public void setDateInscription(Timestamp dateInscription) {
        this.dateInscription = dateInscription;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getTelephone() {
        return telephone;
    }
    
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }
    
    public String getNomCoach() {
        return nomCoach;
    }
    
    public void setNomCoach(String nomCoach) {
        this.nomCoach = nomCoach;
    }
    

    public String getNomComplet() {
        return prenom + " " + nom;
    }
    
    @Override
    public String toString() {
        return "Client{" +
                "id=" + idClient +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", email='" + email + '\'' +
                ", telephone='" + telephone + '\'' +
                ", objectif='" + objectif + '\'' +
                '}';
    }
}