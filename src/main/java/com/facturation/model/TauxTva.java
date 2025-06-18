package com.facturation.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

/**
 * Énumération des taux de TVA autorisés
 * Définit les valeurs de TVA supportées par le système
 */
@Getter
@AllArgsConstructor
public enum TauxTva {
    ZERO(0.0, "0%"),
    CINQ_CINQ(5.5, "5.5%"),
    DIX(10.0, "10%"),
    VINGT(20.0, "20%");

    private final BigDecimal valeur;
    private final String libelle;

    TauxTva(double valeur, String libelle) {
        this.valeur = BigDecimal.valueOf(valeur);
        this.libelle = libelle;
    }

    @Override
    public String toString() {
        return libelle;
    }
} 