package com.facturation.repository;

import com.facturation.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository pour les opérations de base de données sur les clients
 * Fournit les méthodes CRUD de base et des requêtes personnalisées
 */
@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    /**
     * Trouve un client par son email
     * @param email l'email du client
     * @return le client trouvé ou empty si aucun client avec cet email
     */
    Optional<Client> findByEmail(String email);

    /**
     * Trouve un client par son SIRET
     * @param siret le SIRET du client
     * @return le client trouvé ou empty si aucun client avec ce SIRET
     */
    Optional<Client> findBySiret(String siret);

    /**
     * Vérifie si un client existe avec l'email donné
     * @param email l'email à vérifier
     * @return true si un client existe avec cet email, false sinon
     */
    boolean existsByEmail(String email);

    /**
     * Vérifie si un client existe avec le SIRET donné
     * @param siret le SIRET à vérifier
     * @return true si un client existe avec ce SIRET, false sinon
     */
    boolean existsBySiret(String siret);
} 