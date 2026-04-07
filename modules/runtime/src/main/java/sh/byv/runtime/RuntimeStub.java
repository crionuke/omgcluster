package sh.byv.runtime;

import io.quarkus.arc.DefaultBean;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@DefaultBean
@ApplicationScoped
@AllArgsConstructor
public class RuntimeStub implements RuntimeService {

    @Override
    public void init(final RuntimeContext runtime, final int version) {
        if (version == 1) {
            final var world = runtime.newWorld("stub_world");

            world.newLayer("lobby_layer")
                    .newZone(0, 0, 1024, 1024)
                    .newSim("lobby_sim");

            world.newLayer("game_layer")
                    .newZone(0, 0, 1024, 1024)
                    .newSim("game_sim");
        }
    }
}
