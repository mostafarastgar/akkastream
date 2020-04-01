package com.mostafa.akka.akkastream.myakkastreamtry.twitter;

import akka.Done;
import akka.NotUsed;
import akka.actor.ActorSystem;
import akka.stream.*;
import akka.stream.javadsl.*;
import akka.util.ByteString;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

import static com.mostafa.akka.akkastream.myakkastreamtry.twitter.Model.AKKA;

public class MainGraph {
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

    public static void main(String[] args) throws InterruptedException {
        final ActorSystem system = ActorSystem.create("reactive-tweets");
        Sink<ByteString, CompletionStage<IOResult>> writeAuthors = FileIO.toPath(Paths.get("authors.txt"));
        Sink<ByteString, CompletionStage<IOResult>> writeHashtags = FileIO.toPath(Paths.get("hashtags.txt"));

        // #graph-dsl-broadcast
        CompletionStage<IOResult> result = RunnableGraph.fromGraph(
                GraphDSL.create(writeAuthors, writeHashtags, Keep.right(),
                        (b, writeAuthorsP, writeHashtagsP) -> {
                            final UniformFanOutShape<Model.Tweet, Model.Tweet> bcast = b.add(Broadcast.create(2));
                            final FlowShape<Model.Tweet, ByteString> toAuthor = b.add(Flow.of(Model.Tweet.class)
                                    .map(t -> ByteString.fromString(t.author.toString() + System.lineSeparator())));
                            final FlowShape<Model.Tweet, ByteString> toTags =
                                    b.add(
                                            Flow.of(Model.Tweet.class)
                                                    .mapConcat(t -> new ArrayList<Model.Hashtag>(t.hashtags()))
                                                    .map(h -> ByteString.fromString(h.toString() + System.lineSeparator())));
                            b.from(b.add(tweets)).viaFanOut(bcast).via(toAuthor).to(writeAuthorsP);
                            b.from(bcast).via(toTags).to(writeHashtagsP);
                            return ClosedShape.getInstance();
                        }))
                .run(system);
        result.thenRun(system::terminate);
    }
}
