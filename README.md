Base project for java backend app:

Srping WEB + JWT + JPA + Flyway + Docker

To execute the base app:

```
docker compose down -v                                               
docker compose up app --build -d
```

First command wil refresh postgresql data and the second one will start the app.

You can start only db with

```docker compose up db```

And after start the application locally with:

```./gradlew bootRun --args='--spring.profiles.active=local' ```

The app's login does NOTHING with the user and the password, they should be checked against somethig but is not implemented yet (and I am not sure that I should do it here)

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