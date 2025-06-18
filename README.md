# Module de Facturation - Spring Boot

## Description

Application de facturation développée en Java avec Spring Boot. Permet de gérer les clients et les factures avec calcul automatique des montants.

## Fonctionnalités

### Gestion des clients
- Liste des clients
- Création d'un client
- Modification d'un client
- Suppression d'un client

### Gestion des factures
- Liste des factures
- Création d'une facture simple
- Ajout de lignes à une facture
- Modification d'une facture
- Suppression d'une facture
- Calcul automatique des montants (HT, TVA, TTC)
- Export JSON d'une facture

## Technologies

- Java 17
- Spring Boot 3.2.0
- Spring Data JPA
- PostgreSQL
- Maven
- Lombok

## Installation

### Prérequis
- Java 17
- Maven
- PostgreSQL

### Configuration

1. Créer une base de données PostgreSQL :
```sql
CREATE DATABASE facturation_db;
```

2. Créer un fichier `.env` à la racine du projet avec vos configurations :
```bash
# Database Configuration
DB_URL=jdbc:postgresql://localhost:5432/facturation_db
DB_USERNAME=postgres
DB_PASSWORD=votre_mot_de_passe

# Server Configuration
SERVER_PORT=8080

# Logging Configuration
LOGGING_LEVEL_COM_FACTURATION=DEBUG
LOGGING_LEVEL_ORG_SPRINGFRAMEWORK_WEB=DEBUG
```

**⚠️ Important :** Le fichier `.env` ne doit jamais être commité dans Git car il contient des informations sensibles.

3. Lancer l'application :
```bash
mvn spring-boot:run
```

L'application sera accessible sur `http://localhost:8080`

## API

### Clients
- `GET /api/clients` - Liste des clients
- `GET /api/clients/{id}` - Détail d'un client
- `POST /api/clients` - Créer un client
- `PUT /api/clients/{id}` - Modifier un client
- `DELETE /api/clients/{id}` - Supprimer un client

### Factures
- `GET /api/factures` - Liste des factures
- `GET /api/factures/{id}` - Détail d'une facture
- `GET /api/factures/client/{clientId}` - Factures d'un client
- `POST /api/factures` - Créer une facture avec JSON
- `POST /api/factures/{id}/lignes` - Ajouter une ligne à une facture avec JSON
- `PUT /api/factures/{id}` - Modifier une facture avec JSON
- `DELETE /api/factures/{id}` - Supprimer une facture
- `GET /api/factures/{id}/export` - Export JSON d'une facture

## Exemples d'utilisation avec Postman

### 1. Gestion des Clients

#### Créer un client
```
POST http://localhost:8080/api/clients
Content-Type: application/json

{
  "nom": "Entreprise ABC",
  "email": "contact@abc.com",
  "siret": "12345678901234"
}
```

#### Récupérer tous les clients
```
GET http://localhost:8080/api/clients
```

#### Récupérer un client par ID
```
GET http://localhost:8080/api/clients/1
```

#### Modifier un client
```
PUT http://localhost:8080/api/clients/1
Content-Type: application/json

{
  "nom": "Entreprise ABC Modifiée",
  "email": "nouveau@abc.com",
  "siret": "12345678901234"
}
```

#### Supprimer un client
```
DELETE http://localhost:8080/api/clients/1
```

### 2. Gestion des Factures

#### Créer une facture avec JSON
```
POST http://localhost:8080/api/factures
Content-Type: application/json

{
  "clientId": 1,
  "dateFacture": "2024-01-15"
}
```

#### Récupérer toutes les factures
```
GET http://localhost:8080/api/factures
```

#### Récupérer une facture par ID
```
GET http://localhost:8080/api/factures/1
```

#### Récupérer les factures d'un client
```
GET http://localhost:8080/api/factures/client/1
```

#### Ajouter une ligne à une facture avec JSON
```
POST http://localhost:8080/api/factures/1/lignes
Content-Type: application/json

{
  "description": "Développement web",
  "quantite": 10,
  "prixUnitaireHt": 50.00,
  "tauxTva": "VINGT"
}
```

#### Ajouter une autre ligne
```
POST http://localhost:8080/api/factures/1/lignes
Content-Type: application/json

{
  "description": "Hébergement",
  "quantite": 1,
  "prixUnitaireHt": 25.00,
  "tauxTva": "DIX"
}
```

#### Modifier une facture avec JSON
```
PUT http://localhost:8080/api/factures/1
Content-Type: application/json

{
  "clientId": 1,
  "dateFacture": "2024-01-20"
}
```

#### Exporter une facture en JSON
```
GET http://localhost:8080/api/factures/1/export
```

#### Supprimer une facture
```
DELETE http://localhost:8080/api/factures/1
```

### 3. Exemple de workflow complet

1. **Créer un client** :
   ```
   POST http://localhost:8080/api/clients
   Content-Type: application/json
   
   {
     "nom": "Société XYZ",
     "email": "contact@xyz.fr",
     "siret": "98765432109876"
   }
   ```

2. **Créer une facture** avec JSON (notez l'ID retourné, par exemple 1) :
   ```
   POST http://localhost:8080/api/factures
   Content-Type: application/json
   
   {
     "clientId": 1,
     "dateFacture": "2024-01-15"
   }
   ```

3. **Ajouter des lignes** à la facture avec JSON :
   ```
   POST http://localhost:8080/api/factures/1/lignes
   Content-Type: application/json
   
   {
     "description": "Développement",
     "quantite": 20,
     "prixUnitaireHt": 75.00,
     "tauxTva": "VINGT"
   }
   ```

   ```
   POST http://localhost:8080/api/factures/1/lignes
   Content-Type: application/json
   
   {
     "description": "Maintenance",
     "quantite": 5,
     "prixUnitaireHt": 30.00,
     "tauxTva": "DIX"
   }
   ```

4. **Vérifier la facture** avec calculs automatiques :
   ```
   GET http://localhost:8080/api/factures/1
   ```

5. **Exporter la facture** :
   ```
   GET http://localhost:8080/api/factures/1/export
   ```

### 4. Valeurs possibles pour les paramètres

#### Taux de TVA
- `ZERO` : 0%
- `CINQ_CINQ` : 5.5%
- `DIX` : 10%
- `VINGT` : 20%

#### Format des dates
- Format ISO : `YYYY-MM-DD`
- Exemple : `2024-01-15`

#### Format des prix
- Nombres décimaux avec point
- Exemple : `50.00`, `25.50`, `100.00`

## Architecture

### Structure MVC simplifiée
```
src/main/java/com/facturation/
├── controller/     # Contrôleurs REST
├── model/         # Entités JPA
├── repository/    # Repositories JPA
└── service/       # Services métier
```

### Flux de données
```
API → Contrôleur → Service → Repository → Base de données
   ← Contrôleur ← Service ← Repository ← Base de données
```

### Entités principales
- **Client** : Informations des clients
- **Facture** : Factures avec calculs automatiques
- **LigneFacture** : Lignes de détail des factures
- **TauxTva** : Énumération des taux de TVA

## Règles métier

- Une facture peut être créée sans lignes initialement
- Les lignes sont ajoutées séparément via l'API dédiée
- Validation des emails et SIRET
- Calcul automatique des montants HT, TVA et TTC
- Taux de TVA supportés : 0%, 5.5%, 10%, 20%
- Unicité des emails et SIRET

## Validation

Les validations sont appliquées directement sur les entités :
- **Email** : Format email valide
- **SIRET** : Exactement 14 chiffres
- **Quantité** : Minimum 1
- **Prix unitaire HT** : Minimum 0.01 