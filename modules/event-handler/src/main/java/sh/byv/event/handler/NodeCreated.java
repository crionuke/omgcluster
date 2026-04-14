package sh.byv.event.handler;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sh.byv.event.entity.EventEntity;
import sh.byv.event.entity.EventHandler;
import sh.byv.event.entity.EventType;
import sh.byv.node.entity.NodeService;
import sh.byv.node.entity.NodeStatus;
import sh.byv.state.entity.StateService;

@Slf4j
@Transactional
@ApplicationScoped
@AllArgsConstructor
public class NodeCreated implements EventHandler {

    final NodeService nodes;
    final StateService state;

    @Override
    public EventType getType() {
        return EventType.NODE_CREATED;
    }

    @Override
    public void execute(final EventEntity event) {
        final var node = nodes.getByIdRequired(event.getEntityId());
        if (node.getStatus() == NodeStatus.PENDING) {
            state.create(node);

            nodes.activate(node);
        }
    }
}
