package example.test;

import io.micronaut.context.ApplicationContext;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
public class CurrencyControllerTest {
    private static EmbeddedServer server;
    private static HttpClient client;

    @BeforeAll
    static void setupServer() {
        server = ApplicationContext.run(EmbeddedServer.class);
        client = server.getApplicationContext().createBean(HttpClient.class, server.getURL());
    }

    @AfterAll
    static void stopServer() {
        if (server != null) {
            server.stop();
        }
        if (client != null) {
            client.stop();
        }
    }

    @Test
    public void testCurrencyV1GetFound() {
        String body = client.toBlocking().retrieve("/currency/v1/from/EUR/to/GBP");
        assertNotNull(body);
        assertEquals("Currency: EUR - GBP val: 0.6; ", body);
    }

    @Test
    public void testCurrencyV1GetInvalid() {
        HttpClientResponseException e = assertThrows(HttpClientResponseException.class, () ->
                client.toBlocking().exchange("/currency/v1/from/EUR/to/")
        );
        assertEquals(404, e.getStatus().getCode());
    }

    @Test
    public void testCurrencyTimeOut() {
        String body = client.toBlocking().retrieve("/currency/v1/from/EUR/to/GBP");

        try {
            TimeUnit.SECONDS.sleep(6);
        } catch(InterruptedException e) {
            throw new RuntimeException(e);
        }

        String body2 = client.toBlocking().retrieve("/currency/v1/from/EUR/to/GBP");

        assertNotNull(body2);
        assertEquals("Currency: EUR - GBP val: 0.6; ", body2);
    }
}
