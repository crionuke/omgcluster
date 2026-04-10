package sh.byv.handler.event;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sh.byv.event.EventEntity;
import sh.byv.event.EventHandler;
import sh.byv.event.EventType;
import sh.byv.instance.InstanceService;
import sh.byv.instance.InstanceStatus;
import sh.byv.state.StateService;
import sh.byv.state.StateType;

@Slf4j
@Transactional
@ApplicationScoped
@AllArgsConstructor
public class InstanceCreated implements EventHandler {

    final InstanceService instances;
    final StateService state;

    @Override
    public EventType getType() {
        return EventType.INSTANCE_CREATED;
    }

    @Override
    public void execute(final EventEntity event) {
        final var instance = instances.getByIdRequired(event.getEntityId());
        if (instance.getStatus() == InstanceStatus.PENDING) {
            state.create(instance, StateType.INSTANCE);

            instances.activate(instance);
        }
    }
}
