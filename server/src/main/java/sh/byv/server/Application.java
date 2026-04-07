package sh.byv.server;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;

@QuarkusMain
public class Application {

    static void main(final String... args) {
        Quarkus.run(args);
    }
}
