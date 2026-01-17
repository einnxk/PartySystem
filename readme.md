# Party System 1.21.8+

## Beschreibung
Partysystem Back-End für Minecraft Server. Benutzt wird hierfür Spring Boot mit einer Redis-Datenbank, somit können die Requests in kürzester Zeit bearbeitet werden.
<br>Das Projekt ist einfach und in einer ordentlichen Ordnerstruktur aufgebaut, um Änderungen daran später leichter zu machen, bzw. zu ermöglichen.
Ebenfalls ist ein Velocity Teil enthalten in dem die Http-Request von der anderen Seite gezeigt werden. 

> ### ⚠️ Wichtige Information
> Dies ist ein Portfolio Projekt, daher liegt der Fokus auf der Architektur, Skalierung, Sicherheitsaspekte sind hier vereinfacht vertreten.

## Features

- Rest-API (Spring Boot)
- Redis-Datenbank (State & Pub/Sub)
- Stateless Service
- Web-Security mit API Key
- Velocity Adapter

---
## Deployment mit Docker (PartyRestAPI)
### Voraussetzungen
Docker, sowie Docker-Compose sind auf dem Server installiert.
### Build und Deployment
> ⚠️ Docker File, sowie docker-compose.yml sind im Projekt enthalten, wenn du dir nicht sicher bist wie Docker funktioniert, empfehle ich diese zu benutzen.
1. Erstelle das JAR-File der Anwendung:
    ```bash
    ./gradlew build
    ```
2. Baue das Docker-Image:
    ```bash
    docker build -t party-rest-api .
    ```
3. Starte die Anwendung:
    ```bash
    docker run -p 8080:8080 party-rest-api
    ```
### Deployment mit Redis (Docker-Compose)
1. Stelle sicher, dass `docker-compose.yml` im Projekt vorhanden ist.
2. Starte die Dienste:
    ```bash
    docker-compose up
    ```

Die Back-End Anwendung ist jetzt unter `http://localhost:8080` verfügbar.
<br>Das Velocity Plugin muss noch in den Plugins Ordner, bzw. Templates Ordner gezogen werden.
