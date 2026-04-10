package sh.byv.zone;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ZoneStatusConverter implements AttributeConverter<ZoneStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(final ZoneStatus attribute) {
        return attribute == null ? null : attribute.getId();
    }

    @Override
    public ZoneStatus convertToEntityAttribute(final Integer dbData) {
        return dbData == null ? null : ZoneStatus.fromId(dbData);
    }
}
