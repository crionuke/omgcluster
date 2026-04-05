package sh.byv.init;

import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;

@ConfigMapping(prefix = "omgcluster.init")
interface InitConfig {

    int version();
}
