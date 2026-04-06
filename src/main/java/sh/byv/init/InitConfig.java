package sh.byv.init;

import io.smallrye.config.ConfigMapping;

@ConfigMapping(prefix = "omgcluster.init")
interface InitConfig {

    int version();
}
