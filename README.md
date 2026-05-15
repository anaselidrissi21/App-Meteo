# App Meteo

Application meteo full-stack qui permet de rechercher une ville et d'afficher la meteo actuelle.

Le projet est compose de deux parties :

- `backend` : API REST Java avec Spring Boot.
- `frontend` : interface Angular.

L'application utilise Open-Meteo, une API meteo gratuite qui ne demande pas de cle API.

## Fonctionnalites

- recherche meteo par ville
- temperature actuelle en degres Celsius
- humidite
- vitesse du vent
- description meteo en francais
- icone meteo selon la condition
- messages d'erreur si la ville est introuvable ou si le serveur backend est indisponible

## Prerequis

Installez les outils suivants avant de lancer le projet :

- Java 11 ou plus recent
- Maven
- Node.js
- npm

Pour verifier :

```bash
java -version
mvn -version
node -v
npm -v
```

## Installation

Clonez le projet puis installez les dependances du frontend :

```bash
git clone <url-du-repo>
cd App-Meteo/frontend
npm install
```

Le backend telecharge ses dependances automatiquement via Maven au premier lancement.

## Lancer l'application

Il faut lancer le backend et le frontend dans deux terminaux differents.

### 1. Backend

Depuis la racine du projet :

```bash
cd backend
mvn spring-boot:run
```

Le backend demarre sur :

```text
http://localhost:8080
```

Endpoint disponible :

```text
GET http://localhost:8080/api/weather?city=Paris
```

### 2. Frontend

Dans un autre terminal, depuis la racine du projet :

```bash
cd frontend
npm start
```

Le frontend demarre sur :

```text
http://localhost:4200/
```

Ouvrez cette URL dans votre navigateur.

## Commandes utiles

Compiler le frontend :

```bash
cd frontend
npm run build
```

Lancer les tests frontend :

```bash
cd frontend
npm test
```

Lancer les tests backend :

```bash
cd backend
mvn test
```

## Depannage

Si `http://localhost:4200/` affiche une erreur, verifiez que le frontend est bien lance avec `npm start`.

Si l'application affiche `Le serveur meteo est inaccessible`, verifiez que le backend est bien lance sur le port `8080`.

Si le port `4200` est deja utilise, un serveur Angular est probablement deja lance. Vous pouvez l'arreter avec :

```bash
lsof -i :4200
kill <PID>
```

Ou lancer Angular sur un autre port :

```bash
npm start -- --port 4201
```

Dans ce cas, il faudra aussi autoriser ce port cote backend si vous utilisez une autre URL que `http://localhost:4200`.
