package sh.byv.instance;

import io.smallrye.config.ConfigMapping;

@ConfigMapping(prefix = "omgcluster.instance")
public interface InstanceConfig {

    String name();
}
