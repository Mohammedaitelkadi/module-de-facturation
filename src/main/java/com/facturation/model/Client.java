package com.facturation.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entité représentant un client
 * Contient les informations de base d'un client
 */
@Entity
@Table(name = "clients")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "factures")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom du client est obligatoire")
    @Column(name = "nom", nullable = false)
    private String nom;

    @NotBlank(message = "L'email du client est obligatoire")
    @Email(message = "Format d'email invalide")
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Le SIRET est obligatoire")
    @Pattern(regexp = "^[0-9]{14}$", message = "Le SIRET doit contenir exactement 14 chiffres")
    @Column(name = "siret", nullable = false, unique = true)
    private String siret;

    @Column(name = "date_creation", nullable = false)
    private LocalDateTime dateCreation;

    // Relation avec les factures (un client peut avoir plusieurs factures)
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @com.fasterxml.jackson.annotation.JsonManagedReference
    private List<Facture> factures = new ArrayList<>();

    // Constructeur avec paramètres (sans id et dateCreation)
    public Client(String nom, String email, String siret) {
        this.nom = nom;
        this.email = email;
        this.siret = siret;
        this.dateCreation = LocalDateTime.now();
    }
} 