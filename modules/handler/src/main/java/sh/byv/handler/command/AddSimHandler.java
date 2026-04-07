package sh.byv.handler.command;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sh.byv.command.CommandEntity;
import sh.byv.command.CommandHandler;
import sh.byv.command.CommandType;
import sh.byv.sim.SimService;
import sh.byv.state.StateService;

@Slf4j
@Transactional
@ApplicationScoped
@AllArgsConstructor
public class AddSimHandler implements CommandHandler {

    final StateService state;
    final SimService sims;

    @Override
    public CommandType getType() {
        return CommandType.ADD_SIM;
    }

    @Override
    public void execute(final CommandEntity command) {
        final var instance = command.getInstance();
        final var simId = command.getBody().asLong();
        final var sim = sims.getByIdRequired(simId);
        state.addSim(instance, sim);
    }
}