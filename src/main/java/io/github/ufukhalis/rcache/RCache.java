package io.github.ufukhalis.rcache;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RCache <Key, Value> {

    private final CacheService<Key, Value> cacheService;

    private final int poolSize = 10;
    private final ExecutorService executorService;

    RCache(int maxSize, Duration expire) {
        this.cacheService = new CacheService<>(expire, maxSize);
        this.executorService = Executors.newFixedThreadPool(this.poolSize);
    }

    public Mono<Void> put(Key key, Value value) {
        return cacheService.put(key, value).subscribeOn(Schedulers.fromExecutor(executorService));
    }

    public Mono<Void> remove(Key key) {
        return cacheService.remove(key).subscribeOn(Schedulers.fromExecutor(executorService));
    }

    public Mono<Value> get(Key key) {
        return cacheService.get(key).subscribeOn(Schedulers.fromExecutor(executorService));
    }

    static class Builder {
        private int maxSize;
        private Duration expire;

        Builder maxSize(int maxSize) {
            this.maxSize = maxSize;
            return this;
        }

        Builder expire(Duration expire) {
            this.expire = expire;
            return this;
        }

        RCache build() {
            return new RCache(this.maxSize, this.expire);
        }
    }
}
