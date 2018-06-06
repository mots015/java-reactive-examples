package com.stackroute.reactive.examples;

import jdk.internal.dynalink.linker.LinkerServices;
import org.junit.Test;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;

import java.net.ConnectException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import static java.time.Duration.ofSeconds;

public class ConnectableFluxDemo {

    @Test
    public void ConnectableFluxTest() throws InterruptedException{
        Flux<Integer> source = Flux.range(1, 3)
                .doOnSubscribe(s -> System.out.println("subscribed to source"));
        ConnectableFlux<Integer> co = source.publish();

        co.subscribe(System.out::println, e -> {}, () -> {});
        System.out.println("Done subscribing 1");
        co.subscribe(System.out::println, e -> {}, () -> {});
        System.out.println("Done subscribing 2");
        Thread.sleep(500);
      System.out.println("will now connect");
      co.connect();
    }


    @Test
    public void ConnectableFluxInfiniteStreamTest() {
        List<Object> timeList = new ArrayList<>();
        ConnectableFlux<Object> connectableFlux = Flux.create(fluxSink -> {
            while(true) {
                fluxSink.next(System.currentTimeMillis());
            }
        })
                .sample(ofSeconds(2))
                .publish();
        System.out.println("done subscribing 1");
        connectableFlux.subscribe(System.out::println, e -> {}, () -> {});
        System.out.println("done subscribing 2");
        connectableFlux.subscribe(System.out::println, e -> {}, () -> {});
        connectableFlux .connect();
    }

}
