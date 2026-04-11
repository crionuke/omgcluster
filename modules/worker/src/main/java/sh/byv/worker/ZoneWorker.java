package sh.byv.worker;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sh.byv.job.JobWorker;
import sh.byv.job.JobType;
import sh.byv.runtime.RuntimeService;
import sh.byv.zone.ZoneService;

@Slf4j
@Transactional
@ApplicationScoped
@AllArgsConstructor
public class ZoneWorker implements JobWorker {

    final RuntimeService runtime;
    final ZoneService zones;

    @Override
    public JobType getType() {
        return JobType.ZONE;
    }

    @Override
    public void execute() {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public void execute(final Long entityId) {
        log.info("Executing zone job for {}", entityId);
        final var zone = zones.getByIdRequired(entityId);
    }
}
