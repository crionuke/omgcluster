package sh.byv.node.entity;

import io.smallrye.config.ConfigMapping;

@ConfigMapping(prefix = "omgcluster.node")
public interface NodeConfig {

    String name();
}
