package com.stackroute.reactive.examples;

import org.junit.Test;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.List;

public class ReactiveStreamOperationDemoTest {

    @Test
    public void testFilterMap(){


    }

    @Test
    public void testFlatMap(){
        List<String> list = Arrays.asList("Hi","Hello","Bye");
        Flux.fromIterable(list)
                .flatMap(element->Flux.just(element, "1","2","3"))
                .subscribe(System.out::println);

    }


    @Test
    public void testZipWith(){
        List<String> list = Arrays.asList("Hi","Hello","Bye");
        Flux.fromIterable(list)
                .zipWith(Flux.just("1","2","3"))
                .subscribe(System.out::println);

    }
}
