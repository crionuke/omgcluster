package sh.byv.command;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class CommandTypeConverter implements AttributeConverter<CommandType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(final CommandType attribute) {
        return attribute == null ? null : attribute.getId();
    }

    @Override
    public CommandType convertToEntityAttribute(final Integer dbData) {
        return dbData == null ? null : CommandType.fromId(dbData);
    }
}
