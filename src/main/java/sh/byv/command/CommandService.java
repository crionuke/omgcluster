package sh.byv.command;

import com.fasterxml.jackson.databind.JsonNode;
import io.quarkus.runtime.Startup;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import sh.byv.server.ServerEntity;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Startup
@ApplicationScoped
public class CommandService {

    final Map<CommandType, CommandHandler> handlers;
    final CommandRepository repository;

    CommandService(final CommandRepository repository, final Instance<CommandHandler> handlers) {
        this.repository = repository;

        this.handlers = new ConcurrentHashMap<>();
        handlers.stream().forEach(handler -> {
            final var type = handler.getType();
            this.handlers.put(type, handler);
        });

        log.info("Registered command handlers, {}", this.handlers.keySet());
    }

    public CommandEntity create(final Long serverId, final CommandType type, final JsonNode body) {
        return repository.create(serverId, type, body);
    }

    public List<CommandEntity> getPendingCommands(final ServerEntity server) {
        return repository.findByServerAndStatus(server, CommandStatus.PENDING, 100);
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void process(final Long id) {
        final var command = repository.findById(id);
        final var handler = handlers.get(command.getType());

        if (Objects.isNull(handler)) {
            log.error("No handler found for command {}, type={}", id, command.getType());
            repository.fail(command);
            return;
        }

        try {
            log.info("Processing {} command, serverId={}", command.getType(), command.getServerId());
            handler.execute(command);
            repository.process(command);
        } catch (Exception e) {
            log.error("Failed to process command {}, type={}", id, command.getType(), e);
            repository.fail(command);
        }
    }
}
