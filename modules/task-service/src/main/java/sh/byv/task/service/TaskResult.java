package sh.byv.task.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskResult {

    Simulation simulation;

    @Data
    @NoArgsConstructor
    public static class Simulation {
    }
}
