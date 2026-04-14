package sh.byv.job.service;

public interface JobWorker {
    JobType getType();

    void execute();

    void execute(Long entityId);
}
