package sh.byv.job.worker;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sh.byv.event.EventService;
import sh.byv.job.JobWorker;
import sh.byv.job.JobType;

@Slf4j
@Transactional
@ApplicationScoped
@AllArgsConstructor
public class EventWorker implements JobWorker {

    final EventService events;

    @Override
    public JobType getType() {
        return JobType.EVENT;
    }

    @Override
    public void execute() {
        events.getPendingEvents().forEach(events::handle);
    }

    @Override
    public void execute(final Long entityId) {
        throw new UnsupportedOperationException("Not supported");
    }
}
