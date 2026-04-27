package sh.byv.layer.entity;

import java.time.OffsetDateTime;

public record LayerModel(long id,
                         long worldId,
                         OffsetDateTime createdAt,
                         OffsetDateTime updatedAt,
                         String name,
                         LayerStatus status) {
}
