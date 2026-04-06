package sh.byv.group;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sh.byv.event.EntityStatus;
import sh.byv.event.EventEntity;
import sh.byv.event.EventHandler;
import sh.byv.event.EventType;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
public class GroupCreated implements EventHandler {

    final GroupService groups;
    final GroupCreated proxy;

    @Override
    public EventType getType() {
        return EventType.GROUP_CREATED;
    }

    @Override
    public void execute(final EventEntity event) {
        proxy.handle(event.getEntityId());
    }

    @Transactional
    public void handle(final Long groupId) {
        final var group = groups.getByIdRequired(groupId);
        if (group.getStatus() == EntityStatus.PENDING) {
            group.setStatus(EntityStatus.CREATED);
        }
    }
}