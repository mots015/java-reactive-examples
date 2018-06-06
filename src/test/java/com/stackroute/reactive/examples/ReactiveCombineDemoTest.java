package com.stackroute.reactive.examples;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

public class ReactiveCombineDemoTest {

private Mono<String> mono1, mono2, mono3;
private Flux<String> flux1, flux2, intervalFlux1, intervalFlux2;
    @Before
    public void setUp() {
        //Creates a Mono that
         mono1 = Mono.just(" Java ");
         mono2 = Mono.just(" Kotlin ");
         mono3 = Mono.just(" Groovy ");

         flux1 = Flux.just(" 1 ", " 2 ", " 3 ", " 4 ");
         flux2 = Flux.just(" A ", " B ", " C ");

        intervalFlux1 = Flux
                .interval(Duration.ofMillis(500))
                .zipWith(flux1, (i, string) -> string);

        intervalFlux2 = Flux
                .interval(Duration.ofMillis(700))
                .zipWith(flux2, (i, string) -> string);
    }

    @After
    public void tearDown() {
     mono1=mono2=mono3=null;
     flux1=flux2=intervalFlux1=intervalFlux2=null;

    }

    @Test
    public void testMonoConcatOperator() {
        Flux.concat(mono1,mono2,mono3)
                .subscribe(System.out::print);
    }
    @Test
    public void testFluxConcatOperator() {
        Flux.concat(flux1,flux2)
                .subscribe(System.out::print);
    }
    @Test
    public void testFluxZipOperator() {
        Flux.zip(flux1, flux2,
                (itemFlux1, itemFlux2) -> "{" + itemFlux1 + itemFlux2 + "}")
                .subscribe(System.out::print);
    }

    @Test
    public void testMonoZipOperator() {
        Mono.zip(mono1, mono2,
                (itemMono1, itemMono2) -> "{" + itemMono1 + itemMono2 + "}")
                .subscribe(System.out::print);
    }


    @Test
    public void testFluxMergeOperator() throws Exception {
        intervalFlux1.mergeWith(intervalFlux2)
                .subscribe(System.out::print);
        Thread.sleep(3000);
    }
}
