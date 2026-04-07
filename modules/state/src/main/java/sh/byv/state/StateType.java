package sh.byv.state;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StateType {
    CLUSTER(ClusterState.class);

    final Class<?> bodyClass;
}