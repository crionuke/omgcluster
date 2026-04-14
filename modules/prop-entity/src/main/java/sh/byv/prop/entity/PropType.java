package sh.byv.prop.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PropType {
    RUNTIME_VERSION(0);

    final Object defaultValue;
}
