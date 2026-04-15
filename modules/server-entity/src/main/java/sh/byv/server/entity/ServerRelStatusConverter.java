package sh.byv.server.entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ServerRelStatusConverter implements AttributeConverter<ServerRelStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(final ServerRelStatus attribute) {
        return attribute == null ? null : attribute.getId();
    }

    @Override
    public ServerRelStatus convertToEntityAttribute(final Integer dbData) {
        return dbData == null ? null : ServerRelStatus.fromId(dbData);
    }
}
