package sh.byv.event.entity;

public interface EventHandler {
    EventType getType();

    void execute(EventEntity event);
}
