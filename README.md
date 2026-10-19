# Retailer Rewards API — Spring Data JPA edition

Spring Boot REST API calculates customer reward points **per transaction**, grouped by month, with a three-month total.

## Run

### Endpoints
- All customer rewards with a starting month: http://localhost:8080/api/v1/rewards?startMonth=2026-02
- Single customer: http://localhost:8080/api/v1/rewards/C001?startMonth=2026-01

### Swagger / OpenAPI
- Swagger: http://localhost:8080/swagger-ui.html
- OpenAPI JSON: http://localhost:8080/v3/api-docs

### H2 Database
- H2 console: http://localhost:8080/h2-console (JDBC URL `jdbc:h2:mem:rewardsdb;DB_CLOSE_DELAY=-1`, username `sa`, blank password)

**Sample data are hard-coded only in `SampleDataLoader`, not in the JPA repository**. At startup the loader inserts 14 sample transactions into H2; `TransactionRepository` extends `JpaRepository` and Spring Data JPA provides persistence and query implementation. Schema is automatically created for demo use; H2 data are discarded on application restart.

## Technologies
- Spring Boot 3.5.7, **Java 17 runtime** (Java 8 syntax and functional idioms: Streams, Optional, lambdas, Collectors)
- Spring Web, **Spring Data JPA**, Hibernate and H2 in-memory SQL database
- Springdoc OpenAPI / Swagger UI; Jakarta Validation; global error handling
- JUnit 5, Mockito, MockMvc, and `@DataJpaTest`

**Note:** Spring Boot 3.x cannot run on Java 8. If the assignment explicitly requires a Java 8 *runtime*, use Spring Boot 2.7.x with `javax.persistence` and a compatible older Swagger library instead.

## Code Repository:
- [GitHub](https://github.com/SarathBodepudi/retailer-rewards-api)
