package com.facturation.service;

import com.facturation.model.*;
import com.facturation.repository.ClientRepository;
import com.facturation.repository.FactureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Service pour la gestion des factures
 * Contient la logique métier pour les opérations sur les factures
 */
@Service
@Transactional
@RequiredArgsConstructor
public class FactureService {

    private final FactureRepository factureRepository;
    private final ClientRepository clientRepository;

    /**
     * Récupère toutes les factures
     * @return la liste de toutes les factures
     */
    public List<Facture> getAllFactures() {
        return factureRepository.findAll();
    }

    /**
     * Récupère une facture par son ID
     * @param id l'ID de la facture
     * @return la facture trouvée ou empty si non trouvée
     */
    public Optional<Facture> getFactureById(Long id) {
        Facture facture = factureRepository.findByIdWithLignesAndClient(id);
        return facture != null ? Optional.of(facture) : Optional.empty();
    }

    /**
     * Récupère toutes les factures d'un client
     * @param clientId l'ID du client
     * @return la liste des factures du client
     */
    public List<Facture> getFacturesByClientId(Long clientId) {
        return factureRepository.findByClientId(clientId);
    }

    /**
     * Crée une nouvelle facture simple
     * @param clientId l'ID du client
     * @param dateFacture la date de la facture
     * @return la facture créée
     * @throws IllegalArgumentException si le client n'existe pas
     */
    public Facture createFacture(Long clientId, LocalDate dateFacture) {
        // Vérifier que le client existe
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new IllegalArgumentException("Client non trouvé avec l'ID: " + clientId));

        // Créer la nouvelle facture
        Facture facture = new Facture(client);
        facture.setDateFacture(dateFacture);

        // Sauvegarder la facture
        return factureRepository.save(facture);
    }

    /**
     * Ajoute une ligne à une facture existante
     * @param factureId l'ID de la facture
     * @param description la description de la ligne
     * @param quantite la quantité
     * @param prixUnitaireHt le prix unitaire HT
     * @param tauxTva le taux de TVA
     * @return la facture mise à jour
     * @throws IllegalArgumentException si la facture n'existe pas
     */
    public Facture ajouterLigneFacture(Long factureId, String description, Integer quantite, 
                                     java.math.BigDecimal prixUnitaireHt, TauxTva tauxTva) {
        // Récupérer la facture existante
        Facture facture = factureRepository.findByIdWithLignesAndClient(factureId);
        if (facture == null) {
            throw new IllegalArgumentException("Facture non trouvée avec l'ID: " + factureId);
        }

        // Créer et ajouter la nouvelle ligne
        LigneFacture ligne = new LigneFacture(description, quantite, prixUnitaireHt, tauxTva);
        facture.ajouterLigne(ligne);

        // Sauvegarder les modifications
        return factureRepository.save(facture);
    }

    /**
     * Met à jour une facture existante
     * @param id l'ID de la facture à mettre à jour
     * @param clientId l'ID du nouveau client
     * @param dateFacture la nouvelle date de facture
     * @return la facture mise à jour
     * @throws IllegalArgumentException si la facture ou le client n'existe pas
     */
    public Facture updateFacture(Long id, Long clientId, LocalDate dateFacture) {
        // Récupérer la facture existante
        Facture existingFacture = factureRepository.findByIdWithLignesAndClient(id);
        if (existingFacture == null) {
            throw new IllegalArgumentException("Facture non trouvée avec l'ID: " + id);
        }

        // Vérifier que le client existe
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new IllegalArgumentException("Client non trouvé avec l'ID: " + clientId));

        // Mettre à jour les informations de base
        existingFacture.setDateFacture(dateFacture);
        existingFacture.setClient(client);

        // Sauvegarder les modifications
        return factureRepository.save(existingFacture);
    }

    /**
     * Supprime une facture
     * @param id l'ID de la facture à supprimer
     * @throws IllegalArgumentException si la facture n'existe pas
     */
    public void deleteFacture(Long id) {
        if (!factureRepository.existsById(id)) {
            throw new IllegalArgumentException("Facture non trouvée avec l'ID: " + id);
        }
        factureRepository.deleteById(id);
    }
} 