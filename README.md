# my-stuff

Example product for the book "The Effective Software Engineer": https://leanpub.com/the-effective-software-engineer

## Appendix B (ATDD example) at a glance

Appendix B in the book walks through this repo as a concrete ATDD example. The intent is not a full code listing, but a tour of why each artifact exists and how the pieces enable fast, safe change.

### Repository structure and intent

- `domain/` contains business rules (entities, interactors, invariants) with minimal framework dependencies.
- `application/` wires delivery concerns (controllers, serialization, auth, integration with domain).
- `acceptance/` holds BDD-style acceptance tests that express behavior as executable specifications.
- `ui-acceptance/` runs end-to-end flows through the UI with Playwright.
- `ng-frontend/` is an independently buildable/testable frontend artifact.
- CI configuration (under `.github/workflows/`) enforces the feedback loop.
- Infrastructure support (`docker-compose.yaml`, `certs/`, `keystore/`) enables local development.

This separation keeps business logic stable, delivery mechanisms flexible, and behavior validation external, making both feature work and refactoring safe.

### Acceptance testing architecture (4-layer model)

1. Executable specifications describe observable behavior (what, not how).
2. DSL provides readable building blocks for scenarios.
3. Protocol drivers translate DSL actions to the system under test.
4. The system under test (domain, controller layer, UI).

The same scenarios run against different drivers, keeping intent stable even as infrastructure changes.

### Drivers and scenarios

- Domain driver tests business rules in a fast, isolated environment.
- Controller driver validates REST behavior, status codes, and security boundaries.
- UI driver (Playwright) verifies real user flows and security propagation.

Scenarios focus on behavior, while drivers adapt execution environments. This keeps tests deterministic and sustainable.

### CI pipeline and feedback

- Commit stage: fast build, unit/lower-level tests, static analysis (e.g., Sonar).
- Acceptance stage: slower, higher-confidence acceptance and UI acceptance tests.

Together they create a continuous feedback system: TDD keeps design clean, acceptance tests keep intent stable, CI enforces standards, and trunk-based development becomes safe.

### Evidence-driven progress and organizational impact

- Acceptance tests provide objective, repeatable evidence of user-visible behavior.
- The delivery pipeline naturally supports DORA metrics (lead time, deployment frequency, change failure rate, MTTR).
- Progress reporting becomes transparent without blame, improving trust across teams and management.
- Long-term resilience comes from executable acceptance criteria that survive architecture and team changes.
- The system replaces heroics with habits: test-driven design, fast feedback, and automation of the boring.

If you want the full context, Appendix B in the book is the source of truth.

## Local startup

1. Launch Keycloak from the docker compose file:
   - `docker compose -f docker-compose.yaml up -d`
2. Import the sample realm from the Keycloak file:
   - `acceptance/src/test/resources/keycloak/my-stuff.realm.json`
3. Build locally (Java 25 + Maven):
   - `mvn -B clean verify --file pom.xml`
4. Launch the Java application with JVM parameters (example):

```bash
-Djavax.net.ssl.keyStore=path-to-local-keystore-directory
-Djavax.net.ssl.keyStorePassword=changeit
-Djavax.net.ssl.trustStore=path-to-local-keystore-directory
-Djavax.net.ssl.trustStorePassword=changeit
-Dspring.security.oauth2.client.registration.my-stuff.client-secret=the-secret-from-your-keycloak-client-registration
```
