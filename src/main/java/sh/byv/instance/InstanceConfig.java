package sh.byv.instance;

import io.smallrye.config.ConfigMapping;

@ConfigMapping(prefix = "omgcluster.instance")
public interface InstanceConfig {

    AddressConfig address();

    interface AddressConfig {
        String internal();

        String external();
    }
}
