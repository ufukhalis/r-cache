package io.github.ufukhalis.rcache;

import java.time.Duration;

public class CacheModel <V> {
    private final long expireTime;
    private final V value;

    CacheModel(V value, Duration expireDuration) {
        this.expireTime = System.currentTimeMillis() + expireDuration.getSeconds() * 1000;
        this.value = value;
    }

    public long getExpireTime() {
        return expireTime;
    }

    public V getValue() {
        return value;
    }
}
