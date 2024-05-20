package example.test;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;

import java.time.LocalDateTime;

@Controller("/currency")
public class CurrencyController {
    private final TimerStampService timerStampService;
    private final CurrencyService currencyService;

    CurrencyController(TimerStampService timerStampService, CurrencyService currencyService) {
        this.timerStampService = timerStampService;
        this.currencyService = currencyService;
    }

    @Get("/v1/from/{from}/to/{to}")
    @Produces(MediaType.TEXT_PLAIN)
    public String get(String from, String to) {
        if (timerStampService.isPast(LocalDateTime.now(), 5)) {
            currencyService.refresh();
        }
        return currencyService.get(from, to).toString();
    }
}
