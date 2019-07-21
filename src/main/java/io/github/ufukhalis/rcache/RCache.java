package io.github.ufukhalis.rcache;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RCache <Key, Value> {

    private final CacheService<Key, Value> cacheService;

    private final ExecutorService executorService;

    RCache(int maxSize, Duration expire, int cachePoolSize) {
        this.cacheService = new CacheService<>(expire, maxSize);
        this.executorService = Executors.newFixedThreadPool(cachePoolSize);
    }

    public Mono<Void> put(Key key, Value value) {
        return cacheService.put(key, value).subscribeOn(Schedulers.fromExecutor(executorService));
    }

    public Mono<Void> remove(Key key) {
        return cacheService.remove(key).subscribeOn(Schedulers.fromExecutor(executorService));
    }

    public Mono<Void> removeAll(List<Key> keys) {
        return cacheService.removeAll(keys).subscribeOn(Schedulers.fromExecutor(executorService));
    }

    public Mono<Value> get(Key key) {
        return cacheService.get(key).subscribeOn(Schedulers.fromExecutor(executorService));
    }

    static class Builder {
        private int maxSize = 100;
        private int cachePoolSize = 10;
        private Duration expire = Duration.ofMinutes(5);

        Builder maxSize(int maxSize) {
            this.maxSize = maxSize;
            return this;
        }

        Builder cachePoolSize(int cachePoolSize) {
            this.cachePoolSize = cachePoolSize;
            return this;
        }

        Builder expire(Duration expire) {
            this.expire = expire;
            return this;
        }

        RCache build() {
            return new RCache(this.maxSize, this.expire, this.cachePoolSize);
        }
    }
}
