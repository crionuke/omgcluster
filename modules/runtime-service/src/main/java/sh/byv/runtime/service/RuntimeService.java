package sh.byv.runtime.service;

import java.util.List;

public interface RuntimeService {

    void init(RuntimeContext runtime, int version);

    Object simulate(long tick, String sim, Object zoneState);

    Object aggregate(Object prevZoneState, List<Object> simStates, long tick);
}
