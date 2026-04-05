package sh.byv.server;

import io.smallrye.config.ConfigMapping;

@ConfigMapping(prefix = "omgcluster.server")
interface ServerConfig {

    String internalAddress();

    String externalAddress();
}
