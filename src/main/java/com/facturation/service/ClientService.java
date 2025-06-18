package com.facturation.service;

import com.facturation.model.Client;
import com.facturation.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service pour la gestion des clients
 * Contient la logique métier pour les opérations sur les clients
 */
@Service
@Transactional
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;

    /**
     * Récupère tous les clients
     * @return la liste de tous les clients
     */
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    /**
     * Récupère un client par son ID
     * @param id l'ID du client
     * @return le client trouvé ou empty si non trouvé
     */
    public Optional<Client> getClientById(Long id) {
        return clientRepository.findById(id);
    }

    /**
     * Crée un nouveau client
     * @param client les données du client à créer
     * @return le client créé
     * @throws IllegalArgumentException si l'email ou le SIRET existe déjà
     */
    public Client createClient(Client client) {
        // Vérifier si l'email existe déjà
        if (clientRepository.existsByEmail(client.getEmail())) {
            throw new IllegalArgumentException("Un client avec cet email existe déjà");
        }

        // Vérifier si le SIRET existe déjà
        if (clientRepository.existsBySiret(client.getSiret())) {
            throw new IllegalArgumentException("Un client avec ce SIRET existe déjà");
        }

        // S'assurer que la date de création est définie
        if (client.getDateCreation() == null) {
            client.setDateCreation(java.time.LocalDateTime.now());
        }

        // Sauvegarder le client
        return clientRepository.save(client);
    }

    /**
     * Met à jour un client existant
     * @param id l'ID du client à mettre à jour
     * @param client les nouvelles données du client
     * @return le client mis à jour
     * @throws IllegalArgumentException si le client n'existe pas ou si l'email/SIRET existe déjà
     */
    public Client updateClient(Long id, Client client) {
        // Récupérer le client existant
        Client existingClient = clientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Client non trouvé avec l'ID: " + id));

        // Vérifier si l'email existe déjà (sauf pour ce client)
        Optional<Client> clientWithEmail = clientRepository.findByEmail(client.getEmail());
        if (clientWithEmail.isPresent() && !clientWithEmail.get().getId().equals(id)) {
            throw new IllegalArgumentException("Un client avec cet email existe déjà");
        }

        // Vérifier si le SIRET existe déjà (sauf pour ce client)
        Optional<Client> clientWithSiret = clientRepository.findBySiret(client.getSiret());
        if (clientWithSiret.isPresent() && !clientWithSiret.get().getId().equals(id)) {
            throw new IllegalArgumentException("Un client avec ce SIRET existe déjà");
        }

        // Mettre à jour les informations du client
        existingClient.setNom(client.getNom());
        existingClient.setEmail(client.getEmail());
        existingClient.setSiret(client.getSiret());

        // Sauvegarder les modifications
        return clientRepository.save(existingClient);
    }

    /**
     * Supprime un client
     * @param id l'ID du client à supprimer
     * @throws IllegalArgumentException si le client n'existe pas
     */
    public void deleteClient(Long id) {
        // Vérifier que le client existe
        if (!clientRepository.existsById(id)) {
            throw new IllegalArgumentException("Client non trouvé avec l'ID: " + id);
        }
        clientRepository.deleteById(id);
    }
} 