package sh.byv.runtime.service;

import sh.byv.runtime.context.AggregationContext;
import sh.byv.runtime.context.InitialiationContext;
import sh.byv.runtime.context.MigrationContext;
import sh.byv.runtime.context.SimulationContext;
import sh.byv.sim.result.SimResult;
import sh.byv.zone.state.ZoneState;

public interface RuntimeService {

    void migrate(MigrationContext context);

    ZoneState initialize(InitialiationContext context);

    SimResult simulate(SimulationContext context);

    ZoneState aggregate(AggregationContext context);
}
