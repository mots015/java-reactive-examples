package com.stackroute.reactive.examples;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.SynchronousSink;

import java.time.Duration;
import java.util.Random;

public class ReactiveStreamDemoTest {

    @Test
    public void generateInfiniteStream() {
        Flux.generate((SynchronousSink<Integer> synchronousSink) -> {
            synchronousSink.next(new Random().nextInt(100));
        })
                .doOnNext(number -> System.out.println(number))
                .subscribe();
    }
}
