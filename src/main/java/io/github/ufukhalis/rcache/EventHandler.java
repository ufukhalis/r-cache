package io.github.ufukhalis.rcache;

import java.util.concurrent.ConcurrentLinkedQueue;

public abstract class EventHandler<EventClass> {

    ConcurrentLinkedQueue<EventClass> EVENTS = new ConcurrentLinkedQueue<>();

    void pushEvent(EventClass eventClass) {
        if (EVENTS.size() == 0) {
            EVENTS.add(eventClass);
            consume();
        }
    }

    abstract void consume();
}
