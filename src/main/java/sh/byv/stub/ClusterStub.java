package sh.byv.stub;

import io.quarkus.arc.DefaultBean;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sh.byv.cluster.ClusterService;
import sh.byv.context.ClusterContext;

@Slf4j
@DefaultBean
@ApplicationScoped
@AllArgsConstructor
public class ClusterStub implements ClusterService {

    @Override
    public void init(final ClusterContext cluster, final int version) {
        if (version == 1) {
            final var world = cluster.newWorld("stub_world");

            world.newLayer("lobby_layer")
                    .newZone(0, 0, 1024, 1024)
                    .newSim("lobby_sim");

            world.newLayer("game_layer")
                    .newZone(0, 0, 1024, 1024)
                    .newSim("game_sim");
        }
    }
}
