package com.mostafa.akka.akkastream.myakkastreamtry;

import akka.Done;
import akka.NotUsed;
import akka.actor.ActorSystem;
import akka.stream.IOResult;
import akka.stream.javadsl.*;
import akka.util.ByteString;

import java.math.BigInteger;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.concurrent.CompletionStage;

//import jdocs.AbstractJavaTest;

public class Main4 {
    public static void main(String[] args) {
        final ActorSystem system = ActorSystem.create("QuickStart");
        final Source<Integer, NotUsed> source = Source.range(1, 100);
        final Source<BigInteger, NotUsed> factorials =
                source.scan(BigInteger.ONE, (acc, next) -> acc.multiply(BigInteger.valueOf(next)));

        CompletionStage<Done> result = factorials
                .zipWith(Source.range(0, 99), (num, idx) -> String.format("%d! = %s", idx, num))
                .throttle(10, Duration.ofSeconds(1))
                .runForeach(System.out::println, system);
        result.thenRun(system::terminate);
    }
}
