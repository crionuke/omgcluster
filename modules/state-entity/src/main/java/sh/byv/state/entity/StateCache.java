package sh.byv.state.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.hash.HashCommands;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;
import sh.byv.server.entity.ServerConfig;
import sh.byv.server.entity.ServerEntity;
import sh.byv.server.entity.ServerService;

import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;
import java.util.Map;
import java.util.Optional;

@Slf4j
@ApplicationScoped
public class StateCache {

    private static String getKey(final String name) {
        return "omgc:server:%s:state".formatted(name);
    }

    private static final String VERSION_FIELD = "version";
    private static final String BODY_FIELD = "body";

    private final ServerService servers;
    private final StateService states;
    private final ServerConfig config;

    private final HashCommands<String, String, String> cache;
    private final String thisServerStateKey;
    private final ObjectMapper objectMapper;

    private CachedState inMemoryState;

    public StateCache(final ServerService servers,
                      final StateService states,
                      final ServerConfig config,
                      final RedisDataSource redis,
                      final ObjectMapper objectMapper) {
        this.servers = servers;
        this.states = states;
        this.config = config;
        this.objectMapper = objectMapper;

        this.cache = redis.hash(String.class, String.class, String.class);
        this.thisServerStateKey = getKey(config.name());

        inMemoryState = null;
    }

    public synchronized Optional<StateBody> getServerState() {
        final var cachedVersion = cache.hget(thisServerStateKey, VERSION_FIELD);
        if (cachedVersion == null) {
            log.info("No cached state found, loading persisted state");
            return Optional.ofNullable(loadPersistedState());
        }

        try {
            final var parsedVersion = OffsetDateTime.parse(cachedVersion);
            if (inMemoryState == null || parsedVersion.isAfter(inMemoryState.version())) {
                log.info("Local state is stale or missing, loading cached state");
                return Optional.ofNullable(loadCachedState());
            }
        } catch (DateTimeParseException e) {
            log.warn("Failed to parse cached version, fallback to persisted state: {}", e.getMessage(), e);
            return Optional.ofNullable(loadPersistedState());
        }

        log.debug("Returning in-memory state with version: {}", inMemoryState.version());
        return Optional.of(inMemoryState.body());
    }

    synchronized void cacheServerState(final ServerEntity server, final StateEntity entity) {
        final var version = entity.getUpdatedAt();
        final var body = entity.getBody();
        final var key = getKey(server.getName());

        cache.hset(key, Map.of(VERSION_FIELD, version.toString(), BODY_FIELD, serializeBody(body)));

        if (server.getName().equals(config.name())) {
            inMemoryState = new CachedState(version, body);
        }
    }

    private StateBody loadPersistedState() {
        final var persistedState = getPersistedState();
        if (persistedState == null) {
            return null;
        }

        final var version = persistedState.getUpdatedAt();
        final var body = persistedState.getBody();
        cache.hset(thisServerStateKey, Map.of(VERSION_FIELD, version.toString(), BODY_FIELD, serializeBody(body)));
        inMemoryState = new CachedState(version, body);
        return body;
    }

    private StateBody loadCachedState() {
        final var fields = cache.hgetall(thisServerStateKey);
        try {
            final var version = OffsetDateTime.parse(fields.get(VERSION_FIELD));
            final var body = deserializeBody(fields.get(BODY_FIELD));
            inMemoryState = new CachedState(version, body);
            return body;
        } catch (final Exception e) {
            log.warn("Failed to load cached state, fallback to persisted state: {}", e.getMessage(), e);
            return loadPersistedState();
        }
    }

    private StateEntity getPersistedState() {
        return servers.getByNameOptional(config.name())
                .flatMap(states::getByServerOptional)
                .orElse(null);
    }

    private String serializeBody(final StateBody body) {
        try {
            return objectMapper.writeValueAsString(body);
        } catch (final JsonProcessingException e) {
            throw new IllegalStateException("Failed to serialize state body", e);
        }
    }

    private StateBody deserializeBody(final String json) {
        try {
            return objectMapper.readValue(json, StateBody.class);
        } catch (final JsonProcessingException e) {
            throw new IllegalStateException("Failed to deserialize state body", e);
        }
    }

    private record CachedState(OffsetDateTime version, StateBody body) {
    }
}