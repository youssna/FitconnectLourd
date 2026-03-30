package com.fitconnect.dao;

import com.fitconnect.models.Coach;
import java.sql.SQLException;
import java.util.List;

/**
 * Interface définissant les opérations CRUD pour les coachs
 */
public interface ICoachDAO {
    
    /**
     * Récupérer tous les coachs
     * @return Liste de tous les coachs
     * @throws SQLException
     */
    List<Coach> getAll() throws SQLException;
    
    /**
     * Récupérer un coach par son ID
     * @param id ID du coach
     * @return Coach trouvé ou null
     * @throws SQLException
     */
    Coach getById(int id) throws SQLException;
    
    /**
     * Créer un nouveau coach
     * @param coach Coach à créer
     * @return ID du coach créé
     * @throws SQLException
     */
    int create(Coach coach) throws SQLException;
    
    /**
     * Mettre à jour un coach
     * @param coach Coach à mettre à jour
     * @return true si la mise à jour a réussi
     * @throws SQLException
     */
    boolean update(Coach coach) throws SQLException;
    
    /**
     * Supprimer un coach
     * @param id ID du coach à supprimer
     * @return true si la suppression a réussi
     * @throws SQLException
     */
    boolean delete(int id) throws SQLException;
    
    /**
     * Valider un coach (fonction admin)
     * @param idCoach ID du coach à valider
     * @return true si la validation a réussi
     * @throws SQLException
     */
    boolean valider(int idCoach) throws SQLException;
    
    /**
     * Invalider un coach
     * @param idCoach ID du coach à invalider
     * @return true si l'invalidation a réussi
     * @throws SQLException
     */
    boolean invalider(int idCoach) throws SQLException;
    
    /**
     * Récupérer les coachs en attente de validation
     * @return Liste des coachs non validés
     * @throws SQLException
     */
    List<Coach> getEnAttenteValidation() throws SQLException;
    
    /**
     * Récupérer les coachs validés
     * @return Liste des coachs validés
     * @throws SQLException
     */
    List<Coach> getValides() throws SQLException;
}
