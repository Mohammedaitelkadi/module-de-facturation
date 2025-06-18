package com.facturation.controller;

import com.facturation.model.Facture;
import com.facturation.model.TauxTva;
import com.facturation.service.FactureService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Contrôleur REST pour la gestion des factures
 * Fournit les endpoints pour les opérations CRUD sur les factures et l'export JSON
 */
@RestController
@RequestMapping("/api/factures")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class FactureController {

    private final FactureService factureService;

    /**
     * Récupère toutes les factures
     * GET /api/factures
     * @return la liste de toutes les factures
     */
    @GetMapping
    public ResponseEntity<List<Facture>> getAllFactures() {
        List<Facture> factures = factureService.getAllFactures();
        return ResponseEntity.ok(factures);
    }

    /**
     * Récupère une facture par son ID
     * GET /api/factures/{id}
     * @param id l'ID de la facture
     * @return la facture trouvée ou 404 si non trouvée
     */
    @GetMapping("/{id}")
    public ResponseEntity<Facture> getFactureById(@PathVariable Long id) {
        return factureService.getFactureById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Récupère toutes les factures d'un client
     * GET /api/factures/client/{clientId}
     * @param clientId l'ID du client
     * @return la liste des factures du client
     */
    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<Facture>> getFacturesByClientId(@PathVariable Long clientId) {
        List<Facture> factures = factureService.getFacturesByClientId(clientId);
        return ResponseEntity.ok(factures);
    }

    /**
     * Crée une nouvelle facture avec JSON
     * POST /api/factures
     * @param request les données de la facture en JSON
     * @return la facture créée avec le statut 201
     */
    @PostMapping
    public ResponseEntity<Facture> createFacture(@RequestBody CreateFactureRequest request) {
        try {
            Facture createdFacture = factureService.createFacture(request.getClientId(), request.getDateFacture());
            return ResponseEntity.status(HttpStatus.CREATED).body(createdFacture);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Ajoute une ligne à une facture avec JSON
     * POST /api/factures/{id}/lignes
     * @param id l'ID de la facture
     * @param request les données de la ligne en JSON
     * @return la facture mise à jour
     */
    @PostMapping("/{id}/lignes")
    public ResponseEntity<Facture> ajouterLigneFacture(
            @PathVariable Long id,
            @RequestBody AddLigneRequest request) {
        try {
            Facture updatedFacture = factureService.ajouterLigneFacture(
                id, 
                request.getDescription(), 
                request.getQuantite(), 
                request.getPrixUnitaireHt(), 
                request.getTauxTva()
            );
            return ResponseEntity.ok(updatedFacture);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Met à jour une facture avec JSON
     * PUT /api/factures/{id}
     * @param id l'ID de la facture à mettre à jour
     * @param request les nouvelles données de la facture
     * @return la facture mise à jour ou 404 si non trouvée
     */
    @PutMapping("/{id}")
    public ResponseEntity<Facture> updateFacture(
            @PathVariable Long id,
            @RequestBody CreateFactureRequest request) {
        try {
            Facture updatedFacture = factureService.updateFacture(id, request.getClientId(), request.getDateFacture());
            return ResponseEntity.ok(updatedFacture);
        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("non trouvée")) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Supprime une facture
     * DELETE /api/factures/{id}
     * @param id l'ID de la facture à supprimer
     * @return 204 si supprimée avec succès ou 404 si non trouvée
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFacture(@PathVariable Long id) {
        try {
            factureService.deleteFacture(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Exporte une facture au format JSON
     * GET /api/factures/{id}/export
     * @param id l'ID de la facture à exporter
     * @return la facture au format JSON ou 404 si non trouvée
     */
    @GetMapping("/{id}/export")
    public ResponseEntity<Facture> exportFacture(@PathVariable Long id) {
        return factureService.getFactureById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Classe pour recevoir les données de création de facture en JSON
     */
    public static class CreateFactureRequest {
        private Long clientId;
        private LocalDate dateFacture;

        // Getters et setters
        public Long getClientId() { return clientId; }
        public void setClientId(Long clientId) { this.clientId = clientId; }
        public LocalDate getDateFacture() { return dateFacture; }
        public void setDateFacture(LocalDate dateFacture) { this.dateFacture = dateFacture; }
    }

    /**
     * Classe pour recevoir les données d'ajout de ligne en JSON
     */
    public static class AddLigneRequest {
        private String description;
        private Integer quantite;
        private BigDecimal prixUnitaireHt;
        private TauxTva tauxTva;

        // Getters et setters
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public Integer getQuantite() { return quantite; }
        public void setQuantite(Integer quantite) { this.quantite = quantite; }
        public BigDecimal getPrixUnitaireHt() { return prixUnitaireHt; }
        public void setPrixUnitaireHt(BigDecimal prixUnitaireHt) { this.prixUnitaireHt = prixUnitaireHt; }
        public TauxTva getTauxTva() { return tauxTva; }
        public void setTauxTva(TauxTva tauxTva) { this.tauxTva = tauxTva; }
    }
} 