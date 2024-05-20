package example.test;

import io.micronaut.context.BeanContext;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
public class BeanTest {
    @Inject
    BeanContext beanContext;

    @Test
    public void testCurrencyBean() {
        final Currency cr = beanContext.getBean(Currency.class);
        assertInstanceOf(CurrencyVagon.class, cr);
    }
}
