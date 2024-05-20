package example.test;

import io.micronaut.context.env.PropertySource;
import io.micronaut.runtime.Micronaut;

import static io.micronaut.core.util.CollectionUtils.mapOf;

public class Application {

    public static void main(String[] args) {
        String enviroment = System.getenv("MICRONAUT_ENVIRONMENTS");
        if ("testy".equalsIgnoreCase(enviroment)) {
            Micronaut.build(args)
                    .enableDefaultPropertySources(false)
                    .propertySources(
                            PropertySource.of(
                                    "testy",
                                    mapOf(
                                            "micronaut.application.name", "appguide",
                                            "micronaut.server.netty.access-logger.enabled", "true"
                                    )
                            )
                    )
                    .mainClass(Application.class)
                    .start();
        } else {
            Micronaut.run(Application.class, args);
        }
    }
}