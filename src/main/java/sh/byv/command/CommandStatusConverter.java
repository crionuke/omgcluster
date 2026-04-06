package sh.byv.command;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class CommandStatusConverter implements AttributeConverter<CommandStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(final CommandStatus attribute) {
        return attribute == null ? null : attribute.getId();
    }

    @Override
    public CommandStatus convertToEntityAttribute(final Integer dbData) {
        return dbData == null ? null : CommandStatus.fromId(dbData);
    }
}
