package sh.byv.init;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sh.byv.cluster.ClusterContext;
import sh.byv.cluster.ClusterService;
import sh.byv.prop.PropService;
import sh.byv.prop.PropType;

import java.util.stream.IntStream;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
public class InitService {

    final ClusterService clusterService;
    final ClusterContext clusterContext;
    final InitService serviceProxy;
    final PropService propService;
    final InitConfig initConfig;

    public void initToLatest() {
        final var fromVersion = propService.getInt(PropType.CLUSTER_VERSION);
        final var toVersion = initConfig.version();

        IntStream.range(fromVersion, toVersion)
                .forEach(version -> serviceProxy.initToVersion(version + 1));
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void initToVersion(final int version) {
        log.info("Init version {}", version);
        clusterService.init(clusterContext, version);
        propService.setInt(PropType.CLUSTER_VERSION, version);
    }
}