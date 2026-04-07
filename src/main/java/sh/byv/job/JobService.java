package sh.byv.job;

import io.quarkus.runtime.Startup;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.control.ActivateRequestContext;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.ObjectAlreadyExistsException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import sh.byv.mdc.WithMdcId;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Startup
@ApplicationScoped
public class JobService {

    final Map<JobType, JobExecutor> executors;
    final Scheduler scheduler;

    public JobService(final Scheduler scheduler, final Instance<JobExecutor> instances) {
        this.scheduler = scheduler;

        executors = new ConcurrentHashMap<>();
        instances.stream().forEach(jobExecutor -> {
            final var type = jobExecutor.getType();
            executors.put(type, jobExecutor);
        });

        log.info("Registered job executors, {}", executors.keySet());
    }

    public void startJobs() throws SchedulerException {
        iterate(JobType.EVENT);
    }

    public void iterate(final JobType type) throws SchedulerException {
        final var trigger = TriggerBuilder.newTrigger()
                .withIdentity(type.name())
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInSeconds(type.getInterval())
                        .withMisfireHandlingInstructionIgnoreMisfires()
                        .repeatForever())
                .build();

        scheduleJob(type, trigger);
    }

    void scheduleJob(final JobType type, final Trigger trigger) throws SchedulerException {
        final var name = type.name();

        final var job = JobBuilder.newJob(QuartzJob.class)
                .withIdentity(name)
                .build();

        try {
            scheduler.scheduleJob(job, trigger);
            log.info("Job {} scheduled", name);
        } catch (ObjectAlreadyExistsException e) {
            log.info("Job {} already exists, skipping", name);
        }
    }

    @WithMdcId
    @ActivateRequestContext
    void executeJob(final JobType type) {
        final var executor = executors.get(type);
        if (Objects.nonNull(executor)) {
            log.debug("Executing {}", type);
            executor.execute();
        } else {
            log.error("No executor found for {} job", type);
        }
    }

    @DisallowConcurrentExecution
    public static class QuartzJob implements Job {

        @Inject
        JobService jobs;

        public void execute(JobExecutionContext context) throws JobExecutionException {
            final var name = context.getJobDetail().getKey().getName();
            final var type = JobType.valueOf(name);

            jobs.executeJob(type);
        }
    }
}
