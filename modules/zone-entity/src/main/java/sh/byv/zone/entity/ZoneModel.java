package sh.byv.zone.entity;

import java.time.OffsetDateTime;

public record ZoneModel(long id,
                        long layerId,
                        OffsetDateTime createdAt,
                        OffsetDateTime updatedAt,
                        ZoneStatus status,
                        ZoneRect rect,
                        Long parentId) {
}
