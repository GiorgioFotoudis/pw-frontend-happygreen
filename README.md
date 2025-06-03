# Come utilizzare la nostra applicazione

Una volta scaricati ed importati i progetti aprire XAMPP e avviare Apache Web Server e MySQL Database.

## Importare il database all'interno di MySQL
Il databse esportato caricato su classroom è già dotato di 'CREATE/USE DATABASE' per facilitarne l'importazione

## Installazion ngrok da terminale

Se non è già presente sul proprio dispositivo, installare [ngrok](https://ngrok.com/) utilizzando questi comandi.

```bash
brew install ngrok
```
```bash
ngrok config add-authtoken 2xRpQqf7QAsGMDDioW8wNefBRXv_82YzXCKFExv5KPGS9KWTm
```

## Avviare ngrok da terminale

```bash
ngrok http http://localhost:8080
```

## Inserimento del Link nel frontend

Una volta avviato ngrok copiare il link con .app finale nella sezione Forwarding.

Incollare il link ottenuto all'interno dell'oggetto ApiClient
(com.example.frontend_happygreen/data/remote/ApiClient)

```Kotlin
object ApiClient {

    private const val BASE_URL = "link copiato da ngrok"

```
E incollarlo all'interno del file network_security_config.xml
(

```xml
<?xml version="1.0" encoding="utf-8"?>
<network-security-config>
    <domain-config cleartextTrafficPermitted="true">
        <domain includeSubdomains="true">link copiato SENZA https://</domain>
    </domain-config>
</network-security-config>

```

## Esecuzione

Ora si può fare il run del backend, collegare il dispositivo alla macchina e fare il run del frontend per utilizzare la nostra applicazione androi!
