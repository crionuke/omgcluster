package sh.byv.sim.entity;

import sh.byv.zone.entity.ZoneRect;

import java.time.OffsetDateTime;

public record SimModel(long id,
                       OffsetDateTime createdAt,
                       OffsetDateTime updatedAt,
                       String name,
                       SimStatus status,
                       long zoneId,
                       ZoneRect zoneRect,
                       long layerId,
                       String layerName,
                       long worldId,
                       String worldName) {
}
