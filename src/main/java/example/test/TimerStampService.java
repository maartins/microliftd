package example.test;

import jakarta.annotation.PostConstruct;
import jakarta.inject.Singleton;

import java.time.LocalDateTime;

@Singleton
public class TimerStampService {
    private LocalDateTime last;

    @PostConstruct
    void init() {
        last = LocalDateTime.now();
    }

    public boolean isPast(LocalDateTime now, int seconds) {
        boolean isPast = now.isAfter(last.plusSeconds(seconds));
        last = now;
        return isPast;
    }
}
