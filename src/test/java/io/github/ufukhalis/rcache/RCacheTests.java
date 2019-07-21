package io.github.ufukhalis.rcache;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.time.Duration;

public class RCacheTests {

    final RCache<String, String> rCache = new RCache.Builder()
            .maxSize(10)
            .expire(Duration.ofSeconds(2))
            .build();

    @Test
    void test_put_should_completed() {
        StepVerifier.create(rCache.put("key", "value"))
                .expectComplete().verify();
    }

    @Test
    void test_get_shouldReturn_expectedValue() {
        rCache.put("key", "value").block();
        StepVerifier.create(rCache.get("key"))
                .expectNext("value")
                .expectComplete().verify();
    }

    @Test
    void test_remove_shouldRemove_value() {
        rCache.put("key", "value").block();
        String value = rCache.remove("key")
                .flatMap(ignore -> rCache.get("key")).block();

        Assertions.assertEquals(null, value);
    }
}
