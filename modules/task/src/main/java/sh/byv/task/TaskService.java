package sh.byv.task;

import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.hash.HashCommands;
import io.quarkus.redis.datasource.keys.KeyCommands;
import io.quarkus.redis.datasource.list.ListCommands;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

@Slf4j
@ApplicationScoped
public class TaskService {

    final Map<TaskType, TaskRunner> runners;
    final ExecutorService executor;

    final HashCommands<String, String, TaskResult> results;
    final ListCommands<String, TaskItem> tasks;
    final KeyCommands<String> keys;

    public TaskService(final Instance<TaskRunner> instances,
                       final RedisDataSource redis) {
        runners = new ConcurrentHashMap<>();
        instances.stream().forEach(taskRunner -> {
            final var type = taskRunner.getType();
            runners.put(type, taskRunner);
        });

        log.info("Registered task runners, {}", runners.keySet());
        this.executor = Executors.newFixedThreadPool(4);

        tasks = redis.list(TaskItem.class);
        results = redis.hash(TaskResult.class);
        keys = redis.key();
    }

    public void start() {
        IntStream.range(0, 4)
                .forEach(_ -> executor.submit(() -> {
                    while (!Thread.currentThread().isInterrupted()) {
                        try {
                            final var task = popTask();
                            if (task == null) {
                                continue;
                            }

                            final var type = task.getType();
                            final var runner = runners.get(type);
                            if (runner == null) {
                                log.error("No runner found for task {} ", type);
                                continue;
                            }

                            executeRunner(runner, task);

                        } catch (Exception e) {
                            log.error("Failed to execute task", e);
                        }
                    }
                }));
    }

    public void shutdown() {
        log.info("Stop task executor");
        executor.shutdownNow();
    }

    public void pushTask(final TaskItem task) {
        tasks.rpush("cluster:tasks", task);
    }


    TaskItem popTask() {
        final var kv = tasks.blpop(Duration.ofSeconds(1), "cluster:tasks");
        return kv != null ? kv.value() : null;
    }

    void executeRunner(final TaskRunner runner, final TaskItem task) {
        final TaskResult result;
        try {
            result = runner.execute(task);
        } catch (Exception e) {
            log.error("Failed to run task", e);
            return;
        }

        if (result != null) {
            final var resultKey = task.getResultKey();
            setResult(resultKey.getMap(), resultKey.getField(), result);
        }
    }

    public void setResult(final String key, final String field, final TaskResult result) {
        results.hset(key, field, result);
        keys.expire(key, Duration.ofSeconds(5));
    }
}
