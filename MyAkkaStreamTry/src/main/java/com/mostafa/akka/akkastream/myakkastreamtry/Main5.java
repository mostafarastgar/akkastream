package com.mostafa.akka.akkastream.myakkastreamtry;

import akka.Done;
import akka.NotUsed;
import akka.actor.ActorSystem;
import akka.stream.javadsl.Keep;
import akka.stream.javadsl.RunnableGraph;
import akka.stream.javadsl.Sink;
import akka.stream.javadsl.Source;

import java.math.BigInteger;
import java.time.Duration;
import java.util.Arrays;
import java.util.concurrent.CompletionStage;

//import jdocs.AbstractJavaTest;

public class Main5 {
    public static void main(String[] args) {
        final ActorSystem system = ActorSystem.create("QuickStart");
// connect the Source to the Sink, obtaining a RunnableGraph
        final Sink<Integer, CompletionStage<Integer>> sink =
                Sink.<Integer, Integer>fold(0, (aggr, next) -> aggr + next);
        final RunnableGraph<CompletionStage<Integer>> runnable =
                Source.from(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)).toMat(sink, Keep.right());

// get the materialized value of the FoldSink
        final CompletionStage<Integer> sum1 = runnable.run(system);
        final CompletionStage<Integer> sum2 = runnable.run(system);

        sum1.whenComplete((val1, val2)-> System.out.println(val1));
        sum2.whenComplete((val1, val2)-> System.out.println(val1));
// sum1 and sum2 are different Futures!
    }
}
