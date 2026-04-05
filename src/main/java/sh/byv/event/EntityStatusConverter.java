package sh.byv.event;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class EntityStatusConverter implements AttributeConverter<EntityStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(final EntityStatus attribute) {
        return attribute == null ? null : attribute.getId();
    }

    @Override
    public EntityStatus convertToEntityAttribute(final Integer dbData) {
        return dbData == null ? null : EntityStatus.fromId(dbData);
    }
}
