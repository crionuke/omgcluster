package sh.byv.runtime.service;

public interface RuntimeService {

    void migrate(MigrationContext context);

    Object initialize(InitialiationContext context);

    Object simulate(SimulationContext context);

    Object aggregate(AggregationContext context);
}
