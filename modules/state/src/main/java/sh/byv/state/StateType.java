package sh.byv.state;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StateType {
    INSTANCE(InstanceState.class);

    final Class<?> bodyClass;
}