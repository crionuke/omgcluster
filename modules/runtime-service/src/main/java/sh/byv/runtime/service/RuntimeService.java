package sh.byv.runtime.service;

import sh.byv.runtime.context.AggregationContext;
import sh.byv.runtime.context.ZoneCreatedContext;
import sh.byv.runtime.context.MigrationContext;
import sh.byv.runtime.context.SimulationContext;
import sh.byv.sim.result.SimResult;
import sh.byv.zone.state.ZoneState;

public interface RuntimeService {

    void onMigration(MigrationContext context);

    ZoneState onZoneCreated(ZoneCreatedContext context);

    SimResult onSimulation(SimulationContext context);

    ZoneState onAggregation(AggregationContext context);
}
