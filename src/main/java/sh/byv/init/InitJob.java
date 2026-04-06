package sh.byv.init;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sh.byv.job.JobExecutor;
import sh.byv.job.JobType;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
public class InitJob implements JobExecutor {

    final InitService initService;

    @Override
    public JobType getType() {
        return JobType.INIT;
    }

    @Override
    public void execute() {
        initService.initToLatest();
    }
}
