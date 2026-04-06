package sh.byv.command;

public interface CommandHandler {
    CommandType getType();

    void execute(CommandEntity command);
}
