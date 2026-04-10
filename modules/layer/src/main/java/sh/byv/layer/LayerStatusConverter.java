package sh.byv.layer;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class LayerStatusConverter implements AttributeConverter<LayerStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(final LayerStatus attribute) {
        return attribute == null ? null : attribute.getId();
    }

    @Override
    public LayerStatus convertToEntityAttribute(final Integer dbData) {
        return dbData == null ? null : LayerStatus.fromId(dbData);
    }
}
