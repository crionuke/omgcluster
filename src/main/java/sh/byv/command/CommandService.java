package sh.byv.command;

import com.fasterxml.jackson.databind.JsonNode;
import io.quarkus.runtime.Startup;
import io.quarkus.scheduler.Scheduled;
import io.quarkus.scheduler.Scheduler;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import sh.byv.instance.InstanceEntity;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Startup
@ApplicationScoped
public class CommandService {

    final CommandRepository repository;
    final CommandWorker worker;
    final Scheduler scheduler;

    final Map<CommandType, CommandHandler> handlers;

    CommandService(final CommandRepository repository,
                   final CommandWorker worker,
                   final Scheduler scheduler,
                   final Instance<CommandHandler> instances) {
        this.repository = repository;
        this.scheduler = scheduler;
        this.worker = worker;

        handlers = new ConcurrentHashMap<>();
        instances.stream().forEach(instance -> {
            final var type = instance.getType();
            handlers.put(type, instance);
        });

        log.info("Registered command handlers, {}", handlers.keySet());
    }

    public void startWorker() {
        final var trigger = scheduler.newJob("command-worker")
                .setConcurrentExecution(Scheduled.ConcurrentExecution.SKIP)
                .setInterval("1s")
                .setExecuteWith(Scheduled.SIMPLE)
                .setTask(_ -> worker.execute())
                .schedule();

        log.info("Command worker scheduled {}", trigger);
    }

    public CommandEntity create(final Long instanceId, final CommandType type, final JsonNode body) {
        return repository.create(instanceId, type, body);
    }

    @Transactional
    public List<CommandEntity> getPendingCommands(final InstanceEntity instance) {
        return repository.findByInstanceAndStatus(instance, CommandStatus.PENDING, 100);
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
            log.info("Processing {} command, instanceId={}", command.getType(), command.getInstanceId());
            handler.execute(command);
            repository.process(command);
        } catch (Exception e) {
            log.error("Failed to process command {}, type={}", id, command.getType(), e);
            repository.fail(command);
        }
    }
}
