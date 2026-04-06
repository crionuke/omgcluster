package sh.byv.server;

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
public class ServerLifecycle {

    final ServerService servers;
    final ServerConfig config;

    @WithMdcId
    public void onStart(@Observes @Priority(1) final StartupEvent event) {
        final var internalAddress = config.address().internal();
        final var externalAddress = config.address().external();

        log.info("Start server {}", internalAddress);

        servers.getOrCreate(internalAddress, externalAddress);
    }
}
