package sh.byv.zone;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sh.byv.command.CommandEntity;
import sh.byv.command.CommandHandler;
import sh.byv.command.CommandType;
import sh.byv.state.StateService;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
public class RemoveZoneHandler implements CommandHandler {

    final StateService state;

    @Override
    public CommandType getType() {
        return CommandType.REMOVE_ZONE;
    }

    @Override
    public void execute(final CommandEntity command) {
        final var zoneId = command.getBody().asLong();
        state.removeZone(zoneId);
    }
}