# OMGCLUSTER

## Rules

- Never add Co-Authored-By lines to git commit messages
- Keep `MODULES.md` in sync with module dependencies: whenever a module is added, removed, or its inter-module
  dependencies change, update the mermaid graph in `MODULES.md` accordingly

## Project structure

- The `server` module is the deployable artifact (built into the container) and must declare all modules as dependencies
- The `server` module contains all YAML configuration files (`application*.yml`)
- Each module under `modules/` declares only the sibling modules it directly imports classes from

### Module types

- `*-entity` — JPA entities, repositories, services for a domain
- `*-service` — business logic and orchestration
- `*-handler`, `*-executor`, `*-worker` — event/signal processors and executors

### Creating modules

- Add to `modules/pom.xml` `<modules>` list and `server/pom.xml` dependencies
- Package: `sh.byv.<domain>.<component>` (e.g., `sh.byv.world.entity`)
- Use `${project.version}` for inter-module dependency versions

## Module dependency graph

- See [`MODULES.md`](MODULES.md) for the full mermaid dependency graph of all modules

## Useful commands

- `./mvnw compile` — compile all modules
- `./mvnw test` — run unit tests
- `./mvnw test -pl modules/<name>` — run tests for a specific module
- `./mvnw quarkus:dev -pl server` — start in dev mode with live reload
- `./mvnw package -DskipTests` — build without running tests
- `./mvnw clean install` — full clean build with tests

## Code conventions

### Java

#### Package structure

- Colocate related files (entity, repository, service, DTO, resource) in one package

#### Code style

- Initialize fields in constructors, not inline
- `final var` for local variables, `final` for method arguments and class fields
- Package-private over `private` for non-public methods
- Repository classes must be package-private (no `public` modifier)
- Parameterized logging: `log.info("Message: {}", value)`
- Prefer Java Stream API over `for`/`while` loops for collection iteration and transformation

#### Annotations

- `@AllArgsConstructor` for constructor injection (Lombok)
- `@Slf4j` for logging (Lombok)
- `@ApplicationScoped` on services and repositories (Jakarta)
- `@Transactional` on service classes that write data (Jakarta)
- `@ConfigMapping(prefix = "...")` for typed configuration interfaces (Quarkus)

#### Error handling

- Throw exceptions from `exception-clazz` module: `NotFoundException`, `BadRequestException`, `ConflictException`,
  `ForbiddenException`, `UnauthorizedException`
- Throw from service layer, not repositories
- Message format: `"Entity not found: " + id`

#### Logging levels

- `debug` — detailed diagnostics
- `info` — significant events (auth, state changes)
- `warn` — recoverable issues (invalid input, failed ops)
- `error` — unexpected failures, exceptions

#### Enum IDs

- Enums with database-stored values use sequential integer IDs: `PENDING(1), ACTIVE(2)`
- Provide `@Converter(autoApply = true)` for JPA and `fromId(int)` static method

### SQL

- Lowercase everything (tables, columns, keywords)
- `create table if not exists`
- Primary keys: `bigint generated always as identity`
- Prefer `not null`
- Foreign keys: `on delete cascade on update restrict`, defined as table constraints
- Timestamps: `timestamp with time zone`
- Opening `(` on the same line as `create table`
- Table names prefixed with `omgc_`

### Flyway migrations

- Location: `server/src/main/resources/db/migration/`
- Naming: `V<major>.<minor>.<patch>.<seq>__<description>.sql` (e.g., `V0.1.0.1__omgc_tables.sql`)

### Configuration

- Custom properties under `omgcluster:` namespace in `application.yml`
- Kebab-case keys
- Use `@ConfigMapping` interfaces, not `@ConfigProperty`

### Commit messages

- Format: `<Action> <what changed> (#<PR>)`
- Actions: `Add`, `Remove`, `Rename`, `Extract`, `Split`, `Fix`
