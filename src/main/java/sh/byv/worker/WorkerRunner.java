package sh.byv.worker;

import lombok.extern.slf4j.Slf4j;

import java.time.Duration;

@Slf4j
public abstract class WorkerRunner implements Runnable {

    public abstract String getName();

    public abstract Duration getInterval();

    public abstract void execute();

    @Override
    public void run() {
        try {
            execute();
        } catch (final Exception e) {
            log.error("Worker {} failed, {}", getName(), e.getMessage(), e);
        }
    }
}