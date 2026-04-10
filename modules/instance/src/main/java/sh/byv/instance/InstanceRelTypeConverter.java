package sh.byv.instance;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class InstanceRelTypeConverter implements AttributeConverter<InstanceRelType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(final InstanceRelType attribute) {
        return attribute == null ? null : attribute.getId();
    }

    @Override
    public InstanceRelType convertToEntityAttribute(final Integer dbData) {
        return dbData == null ? null : InstanceRelType.fromId(dbData);
    }
}
