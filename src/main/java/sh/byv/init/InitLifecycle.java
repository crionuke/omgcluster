package sh.byv.init;

import io.quarkus.runtime.StartupEvent;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.SchedulerException;
import sh.byv.job.JobService;
import sh.byv.job.JobType;
import sh.byv.mdc.WithMdcId;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
public class InitLifecycle {

    final JobService jobs;

    @WithMdcId
    public void onStart(@Observes @Priority(2) final StartupEvent event) throws SchedulerException {
        jobs.request(JobType.INIT);
    }
}
