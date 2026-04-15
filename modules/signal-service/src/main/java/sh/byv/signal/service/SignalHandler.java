package sh.byv.signal.service;

public interface SignalHandler {

    SignalType getType();

    void execute(SignalBody signal);
}