package sh.byv.instance;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class InstanceStatusConverter implements AttributeConverter<InstanceStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(final InstanceStatus attribute) {
        return attribute == null ? null : attribute.getId();
    }

    @Override
    public InstanceStatus convertToEntityAttribute(final Integer dbData) {
        return dbData == null ? null : InstanceStatus.fromId(dbData);
    }
}
