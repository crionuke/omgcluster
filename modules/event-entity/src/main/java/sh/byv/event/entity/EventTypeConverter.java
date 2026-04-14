package sh.byv.event.entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class EventTypeConverter implements AttributeConverter<EventType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(final EventType attribute) {
        return attribute == null ? null : attribute.getId();
    }

    @Override
    public EventType convertToEntityAttribute(final Integer dbData) {
        return dbData == null ? null : EventType.fromId(dbData);
    }
}
