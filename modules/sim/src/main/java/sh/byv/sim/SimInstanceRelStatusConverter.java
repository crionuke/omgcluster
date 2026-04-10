package sh.byv.sim;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class SimInstanceRelStatusConverter implements AttributeConverter<SimInstanceRelStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(final SimInstanceRelStatus attribute) {
        return attribute == null ? null : attribute.getId();
    }

    @Override
    public SimInstanceRelStatus convertToEntityAttribute(final Integer dbData) {
        return dbData == null ? null : SimInstanceRelStatus.fromId(dbData);
    }
}
