package sh.byv.sim.entity;

import java.time.OffsetDateTime;

public record SimModel(long id,
                       long zoneId,
                       OffsetDateTime createdAt,
                       OffsetDateTime updatedAt,
                       String name,
                       SimStatus status) {
}
