package example.test;

import io.micronaut.context.ApplicationContext;
import io.micronaut.http.client.HttpClient;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@MicronautTest
public class CurrencyServiceTest {
    private static EmbeddedServer server;
    private static HttpClient client;
    private static CurrencyService currencyService;

    @BeforeAll
    static void setupServer() {
        server = ApplicationContext.run(EmbeddedServer.class);
        client = server.getApplicationContext().createBean(HttpClient.class, server.getURL());
        currencyService = server.getApplicationContext().createBean(CurrencyService.class, server.getURL());
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
    public void testCurrencyServiceStatus() {
        String body = currencyService.getStatus();
        assertNotNull(body);
        assertEquals("246186094016729088", body);
    }

    @Test
    public void testCurrencyServiceGetNew() {
        CurrencyVagon body = currencyService.get("EUR", "USD");
        assertNotNull(body.toString());
        assertEquals("Currency: EUR - USD val: 0.6; ", body.toString());

        CurrencyVagon body2 = currencyService.get("EUR", "USD");
        assertNotNull(body2.toString());
        assertEquals("Currency: EUR - USD val: 0.6; ", body2.toString());
    }
}
