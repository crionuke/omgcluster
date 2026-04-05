package sh.byv.cluster;

import sh.byv.context.ClusterContext;

public interface ClusterService {

    void init(ClusterContext cluster, int version);
}
