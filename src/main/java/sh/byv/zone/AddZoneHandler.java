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
public class AddZoneHandler implements CommandHandler {

    final StateService state;
    final ZoneService zones;

    @Override
    public CommandType getType() {
        return CommandType.ADD_ZONE;
    }

    @Override
    public void execute(final CommandEntity command) {
        final var zoneId = command.getBody().asLong();
        final var zone = zones.getByIdRequired(zoneId);
        state.addZone(zone);
    }
}