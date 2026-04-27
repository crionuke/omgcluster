package sh.byv.layer.entity;

import java.time.OffsetDateTime;

public record LayerModel(long id,
                         OffsetDateTime createdAt,
                         OffsetDateTime updatedAt,
                         String name,
                         LayerStatus status,
                         long worldId,
                         String worldName) {
}
