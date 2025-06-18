package com.facturation.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * Entité représentant une ligne de facture
 * Contient les détails d'un article ou service facturé
 */
@Entity
@Table(name = "lignes_facture")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "facture")
public class LigneFacture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "La description est obligatoire")
    @Column(name = "description", nullable = false)
    private String description;

    @NotNull(message = "La quantité est obligatoire")
    @Min(value = 1, message = "La quantité doit être au moins égale à 1")
    @Column(name = "quantite", nullable = false)
    private Integer quantite;

    @NotNull(message = "Le prix unitaire HT est obligatoire")
    @DecimalMin(value = "0.01", message = "Le prix unitaire HT doit être supérieur à 0")
    @Column(name = "prix_unitaire_ht", nullable = false, precision = 10, scale = 2)
    private BigDecimal prixUnitaireHt;

    @NotNull(message = "Le taux de TVA est obligatoire")
    @Column(name = "taux_tva", nullable = false)
    @Enumerated(EnumType.STRING)
    private TauxTva tauxTva;

    // Relation avec la facture
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "facture_id", nullable = false)
    @com.fasterxml.jackson.annotation.JsonBackReference
    private Facture facture;

    // Constructeur avec paramètres (sans id et facture)
    public LigneFacture(String description, Integer quantite, BigDecimal prixUnitaireHt, TauxTva tauxTva) {
        this.description = description;
        this.quantite = quantite;
        this.prixUnitaireHt = prixUnitaireHt;
        this.tauxTva = tauxTva;
    }

    // Méthodes de calcul
    /**
     * Calcule le montant HT de la ligne
     * @return montant HT (quantité * prix unitaire HT)
     */
    public BigDecimal getMontantHt() {
        // Multiplier la quantité par le prix unitaire
        BigDecimal quantiteBigDecimal = BigDecimal.valueOf(quantite);
        return prixUnitaireHt.multiply(quantiteBigDecimal);
    }

    /**
     * Calcule le montant de TVA de la ligne
     * @return montant TVA (montant HT * taux TVA)
     */
    public BigDecimal getMontantTva() {
        // Récupérer le montant HT
        BigDecimal montantHt = getMontantHt();
        
        // Calculer le taux de TVA (diviser par 100 car c'est un pourcentage)
        BigDecimal tauxDecimal = tauxTva.getValeur().divide(BigDecimal.valueOf(100));
        
        // Multiplier le montant HT par le taux de TVA
        return montantHt.multiply(tauxDecimal);
    }

    /**
     * Calcule le montant TTC de la ligne
     * @return montant TTC (montant HT + montant TVA)
     */
    public BigDecimal getMontantTtc() {
        // Récupérer le montant HT et la TVA
        BigDecimal montantHt = getMontantHt();
        BigDecimal montantTva = getMontantTva();
        
        // Additionner les deux montants
        return montantHt.add(montantTva);
    }
} 