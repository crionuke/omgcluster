package sh.byv.state;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StateType {
    WORLD(WorldState.class);

    final Class<?> bodyClass;
}