package sh.byv.task.executor;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
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
        defaultExecutor.execute(() -> {
            try {
                task.run();
            } catch (final Exception e) {
                log.error("Task execution failed", e);
            }
        });
    }

}
