package example.test;

import io.micronaut.context.annotation.Primary;
import io.micronaut.context.annotation.Prototype;
import jakarta.inject.Inject;

import java.util.HashMap;
import java.util.Map;

/**
 * {
 *  from: EUR
 *      to :{
 *          USD: 0.34,
 *          BTC: 3.14
 *      }
 * }
 */
@Primary
@Prototype
public class CurrencyVagon implements Currency {
    private final String from;
    private final Map<String, Float> toMap;

    @Inject
    public CurrencyVagon() {
        from = "bombaclat";
        toMap = new HashMap<>();
        toMap.put("ok", 0.4f);
    }

    @Inject
    public CurrencyVagon(String from, Map<String, Float> toMap) {
        this.from = from;
        this.toMap = toMap;
    }

    @Override
    public Float getValue(String to) {
        return toMap.get(to);
    }

    public String getFrom() {
        return from;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        toMap.forEach((k, v) -> sb.append(k).append(" val: ").append(v).append("; "));
        return "Currency: " + from + " - " + sb;
    }
}

