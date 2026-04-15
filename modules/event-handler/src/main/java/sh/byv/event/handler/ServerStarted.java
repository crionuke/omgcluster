package sh.byv.event.handler;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sh.byv.event.entity.EventEntity;
import sh.byv.event.entity.EventHandler;
import sh.byv.event.entity.EventType;
import sh.byv.init.InitService;

@Slf4j
@Transactional
@ApplicationScoped
@AllArgsConstructor
public class ServerStarted implements EventHandler {

    final InitService init;

    @Override
    public EventType getType() {
        return EventType.SERVER_STARTED;
    }

    @Override
    public void execute(final EventEntity event) {
        init.initToLatest();
    }
}
