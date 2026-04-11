# OMGCLUSTER

## Rules

- Never add Co-Authored-By lines to git commit messages
- Keep `MODULES.md` in sync with module dependencies: whenever a module is added, removed, or its inter-module
  dependencies change, update the mermaid graph in `MODULES.md` accordingly

## Code conventions

### Java

#### Package structure

- Colocate related files (entity, repository, service, DTO, resource) in one package

#### Code style

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
