package sh.byv.instance;

import io.quarkus.runtime.StartupEvent;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sh.byv.mdc.WithMdcId;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
public class InstanceLifecycle {

    final InstanceService instances;
    final InstanceConfig config;

    @WithMdcId
    public void onStart(@Observes @Priority(1) final StartupEvent event) {
        final var name = config.name();

        log.info("Start instance {}", name);

        instances.getOrCreate(name);
    }
}
