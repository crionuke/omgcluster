package sh.byv.command;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.control.ActivateRequestContext;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sh.byv.instance.InstanceConfig;
import sh.byv.instance.InstanceService;
import sh.byv.mdc.WithMdcId;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
public class CommandWorker {

    final CommandService commands;
    final InstanceService instances;
    final InstanceConfig config;

    @WithMdcId
    public void execute() {
        final var instance = instances.getByNameRequired(config.name());
        final var pending = commands.getPendingCommands(instance);
        pending.forEach(command -> commands.process(command.getId()));
    }
}
