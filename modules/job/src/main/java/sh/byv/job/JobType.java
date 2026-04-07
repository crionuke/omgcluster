package sh.byv.job;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum JobType {
    EVENT(1);

    final int interval;
}
