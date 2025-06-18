package com.facturation.repository;

import com.facturation.model.Facture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository pour les opérations de base de données sur les factures
 * Fournit les méthodes CRUD de base et des requêtes personnalisées
 */
@Repository
public interface FactureRepository extends JpaRepository<Facture, Long> {

    /**
     * Trouve toutes les factures d'un client
     * @param clientId l'ID du client
     * @return la liste des factures du client
     */
    List<Facture> findByClientId(Long clientId);

    /**
     * Trouve toutes les factures entre deux dates
     * @param dateDebut la date de début
     * @param dateFin la date de fin
     * @return la liste des factures dans la période
     */
    List<Facture> findByDateFactureBetween(LocalDate dateDebut, LocalDate dateFin);

    /**
     * Trouve toutes les factures d'un client entre deux dates
     * @param clientId l'ID du client
     * @param dateDebut la date de début
     * @param dateFin la date de fin
     * @return la liste des factures du client dans la période
     */
    List<Facture> findByClientIdAndDateFactureBetween(Long clientId, LocalDate dateDebut, LocalDate dateFin);

    /**
     * Compte le nombre de factures d'un client
     * @param clientId l'ID du client
     * @return le nombre de factures du client
     */
    long countByClientId(Long clientId);

    /**
     * Trouve la facture avec ses lignes et le client (pour éviter les problèmes de lazy loading)
     * @param factureId l'ID de la facture
     * @return la facture avec ses relations chargées
     */
    @Query("SELECT f FROM Facture f LEFT JOIN FETCH f.lignes LEFT JOIN FETCH f.client WHERE f.id = :factureId")
    Facture findByIdWithLignesAndClient(@Param("factureId") Long factureId);
} 