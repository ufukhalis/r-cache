package io.github.ufukhalis.rcache;

import reactor.core.scheduler.Schedulers;

public class CacheServiceEventHandler extends EventHandler<CacheService> {

    @Override
    void consume() {
        CacheService eventClass = EVENTS.peek();
        eventClass.clearTheCache()
                .doOnSuccess(ignore -> EVENTS.poll())
                .subscribeOn(Schedulers.single())
                .subscribe();
    }

}
