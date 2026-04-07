package sh.byv.job;

public interface JobExecutor {
    JobType getType();

    void execute();
}
