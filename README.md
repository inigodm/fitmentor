Base project for java backend app:

Spring WEB + JWT + JPA + Flyway + Docker

## Running the Application

### Option 1: Full Docker Stack

To execute the full app with Docker:

```bash
docker compose down -v                                               
docker compose up app --build -d
```

First command will refresh postgresql data and the second one will start the app.

### Option 2: Local Development (Recommended)

**Backend (Port 8080):**

You can start only the database with:

```bash
docker compose up db
```

And then start the application locally with:

```bash
./gradlew bootRun --args='--spring.profiles.active=local'
```

**Frontend (Port 4200):**

The frontend is configured with a proxy to redirect API calls from `http://localhost:4200/api/*` to `http://localhost:8080/api/*`.

**For simple local development (HTTP on localhost:4200):**

```bash
cd frontend
npm install  # First time only
npm run start:local
```

Or use the script:

```bash
./scripts/run-frontend-local.sh
```

**For HTTPS development (requires certificates, see FIDO section below):**

```bash
cd frontend
npm install  # First time only
npm start
```

Or use the script:

```bash
./scripts/run-frontend.sh
```

> **Note:** The frontend proxy is configured in `frontend/proxy.conf.json` to redirect `/api` and `/webauthn` endpoints to `http://localhost:8080`.

### API Access

- **Backend API:** http://localhost:8080/api
- **Frontend (local):** http://localhost:4200
- **Frontend (HTTPS):** https://fitmentor.com:4200
- **Swagger UI:** http://localhost:8080/swagger-ui.html

The app's login does NOTHING with the user and the password, they should be checked against something but is not implemented yet (and I am not sure that I should do it here)


## Using FIDO

In order to use advanced validation we need to enable https and a domain name:

To use locally, add this to /etc/hosts:

``` 127.0.0.1   fitmentor.com ```

Install mkcert:

 ``` 
 sudo apt install libnss3-tools mkcert  # Linux
 brew install mkcert              # Mac 
 ```

And create certificates:

```
mkcert -install
mkcert fitmentor.com
```

And export p12 cert:

```
openssl pkcs12 -export \
  -in fitmentor.com.pem \
  -inkey fitmentor.com-key.pem \  
  -out fitmentor.com.p12 \
  -name fitmentor.com \
  -password pass:fitmentor_change_it
```

And save it on /resources OR in any place on your computer changing application.yml key properly: 

``` server.ssl.key-store: file:/etc/ssl/firmento.com.p12 ```