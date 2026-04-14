package sh.byv.node.entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class NodeRelStatusConverter implements AttributeConverter<NodeRelStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(final NodeRelStatus attribute) {
        return attribute == null ? null : attribute.getId();
    }

    @Override
    public NodeRelStatus convertToEntityAttribute(final Integer dbData) {
        return dbData == null ? null : NodeRelStatus.fromId(dbData);
    }
}
