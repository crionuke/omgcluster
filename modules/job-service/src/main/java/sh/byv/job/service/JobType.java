package sh.byv.job.service;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum JobType {
    EVENT("event", "system", 1),
    ZONE("zone-%d", "zones", 1);

    final String identity;
    final String group;
    final int interval;

    public String formatIdentity(final Long entityId) {
        return identity.formatted(entityId);
    }

    public static JobType fromIdentity(final String identity) {
        return Arrays.stream(values())
                .filter(type -> identity.matches(type.identity.replace("%d", "\\d+")))
                .findFirst()
                .orElseThrow();
    }
}
