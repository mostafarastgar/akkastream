package com.mostafa.akka.akkastream.myakkastreamtry.twitter;

import akka.Done;
import akka.NotUsed;
import akka.actor.ActorSystem;
import akka.stream.javadsl.Sink;
import akka.stream.javadsl.Source;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletionStage;

import static com.mostafa.akka.akkastream.myakkastreamtry.twitter.Model.AKKA;

public class Main {

    public static final Source<Model.Tweet, NotUsed> tweets =
            Source.from(
                    Arrays.asList(
                            new Model.Tweet(new Model.Author("rolandkuhn"), System.currentTimeMillis(), "#akka rocks!"),
                            new Model.Tweet(new Model.Author("patriknw"), System.currentTimeMillis(), "#akka !"),
                            new Model.Tweet(new Model.Author("bantonsson"), System.currentTimeMillis(), "#akka !"),
                            new Model.Tweet(new Model.Author("drewhk"), System.currentTimeMillis(), "#akka !"),
                            new Model.Tweet(
                                    new Model.Author("ktosopl"), System.currentTimeMillis(), "#akka on the rocks!"),
                            new Model.Tweet(new Model.Author("mmartynas"), System.currentTimeMillis(), "wow #akka !"),
                            new Model.Tweet(new Model.Author("akkateam"), System.currentTimeMillis(), "#akka rocks!"),
                            new Model.Tweet(new Model.Author("bananaman"), System.currentTimeMillis(), "#bananas rock!"),
                            new Model.Tweet(new Model.Author("appleman"), System.currentTimeMillis(), "#apples rock!"),
                            new Model.Tweet(
                                    new Model.Author("drama"),
                                    System.currentTimeMillis(),
                                    "we compared #apples to #oranges!")));

    public static void main(String[] args) {
        final ActorSystem system = ActorSystem.create("reactive-tweets");
        final Source<Model.Author, NotUsed> authors =
                tweets.filter(t -> t.hashtags().contains(AKKA)).map(t -> t.author);
        CompletionStage<Done> result = authors.runWith(Sink.foreach(a -> System.out.println(a)), system);
        result.thenRun(system::terminate);
    }
}
