package sh.byv.task.executor;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@ApplicationScoped
public class TaskExecutor {

    final ExecutorService defaultExecutor;

    public TaskExecutor() {
        defaultExecutor = Executors.newCachedThreadPool();
    }

    public void shutdown() {
        defaultExecutor.shutdown();
    }

    public void execute(final Runnable task) {
        defaultExecutor.submit(task);
    }

}
