package com.fitconnect.dao;

import com.fitconnect.models.Client;
import java.sql.SQLException;
import java.util.List;

/**
 * Interface définissant les opérations CRUD pour les clients
 */
public interface IClientDAO {
    
    /**
     * Récupérer tous les clients
     * @return Liste de tous les clients
     * @throws SQLException
     */
    List<Client> getAll() throws SQLException;
    
    /**
     * Récupérer un client par son ID
     * @param id ID du client
     * @return Client trouvé ou null
     * @throws SQLException
     */
    Client getById(int id) throws SQLException;
    
    /**
     * Créer un nouveau client
     * @param client Client à créer
     * @return ID du client créé
     * @throws SQLException
     */
    int create(Client client) throws SQLException;
    
    /**
     * Mettre à jour un client
     * @param client Client à mettre à jour
     * @return true si la mise à jour a réussi
     * @throws SQLException
     */
    boolean update(Client client) throws SQLException;
    
    /**
     * Supprimer un client
     * @param id ID du client à supprimer
     * @return true si la suppression a réussi
     * @throws SQLException
     */
    boolean delete(int id) throws SQLException;
    
    /**
     * Récupérer les clients d'un coach spécifique
     * @param idCoach ID du coach
     * @return Liste des clients du coach
     * @throws SQLException
     */
    List<Client> getByCoach(int idCoach) throws SQLException;
    
    /**
     * Assigner un client à un coach
     * @param idClient ID du client
     * @param idCoach ID du coach
     * @return true si l'assignation a réussi
     * @throws SQLException
     */
    boolean assignerCoach(int idClient, int idCoach) throws SQLException;
    
    /**
     * Rechercher des clients par nom/prénom
     * @param recherche Terme de recherche
     * @return Liste des clients correspondants
     * @throws SQLException
     */
    List<Client> rechercher(String recherche) throws SQLException;
}
