package sh.byv.command;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sh.byv.server.ServerConfig;
import sh.byv.server.ServerService;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
public class CommandWorker {

    final CommandService commands;
    final ServerService servers;
    final ServerConfig config;

    public void execute() {
        final var server = servers.getByInternalAddressRequired(config.address().internal());
        final var pending = commands.getPendingCommands(server);
        pending.forEach(command -> commands.process(command.getId()));
    }
}
