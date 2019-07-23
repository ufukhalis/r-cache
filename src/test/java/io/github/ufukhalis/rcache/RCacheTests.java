package io.github.ufukhalis.rcache;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.Arrays;
import java.util.stream.IntStream;

public class RCacheTests {

    final RCache<String, String> rCache = new RCache.Builder()
            .maxSize(5)
            .expire(Duration.ofSeconds(1))
            .cachePoolSize(5)
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

    @Test
    void test_removeAll_shouldRemove_allValues() {
        rCache.put("key", "value").block();
        rCache.put("key1", "value").block();
        String value = rCache.removeAll(Arrays.asList("key", "key1"))
                .flatMap(ignore -> rCache.get("key")).block();

        Assertions.assertEquals(null, value);
    }

    @Test
    void test_cacheServiceEvent_shouldBe_triggered() {
        IntStream.range(0, 10)
                .forEach(index -> {
                    try {
                        rCache.put("key" + index, "value" + index).block();
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {

                    }
                });
        Assertions.assertTrue(rCache.cachedElementSize() < 10);
    }
}
