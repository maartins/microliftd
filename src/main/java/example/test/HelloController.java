package example.test;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;

@Controller("/hello")
public class HelloController {
    @Get
    @Produces(MediaType.TEXT_PLAIN)
    public String index() {
        String a = "Instinct";
        return "Hello world! " + System.getenv(a) + " " + a;
    }
}
