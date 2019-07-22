package io.github.ufukhalis.rcache;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class CacheService<Key, Value> {

    private final ConcurrentHashMap<Key, CacheModel> CACHE_MAP = new ConcurrentHashMap<>();
    private final EventHandler<CacheService> eventHandler = new CacheServiceEventHandler();

    private final Duration duration;
    private final int maxSize;

    CacheService(Duration duration, int maxSize) {
        this.duration = duration;
        this.maxSize = maxSize;
    }

    Mono<Void> put(Key key, Value value) {
        return Mono.fromCallable(() -> {
            if (cacheNeedToBeCleaned()) {
                eventHandler.pushEvent(this);
            }
            return CACHE_MAP.put(key, new CacheModel<>(value, duration));
        }).then();
    }

    Mono<Void> remove(Key key) {
        return Mono.fromCallable(() -> CACHE_MAP.remove(key)).then();
    }

    Mono<Void> removeAll(List<Key> keys) {
        return Flux.fromIterable(keys)
                .flatMap(this::remove).then();
    }

    Mono<Value> get(Key key) {
        return Mono.fromCallable(() -> {
            long currentTime = System.currentTimeMillis();

            return Optional.ofNullable(CACHE_MAP.get(key))
                    .filter(value -> value.getExpireTime() >= currentTime)
                    .map(value -> Mono.just((Value) value.getValue()))
                    .orElse(Mono.empty());
        }).flatMap(v -> v);
    }

    Mono<Void> clearTheCache() {
        long currentTime = System.currentTimeMillis();

        List<Key> keys = CACHE_MAP.entrySet()
                .stream()
                .filter(entry -> entry.getValue().getExpireTime() < currentTime)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        return removeAll(keys);
    }

    int cachedElementSize() {
        return CACHE_MAP.size();
    }

    private boolean cacheNeedToBeCleaned() {
        return (double) cachedElementSize()/ maxSize >= 0.7;
    }
}
