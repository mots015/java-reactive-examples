package com.stackroute.reactive.examples;


import org.junit.Test;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ReactivePublisherSubscriberTests {

    @Test
    public void testSubscriptionForPublisherJust() {
        List<Integer> elements = new ArrayList<>();
       // just: Converts aa set of objects into a Flux that emits those objects
        Flux.just(5, 2, 3, 4)
                .log()
          .subscribe(elements::add);
        assertThat(elements).containsExactly(5, 2, 3, 4);
    }

    @Test
    public void testSubscriptionForPublisherExplicit() {
        List<Integer> elements = new ArrayList<>();
        Flux.just(1, 2, 3, 4)
                //.log()
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        //No events will be sent by a Publisher until demand is signaled via this method.
                       s.request(Long.MAX_VALUE);
                    }

                    @Override
                    public void onNext(Integer integer) {
                        elements.add(integer);
                      if(integer>=3)
                          throw new RuntimeException("RuntimeException thrown");
                        System.out.println("onNext"+integer);
                    }

                    @Override
                    public void onError(Throwable t) {
                        System.out.println("Got this "+t);
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("Done");
                    }
                });
        assertThat(elements).containsExactly(1, 2, 3, 4);
    }
    @Test
    public void testSubscriptionForPublisherBackpressure() {
        List<Integer> elements = new ArrayList<>();

        Flux.just(1, 2, 3, 4,5,6,7,8,9,10)
                .log()
                .subscribe(new Subscriber<Integer>() {
                    private Subscription s;
                    private int onNextAmount;
                    @Override
                    public void onSubscribe(Subscription s) {
                        //No events will be sent by a Publisher until demand is signaled via this method.
                        this.s=s;
                        s.request(2);
                    }

                    @Override
                    public void onNext(Integer integer) {
                        elements.add(integer);
                        onNextAmount++;
                        if(onNextAmount % 3 ==0 ){
                            s.request(2);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        System.out.println("Got this "+t);
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("Done");
                    }
                });
        assertThat(elements).containsExactly(1, 2);
    }
    @Test
    public void testSubscriptionForPublisherFromIterable() {
        List<String> list = Arrays.asList("Hi","Hello","Bye","Hi");
        Flux.fromIterable(list)
                .concatWith(Mono.just("-"))
                .distinct()
                .map(s->s.toUpperCase())
                .sort()
           .subscribe(System.out::println);
    }

    @Test
    public void testSubscriptionForPubliserRange() {
        List<Integer> elements = new ArrayList<>();
        Flux.range(5,4)
                .log()
                .subscribe(elements::add);
        assertThat(elements).containsExactly(5,6, 7, 8);
    }


    @Test
    public void testOverloadedSubscriptionWithErrorCompletion() {

        Flux.range(5,4)
                .subscribe(i->System.out.println(i),
                           err->System.err.println(err),
                           ()->System.out.println("Done"));
    }



    @Test
    public void testOverloadedSubscriptionWithErrorHandling() {
        List<Integer> elements = new ArrayList<>();
        Flux.range(1,4)
                .map(i->{
                    if (i<=3)
                        return i;
                    throw new RuntimeException("Exception thrown");
                })
                .subscribe(i->System.out.println(i),
                        err->System.err.println(err),
                        ()->System.out.println("Done"));

    }
}
