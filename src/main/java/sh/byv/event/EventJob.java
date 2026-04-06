package sh.byv.event;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sh.byv.job.JobExecutor;
import sh.byv.job.JobService;
import sh.byv.job.JobType;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
public class EventJob implements JobExecutor {

    final EventService eventService;
    final JobService jobService;

    @Override
    public JobType getType() {
        return JobType.EVENT;
    }

    @Override
    public void execute() {
        log.debug("Executing {}", getType());

        final var events = eventService.getPendingEvents();
        events.forEach(event -> eventService.process(event.getId()));
    }
}
