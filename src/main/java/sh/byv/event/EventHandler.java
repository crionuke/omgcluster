package sh.byv.event;

public interface EventHandler {
    EventType getType();

    void execute(EventEntity event);
}
