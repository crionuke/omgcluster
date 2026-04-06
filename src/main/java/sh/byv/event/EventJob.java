package sh.byv.event;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sh.byv.job.JobExecutor;
import sh.byv.job.JobType;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
public class EventJob implements JobExecutor {

    final EventService events;

    @Override
    public JobType getType() {
        return JobType.EVENT;
    }

    @Override
    public void execute() {
        log.debug("Executing {}", getType());

        final var pending = events.getPendingEvents();
        pending.forEach(event -> events.process(event.getId()));
    }
}
