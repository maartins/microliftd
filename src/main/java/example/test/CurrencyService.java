package example.test;

import com.google.gson.*;
import io.micronaut.core.async.processor.SingleSubscriberProcessor;
import io.micronaut.core.async.publisher.DelayedSubscriber;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.uri.UriBuilder;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Flow;

@Singleton
public class CurrencyService {
    private static final Logger LOG = LoggerFactory.getLogger(CurrencyService.class);

    @Client("https://api.currencyapi.com/v3")
    @Inject
    HttpClient httpClient;

    @Inject
    SupaSecrets secrets;

    private final ArrayList<CurrencyVagon> convoy = new ArrayList<>();

    @PostConstruct
    public void init() {
        convoy.add(new CurrencyVagon());
    }

    public CurrencyVagon get(String from, String to) {
        boolean exists = convoy.stream().anyMatch(c -> from.equalsIgnoreCase(c.getFrom()));
        if (exists) {
            LOG.atInfo().log("found: " + from + " - " + to);
            return convoy.stream()
                    .filter(c -> from.equalsIgnoreCase(c.getFrom()))
                    .findFirst()
                    .orElse(new CurrencyVagon());
        }
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(CurrencyVagon.class, (JsonDeserializer<CurrencyVagon>) (jsonElement, type, ctx) -> {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            LOG.atInfo().log("jsonObject: " + jsonObject);
//            return new CurrencyVagon(from, Map.of("a", 4.12f));
            return null;
        });
        CurrencyVagon f = null;
        getLatest(from, to).subscribe(new Subscriber<>() {
            @Override
            public void onSubscribe(Subscription s) {
                s.request(1);
            }

            @Override
            public void onNext(String s) {
                f = new CurrencyVagon(from, Map.of(to, 0.6f));
            }

            @Override
            public void onError(Throwable t) {
                LOG.atError().log(t.getMessage());
            }

            @Override
            public void onComplete() {}
        });
//        gsonBuilder.create().fromJson(a, CurrencyVagon.class);
        LOG.atInfo().log("created: " + from + " - " + to);
        convoy.add(cv);
        return cv;
    }

    public Publisher<String> getLatest(String from, String to) {
        LOG.atInfo().log("fetching: " + from + " - " + to);
        String uri = UriBuilder.of("/latest")
                .queryParam("apikey", secrets.get("CUR_API_KEY"))
                .queryParam("currencies", to)
                .queryParam("base_currency", from)
                .toString();
        return httpClient.retrieve(uri);
    }

    public String getStatus() {
        String uri = UriBuilder.of("/status").queryParam("apikey", secrets.get("CUR_API_KEY")).toString();
        LOG.atInfo().log("status requested.");
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(CurrencyStatus.class, (JsonDeserializer<CurrencyStatus>) (jsonElement, type, ctx) -> {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            String accountId = jsonObject.get("account_id").getAsString();
            return new CurrencyStatus(accountId);
        });
        return gsonBuilder.create().fromJson(httpClient.toBlocking().retrieve(uri), CurrencyStatus.class).getAccountId();
    }

    public void refresh() {
        // Do something to refresh the vagoons :D
    }
}
