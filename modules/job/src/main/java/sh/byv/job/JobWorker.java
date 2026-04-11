package sh.byv.job;

public interface JobWorker {
    JobType getType();

    void execute();

    void execute(Long entityId);
}
