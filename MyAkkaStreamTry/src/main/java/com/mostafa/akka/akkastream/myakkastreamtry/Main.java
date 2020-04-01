package com.mostafa.akka.akkastream.myakkastreamtry;

import akka.stream.*;
import akka.stream.javadsl.*;
import akka.Done;
import akka.NotUsed;
import akka.actor.ActorSystem;
import akka.util.ByteString;

import java.nio.file.Paths;
import java.math.BigInteger;
import java.time.Duration;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

//import jdocs.AbstractJavaTest;

public class Main {
    public static void main(String[] args) {
        final ActorSystem system = ActorSystem.create("QuickStart");
        final Source<Integer, NotUsed> source = Source.range(1, 100);
        CompletionStage<Done> done = source.runForeach(i -> System.out.println(i), system);
        done.thenRun(()->system.terminate());
    }
}
