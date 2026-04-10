package sh.byv.instance;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class InstanceRelStatusConverter implements AttributeConverter<InstanceRelStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(final InstanceRelStatus attribute) {
        return attribute == null ? null : attribute.getId();
    }

    @Override
    public InstanceRelStatus convertToEntityAttribute(final Integer dbData) {
        return dbData == null ? null : InstanceRelStatus.fromId(dbData);
    }
}
