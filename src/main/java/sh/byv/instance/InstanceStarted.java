package sh.byv.instance;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sh.byv.event.EventEntity;
import sh.byv.event.EventHandler;
import sh.byv.event.EventType;
import sh.byv.init.InitService;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
public class InstanceStarted implements EventHandler {

    final InstanceStarted proxy;
    final InitService init;

    @Override
    public EventType getType() {
        return EventType.INSTANCE_STARTED;
    }

    @Override
    public void execute(final EventEntity event) {
        proxy.handle(event.getEntityId());
    }

    @Transactional
    public void handle(final Long instanceId) {
        init.initToLatest();
    }
}
