package sh.byv.server.entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ServerStatusConverter implements AttributeConverter<ServerStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(final ServerStatus attribute) {
        return attribute == null ? null : attribute.getId();
    }

    @Override
    public ServerStatus convertToEntityAttribute(final Integer dbData) {
        return dbData == null ? null : ServerStatus.fromId(dbData);
    }
}
