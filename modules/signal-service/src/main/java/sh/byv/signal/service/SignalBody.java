package sh.byv.signal.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignalBody {

    SignalType type;
    TickSignal tick;

    @Data
    @AllArgsConstructor
    public static class TickSignal {
        long zoneId;
        long tick;
    }
}