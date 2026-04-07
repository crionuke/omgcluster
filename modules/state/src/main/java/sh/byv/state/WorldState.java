package sh.byv.state;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class WorldState {

    Set<Long> sims;
    Set<Long> zones;

    public WorldState() {
        sims = new HashSet<>();
        zones = new HashSet<>();
    }
}