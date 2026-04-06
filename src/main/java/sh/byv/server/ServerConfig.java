package sh.byv.server;

import io.smallrye.config.ConfigMapping;

@ConfigMapping(prefix = "omgcluster.server")
interface ServerConfig {

    AddressConfig address();

    interface AddressConfig {
        String internal();

        String external();
    }
}
