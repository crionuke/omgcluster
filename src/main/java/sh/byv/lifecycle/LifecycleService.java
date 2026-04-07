package sh.byv.lifecycle;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.control.ActivateRequestContext;
import jakarta.enterprise.event.Observes;
import lombok.AllArgsConstructor;
import org.quartz.SchedulerException;
import sh.byv.command.CommandService;
import sh.byv.instance.InstanceService;
import sh.byv.job.JobService;
import sh.byv.mdc.WithMdcId;

@ApplicationScoped
@AllArgsConstructor
public class LifecycleService {

    final InstanceService instances;
    final CommandService commands;
    final JobService jobs;

    @WithMdcId
    public void onStart(@Observes final StartupEvent event) throws SchedulerException {
        instances.createInstance();
        commands.startWorker();
        jobs.startJobs();

        instances.startInstance();
    }
}
