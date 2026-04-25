package sh.byv.init;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sh.byv.runtime.service.RuntimeContext;
import sh.byv.runtime.service.RuntimeService;
import sh.byv.prop.entity.PropService;
import sh.byv.prop.entity.PropType;

import java.util.stream.IntStream;

@Slf4j
@Transactional
@ApplicationScoped
@AllArgsConstructor
public class InitService {

    final RuntimeService runtimeService;
    final RuntimeContext runtimeContext;
    final InitService serviceProxy;
    final PropService propService;
    final InitConfig initConfig;

    public void initToLatest() {
        final var fromVersion = propService.getInt(PropType.RUNTIME_VERSION);
        final var toVersion = initConfig.version();

        IntStream.range(fromVersion, toVersion)
                .forEach(version -> serviceProxy.migrateToVersion(version + 1));
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void migrateToVersion(final int version) {
        log.info("Migrate to version {}", version);
        runtimeService.migrateCluster(runtimeContext, version);
        propService.setInt(PropType.RUNTIME_VERSION, version);
    }
}