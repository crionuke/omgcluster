package sh.byv.conn;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ConnZoneRelStatusConverter implements AttributeConverter<ConnZoneRelStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(final ConnZoneRelStatus attribute) {
        return attribute == null ? null : attribute.getId();
    }

    @Override
    public ConnZoneRelStatus convertToEntityAttribute(final Integer dbData) {
        return dbData == null ? null : ConnZoneRelStatus.fromId(dbData);
    }
}
