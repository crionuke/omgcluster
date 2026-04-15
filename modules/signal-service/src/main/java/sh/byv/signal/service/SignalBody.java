package sh.byv.signal.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignalBody {

    SignalType type;
    TickBody tick;

    @Data
    @AllArgsConstructor
    public static class TickBody {
        long zoneId;
        long tickId;
    }
}