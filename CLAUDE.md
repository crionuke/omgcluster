# OMGCLUSTER

## Rules

- Never add Co-Authored-By lines to git commit messages
- Keep `MODULES.md` in sync with module dependencies: whenever a module is added, removed, or its inter-module
  dependencies change, update the mermaid graph in `MODULES.md` accordingly

## Project structure

- The `server` module is the deployable artifact (built into the container) and must declare all modules as dependencies
- The `server` module contains all YAML configuration files (`application*.yml`)
- Each module under `modules/` declares only the sibling modules it directly imports classes from

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
- Parameterized logging: `log.info("Message: {}", value)`

#### Lombok

- `@AllArgsConstructor` for constructor injection
- `@Slf4j` for logging

#### Logging levels

- `debug` — detailed diagnostics
- `info` — significant events (auth, state changes)
- `warn` — recoverable issues (invalid input, failed ops)
- `error` — unexpected failures, exceptions

### SQL

- Lowercase everything (tables, columns, keywords)
- `create table if not exists`
- Primary keys: `bigint generated always as identity`
- Prefer `not null`
- Foreign keys: `on delete cascade on update restrict`, defined as table constraints
- Timestamps: `timestamp with time zone`
- Opening `(` on the same line as `create table`
