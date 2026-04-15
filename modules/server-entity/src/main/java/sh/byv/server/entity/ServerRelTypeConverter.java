package sh.byv.server.entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ServerRelTypeConverter implements AttributeConverter<ServerRelType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(final ServerRelType attribute) {
        return attribute == null ? null : attribute.getId();
    }

    @Override
    public ServerRelType convertToEntityAttribute(final Integer dbData) {
        return dbData == null ? null : ServerRelType.fromId(dbData);
    }
}
