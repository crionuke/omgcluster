package sh.byv.conn;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ConnStatusConverter implements AttributeConverter<ConnStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(final ConnStatus attribute) {
        return attribute == null ? null : attribute.getId();
    }

    @Override
    public ConnStatus convertToEntityAttribute(final Integer dbData) {
        return dbData == null ? null : ConnStatus.fromId(dbData);
    }
}
