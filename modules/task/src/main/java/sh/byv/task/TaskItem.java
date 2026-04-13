package sh.byv.task;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskItem {

    TaskType type;
    MapField resultKey;
    Simulation simulation;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Simulation {

        Long zoneId;
        Long tickId;
        String name;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MapField {

        String map;
        String field;
    }
}
