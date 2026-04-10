package sh.byv.zone;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ZoneInstanceRelStatusConverter implements AttributeConverter<ZoneInstanceRelStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(final ZoneInstanceRelStatus attribute) {
        return attribute == null ? null : attribute.getId();
    }

    @Override
    public ZoneInstanceRelStatus convertToEntityAttribute(final Integer dbData) {
        return dbData == null ? null : ZoneInstanceRelStatus.fromId(dbData);
    }
}
