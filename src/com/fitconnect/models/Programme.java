package com.fitconnect.models;

import java.sql.Timestamp;


public class Programme {
    private int idProgramme;
    private String nom;
    private String description;
    private String type;
    private String niveau;
    private int dureeSemaines;
    private int frequenceParSemaine;
    private Integer idCoachCreateur;
    private Timestamp dateCreation;
    
    private String nomCoach;
    private int nombreClientsAssignes;
    
    public Programme() {}
    
    public Programme(String nom, String description, String type, String niveau) {
        this.nom = nom;
        this.description = description;
        this.type = type;
        this.niveau = niveau;
    }
    
    public int getIdProgramme() {
        return idProgramme;
    }
    
    public void setIdProgramme(int idProgramme) {
        this.idProgramme = idProgramme;
    }
    
    public String getNom() {
        return nom;
    }
    
    public void setNom(String nom) {
        this.nom = nom;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public String getNiveau() {
        return niveau;
    }
    
    public void setNiveau(String niveau) {
        this.niveau = niveau;
    }
    
    public int getDureeSemaines() {
        return dureeSemaines;
    }
    
    public void setDureeSemaines(int dureeSemaines) {
        this.dureeSemaines = dureeSemaines;
    }
    
    public int getFrequenceParSemaine() {
        return frequenceParSemaine;
    }
    
    public void setFrequenceParSemaine(int frequenceParSemaine) {
        this.frequenceParSemaine = frequenceParSemaine;
    }
    
    public Integer getIdCoachCreateur() {
        return idCoachCreateur;
    }
    
    public void setIdCoachCreateur(Integer idCoachCreateur) {
        this.idCoachCreateur = idCoachCreateur;
    }
    
    public Timestamp getDateCreation() {
        return dateCreation;
    }
    
    public void setDateCreation(Timestamp dateCreation) {
        this.dateCreation = dateCreation;
    }
    
    public String getNomCoach() {
        return nomCoach;
    }
    
    public void setNomCoach(String nomCoach) {
        this.nomCoach = nomCoach;
    }
    
    public int getNombreClientsAssignes() {
        return nombreClientsAssignes;
    }
    
    public void setNombreClientsAssignes(int nombreClientsAssignes) {
        this.nombreClientsAssignes = nombreClientsAssignes;
    }
    
    @Override
    public String toString() {
        return nom + " (" + type + " - " + niveau + ")";
    }
}
