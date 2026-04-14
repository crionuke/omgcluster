package sh.byv.event.handler;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sh.byv.event.EventEntity;
import sh.byv.event.EventHandler;
import sh.byv.event.EventType;
import sh.byv.node.NodeService;
import sh.byv.node.NodeStatus;
import sh.byv.state.StateService;

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
