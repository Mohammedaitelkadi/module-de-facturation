package com.facturation.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Entité représentant une facture
 * Contient les informations de base d'une facture et ses lignes
 */
@Entity
@Table(name = "factures")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "lignes")
public class Facture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "La date de facture est obligatoire")
    @Column(name = "date_facture", nullable = false)
    private LocalDate dateFacture;

    @NotNull(message = "Le client est obligatoire")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    @com.fasterxml.jackson.annotation.JsonBackReference
    private Client client;

    @OneToMany(mappedBy = "facture", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<LigneFacture> lignes = new ArrayList<>();

    // Constructeur avec client
    public Facture(Client client) {
        this.dateFacture = LocalDate.now();
        this.client = client;
    }

    // Méthodes de gestion des lignes
    /**
     * Ajoute une ligne à la facture
     * @param ligne la ligne à ajouter
     */
    public void ajouterLigne(LigneFacture ligne) {
        lignes.add(ligne);
        ligne.setFacture(this);
    }

    /**
     * Supprime une ligne de la facture
     * @param ligne la ligne à supprimer
     */
    public void supprimerLigne(LigneFacture ligne) {
        lignes.remove(ligne);
        ligne.setFacture(null);
    }

    // Méthodes de calcul automatique
    /**
     * Calcule le total HT de la facture
     * @return total HT (somme des montants HT de toutes les lignes)
     */
    public BigDecimal getTotalHt() {
        BigDecimal total = BigDecimal.ZERO;
        
        // Parcourir toutes les lignes et additionner les montants HT
        for (LigneFacture ligne : lignes) {
            BigDecimal montantLigne = ligne.getMontantHt();
            total = total.add(montantLigne);
        }
        
        return total;
    }

    /**
     * Calcule le total TVA de la facture
     * @return total TVA (somme des montants TVA de toutes les lignes)
     */
    public BigDecimal getTotalTva() {
        BigDecimal total = BigDecimal.ZERO;
        
        // Parcourir toutes les lignes et additionner les montants TVA
        for (LigneFacture ligne : lignes) {
            BigDecimal montantLigne = ligne.getMontantTva();
            total = total.add(montantLigne);
        }
        
        return total;
    }

    /**
     * Calcule le total TTC de la facture
     * @return total TTC (total HT + total TVA)
     */
    public BigDecimal getTotalTtc() {
        BigDecimal totalHt = getTotalHt();
        BigDecimal totalTva = getTotalTva();
        return totalHt.add(totalTva);
    }
} 