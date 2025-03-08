# Installation et Configuration du Projet

## Prérequis

Avant de commencer, assurez-vous d'avoir :
  - **Java 11**
  - **NodeJS 16**
  - **MySQL**
  - **Angular CLI 14**

## Installer le projet

  - Pour le front-end :
    ```sh
    cd front
    npm install
    ```
  - Pour le back-end :
    ```sh
    cd back
    mvn clean install
    ```

#### Installation de la base de données

1. Assurez-vous que MySQL est installé et démarré.
2. Créez une base de données pour le projet et importer le script sql contenu à la racine du projet dans:
   ``` ressources > sql >script.sql ```
3. Assurez-vous que la base de données fonctionne sur le port par défaut **3306**.

#### Démarrage du projet

1. **Démarrer le serveur et l'API** :
    ```sh
    cd back
    mvn spring-boot:run
    ```
2. **Démarrer le front-end** :
    ```sh
    cd front
    ng serve
    ```

### 3. Connexion et vérifications

#### Connexion avec un compte admin
- **Login** : `yoga@studio.com`
- **Password** : `test!1234`


### 4. Lancer les tests

1.Tests unitaires et test d'intégration back-end ( avec JUnit et Mockito)

1. Depuis le répertoire du back-end, exécutez la commande `mvn clean test` pour lancer les tests unitaires et d'intégration back-end.
2. Le rapport de couverture sera généré dans le dossier `back/target/site/jacoco/index.html`.

2.Tests unitaires et tests d'intégration front-end (avec Jest)

1. Depuis le répertoire du front-end, exécutez la commande `npm run test` pour lancer les tests unitaires front-end avec Jest.
2. Pour afficher le rapport de couverture, lancez la commande `npm run test-coverage`.
Le rapport de couverture sera généré dans le dossier `front/coverage/jest/lcov-report/index.html`.

3.Tests end-to-end (Cypress)

1. Depuis le répertoir front-end, exécutez la commande `npm run cypress:open` puis `npm run cypress:run` pour lancer les tests end-to-end avec Cypress.
2. Pour afficher le rapport de couverture, exécutez la commande `npm run e2e:coverage`.


