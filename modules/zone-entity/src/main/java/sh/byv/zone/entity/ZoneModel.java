package sh.byv.zone.entity;

import java.time.OffsetDateTime;

public record ZoneModel(long id,
                        OffsetDateTime createdAt,
                        OffsetDateTime updatedAt,
                        ZoneStatus status,
                        ZoneRect rect,
                        Long parentId,
                        long layerId,
                        String layerName,
                        long worldId,
                        String worldName) {
}
