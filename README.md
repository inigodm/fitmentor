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
