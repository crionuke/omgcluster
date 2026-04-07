package sh.byv.init;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sh.byv.runtime.RuntimeContext;
import sh.byv.runtime.RuntimeService;
import sh.byv.prop.PropService;
import sh.byv.prop.PropType;

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

    @Transactional(Transactional.TxType.SUPPORTS)
    public void initToLatest() {
        final var fromVersion = propService.getInt(PropType.RUNTIME_VERSION);
        final var toVersion = initConfig.version();

        IntStream.range(fromVersion, toVersion)
                .forEach(version -> serviceProxy.initToVersion(version + 1));
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void initToVersion(final int version) {
        log.info("Init version {}", version);
        runtimeService.init(runtimeContext, version);
        propService.setInt(PropType.RUNTIME_VERSION, version);
    }
}