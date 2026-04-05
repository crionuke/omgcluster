package sh.byv.init;

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
public class InitJob implements JobExecutor {

    final InitService initService;
    final JobService jobService;

    @Override
    public JobType getType() {
        return JobType.INIT;
    }

    @Override
    public void execute() {
        initService.initToLatest();
    }

    void onStart(@Observes final StartupEvent event) throws SchedulerException {
        jobService.request(getType());
    }
}
