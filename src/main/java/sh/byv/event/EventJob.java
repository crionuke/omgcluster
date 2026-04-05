package sh.byv.event;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.SchedulerException;
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

        final var events = eventService.fetchPending();
        events.forEach(event -> eventService.process(event.getId()));
    }

    void onStart(@Observes final StartupEvent event) throws SchedulerException {
        jobService.iterate(getType());
    }
}
