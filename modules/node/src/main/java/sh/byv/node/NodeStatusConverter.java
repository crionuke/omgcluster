package sh.byv.node;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class NodeStatusConverter implements AttributeConverter<NodeStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(final NodeStatus attribute) {
        return attribute == null ? null : attribute.getId();
    }

    @Override
    public NodeStatus convertToEntityAttribute(final Integer dbData) {
        return dbData == null ? null : NodeStatus.fromId(dbData);
    }
}
