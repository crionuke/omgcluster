package sh.byv.node;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class NodeRelTypeConverter implements AttributeConverter<NodeRelType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(final NodeRelType attribute) {
        return attribute == null ? null : attribute.getId();
    }

    @Override
    public NodeRelType convertToEntityAttribute(final Integer dbData) {
        return dbData == null ? null : NodeRelType.fromId(dbData);
    }
}
