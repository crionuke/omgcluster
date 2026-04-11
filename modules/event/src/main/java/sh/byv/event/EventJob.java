package sh.byv.event;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sh.byv.job.JobExecutor;
import sh.byv.job.JobType;

@Slf4j
@Transactional
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
        events.getPendingEvents().forEach(events::handle);
    }
}
