package sh.byv.event.entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class EventStatusConverter implements AttributeConverter<EventStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(final EventStatus attribute) {
        return attribute == null ? null : attribute.getId();
    }

    @Override
    public EventStatus convertToEntityAttribute(final Integer dbData) {
        return dbData == null ? null : EventStatus.fromId(dbData);
    }
}
