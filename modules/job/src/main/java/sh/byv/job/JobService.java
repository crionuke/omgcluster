package sh.byv.job;

import io.quarkus.runtime.Startup;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.control.ActivateRequestContext;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
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
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Startup
@ApplicationScoped
public class JobService {

    private static final String ENTITY_ID_JOB_PROPERTY = "entityId";
    private static final String TYPE_JOB_PROPERTY = "type";

    final Map<JobType, JobWorker> executors;
    final Scheduler scheduler;

    public JobService(final Scheduler scheduler, final Instance<JobWorker> instances) {
        this.scheduler = scheduler;

        executors = new ConcurrentHashMap<>();
        instances.stream().forEach(jobWorker -> {
            final var type = jobWorker.getType();
            executors.put(type, jobWorker);
        });

        log.info("Registered job executors, {}", executors.keySet());
    }

    @SneakyThrows
    public void start() {
        schedule(JobType.EVENT);
    }

    @SneakyThrows
    public void schedule(final JobType type) {
        schedule(type, null);
    }

    @SneakyThrows
    public void schedule(final JobType type, final Long entityId) {
        final String identity;
        final JobDetail job;

        if (entityId != null) {
            identity = type.formatIdentity(entityId);

            job = JobBuilder.newJob(EntityJob.class)
                    .withIdentity(identity)
                    .usingJobData(TYPE_JOB_PROPERTY, type.name())
                    .usingJobData(ENTITY_ID_JOB_PROPERTY, entityId.toString())
                    .build();
        } else {
            identity = type.getIdentity();

            job = JobBuilder.newJob(BackgroundJob.class)
                    .withIdentity(identity)
                    .usingJobData(TYPE_JOB_PROPERTY, type.name())
                    .build();
        }

        final var trigger = TriggerBuilder.newTrigger()
                .withIdentity(identity)
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInSeconds(type.getInterval())
                        .withMisfireHandlingInstructionIgnoreMisfires()
                        .repeatForever())
                .build();

        schedule(job, trigger);
    }

    @SneakyThrows
    void schedule(final JobDetail job, final Trigger trigger) {
        final var name = job.getKey().getName();

        try {
            scheduler.scheduleJob(job, trigger);
            log.info("Job {} scheduled", name);
        } catch (ObjectAlreadyExistsException e) {
            log.info("Job {} already exists, skipping", name);
        }
    }

    @WithMdcId
    @ActivateRequestContext
    void execute(final JobType type) {
        execute(type, null);
    }

    @WithMdcId
    @ActivateRequestContext
    void execute(final JobType type, final Long entityId) {
        final var executor = executors.get(type);
        if (executor != null) {
            if (entityId != null) {
                log.debug("Executing {} for entity {}", type, entityId);
                executor.execute(entityId);
            } else {
                log.debug("Executing {}", type);
                executor.execute();
            }
        } else {
            log.error("No executor found for {} job", type);
        }
    }

    @DisallowConcurrentExecution
    public static class BackgroundJob implements Job {

        @Inject
        JobService jobs;

        public void execute(final JobExecutionContext context) throws JobExecutionException {
            final var typeProperty = context.getMergedJobDataMap().getString(TYPE_JOB_PROPERTY);
            final var type = JobType.valueOf(typeProperty);

            jobs.execute(type);
        }
    }

    @DisallowConcurrentExecution
    public static class EntityJob implements Job {

        @Inject
        JobService jobs;

        public void execute(final JobExecutionContext context) throws JobExecutionException {
            final var typeProperty = context.getMergedJobDataMap().getString(TYPE_JOB_PROPERTY);
            final var type = JobType.valueOf(typeProperty);

            final var entityIdProperty = context.getMergedJobDataMap().getString(ENTITY_ID_JOB_PROPERTY);
            final var entityId = Long.valueOf(entityIdProperty);

            jobs.execute(type, entityId);
        }
    }
}
