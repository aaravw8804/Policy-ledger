# HDFC Life Policy Ledger

Spring Boot 3 / Java 25 REST API for HDFC Life customers, policies, claims and riders, backed by
Spring Data JPA with a Flyway-owned schema.

## How to run

```bash
mvn spring-boot:run
```

This starts on the `dev` profile by default: an in-memory H2 database (`MODE=PostgreSQL`), Flyway
migrations applied on startup, and the H2 console enabled at `/h2-console`.

To run against PostgreSQL, activate the `prod` profile and provide `DB_URL`, `DB_USER`,
`DB_PASSWORD`:

```bash
DB_URL=jdbc:postgresql://localhost:5432/hdfclife \
DB_USER=hdfclife \
DB_PASSWORD=change-me \
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

Swagger UI: http://localhost:8080/swagger-ui/index.html

## Endpoints

| Method | Path                              | Status              |
|--------|-----------------------------------|----------------------|
| GET    | /api/policies                     | 200 — all 6, policyNo ascending |
| GET    | /api/policies/{policyNo}          | 200 / 404            |
| GET    | /api/policies?status=Active       | 200 — derived query  |
| GET    | /api/policies?type=TERM           | 200 — derived query  |
| GET    | /api/policies?customer=Anita%20Sharma | 200 — derived query |
| GET    | /api/policies/search?minPremium=  | 200 — JPQL query, 400 if missing/negative |
| POST   | /api/policies                     | 201 / 400 / 409      |
| DELETE | /api/policies/{policyNo}          | 204 / 404            |
| GET    | /api/policies/{policyNo}/claims   | 200 / 404            |
| POST   | /api/claims                       | 201 / 400 / 404      |

## Ownership of `policy_riders`

`Policy` is the owning side of the `Policy <-> Rider` many-to-many relationship: it carries the
`@JoinTable(name = "policy_riders")` mapping. `Rider` has no back-reference — riders are looked up
independently and attached to a policy through `Policy.addRider(...)`, and changes are persisted by
saving the `Policy`.

## Why DTOs, given `open-in-view = false`

With `spring.jpa.open-in-view` off, the Hibernate session closes once the `@Transactional` service
method returns — before Jackson serializes the response outside the controller. Returning a
`Policy` entity straight from a controller would let Jackson try to walk its lazy `riders` and
`claims` collections after that session is gone, which throws
`LazyInitializationException` instead of a clean response. Mapping to a `PolicyResponse` inside the
transactional service method means every field, including the resolved `riderCodes` list, is read
while the session is still open, so the controller only ever hands back plain, already-fetched data.
DTOs also stop the API's JSON shape from leaking internal entity structure (IDs, back-references) or
changing every time the JPA model changes.

## Layout

```
src/main/java/com/hdfclife/ledger/
  LedgerApplication.java
  config/       DataSeeder, RestExceptionHandler
  domain/       Customer, Policy, Claim, Rider
  repo/         CustomerRepository, PolicyRepository, ClaimRepository, RiderRepository
  dto/          PolicyResponse, ClaimResponse, CreatePolicyRequest, CreateClaimRequest, ErrorResponse
  validation/   PolicyType, PolicyTypeValidator
  service/      PolicyService, ClaimService
  web/          PolicyController, ClaimController
  exception/    DeskException, PolicyNotFoundException, ClaimNotFoundException,
                DuplicatePolicyException, InvalidRequestException
src/main/resources/
  application.yml, application-dev.yml, application-prod.yml
  db/migration/ V1__hdfc_life_schema.sql, V2__seed_reference_riders.sql
```
