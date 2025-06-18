package com.facturation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Classe principale de l'application de facturation
 * Point d'entrée pour démarrer le serveur Spring Boot
 */
@SpringBootApplication
public class FacturationApplication {

    public static void main(String[] args) {
        SpringApplication.run(FacturationApplication.class, args);
    }
} 