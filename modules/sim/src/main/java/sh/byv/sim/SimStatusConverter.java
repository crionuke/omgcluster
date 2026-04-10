package sh.byv.sim;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class SimStatusConverter implements AttributeConverter<SimStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(final SimStatus attribute) {
        return attribute == null ? null : attribute.getId();
    }

    @Override
    public SimStatus convertToEntityAttribute(final Integer dbData) {
        return dbData == null ? null : SimStatus.fromId(dbData);
    }
}
