package sh.byv.sim;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;
import sh.byv.worker.WorkerRunner;

import java.time.Duration;

@Slf4j
@ApplicationScoped
public class SimWorker extends WorkerRunner {

    @Override
    public String getName() {
        return "sim";
    }

    @Override
    public Duration getInterval() {
        return Duration.ofMillis(1000);
    }

    @Override
    public void execute() {
        log.debug("Sim worker tick");
    }
}