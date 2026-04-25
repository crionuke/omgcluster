package sh.byv.runtime.service;

import java.util.List;

public interface RuntimeService {

    void migrateCluster(RuntimeContext runtime, int version);

    Object simulateZone(long tick, String sim, Object zoneState);

    Object initZone();

    Object computeZone(Object prevZoneState, List<Object> simStates, long tick);
}
