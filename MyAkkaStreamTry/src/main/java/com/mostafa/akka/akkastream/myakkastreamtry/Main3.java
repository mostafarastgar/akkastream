package com.mostafa.akka.akkastream.myakkastreamtry;

import akka.NotUsed;
import akka.actor.ActorSystem;
import akka.stream.IOResult;
import akka.stream.javadsl.*;
import akka.util.ByteString;

import java.math.BigInteger;
import java.nio.file.Paths;
import java.util.concurrent.CompletionStage;

//import jdocs.AbstractJavaTest;

public class Main3 {
    public static void main(String[] args) {
        final ActorSystem system = ActorSystem.create("QuickStart");
        final Source<Integer, NotUsed> source = Source.range(1, 10);
        final Source<BigInteger, NotUsed> factorials =
                source.scan(BigInteger.ONE, (acc, next) -> acc.multiply(BigInteger.valueOf(next)));

        CompletionStage<IOResult> result = factorials.map(BigInteger::toString)
                .runWith(lineSink("factorials2.txt"), system);
        result.thenRun(()->system.terminate());
    }

    public static Sink<String, CompletionStage<IOResult>> lineSink(String filename) {
        return Flow.of(String.class)
                .map(s -> ByteString.fromString(s + "\n"))
                .toMat(FileIO.toPath(Paths.get(filename)), Keep.right());
    }
}
