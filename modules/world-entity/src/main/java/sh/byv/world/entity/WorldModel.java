package sh.byv.world.entity;

import java.time.OffsetDateTime;

public record WorldModel(long id,
                         OffsetDateTime createdAt,
                         OffsetDateTime updatedAt,
                         String name,
                         WorldStatus status) {
}