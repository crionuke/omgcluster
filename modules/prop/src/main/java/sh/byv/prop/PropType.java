package sh.byv.prop;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PropType {
    CLUSTER_VERSION(0);

    final Object defaultValue;
}
