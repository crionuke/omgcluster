package sh.byv.command;

import com.fasterxml.jackson.databind.JsonNode;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import sh.byv.server.ServerEntity;

import java.time.OffsetDateTime;
import java.util.List;

@ApplicationScoped
public class CommandRepository implements PanacheRepository<CommandEntity> {

    public CommandEntity create(final Long serverId,
                                final CommandType type,
                                final JsonNode body) {
        final var command = new CommandEntity();
        command.setCreatedAt(OffsetDateTime.now());
        command.setUpdatedAt(OffsetDateTime.now());
        command.setType(type);
        command.setServerId(serverId);
        command.setBody(body);
        command.setStatus(CommandStatus.PENDING);
        persist(command);
        return command;
    }

    public CommandEntity process(final CommandEntity command) {
        command.setUpdatedAt(OffsetDateTime.now());
        command.setStatus(CommandStatus.PROCESSED);
        return command;
    }

    public CommandEntity fail(final CommandEntity command) {
        command.setUpdatedAt(OffsetDateTime.now());
        command.setStatus(CommandStatus.FAILED);
        return command;
    }

    public List<CommandEntity> findByServerAndStatus(final ServerEntity server,
                                                     final CommandStatus status,
                                                     final int limit) {
        return find("status = ?1 and serverId = ?2 order by createdAt asc", status, server.getId())
                .page(0, limit)
                .list();
    }
}
