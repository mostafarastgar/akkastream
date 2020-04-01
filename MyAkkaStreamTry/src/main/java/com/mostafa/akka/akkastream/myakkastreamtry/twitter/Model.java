package com.mostafa.akka.akkastream.myakkastreamtry.twitter;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class Model {
    public static class Author {
        public final String handle;

        public Author(String handle) {
            this.handle = handle;
        }

        // ...

        // #model

        @Override
        public String toString() {
            return "Author(" + handle + ")";
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            Author author = (Author) o;

            if (handle != null ? !handle.equals(author.handle) : author.handle != null) {
                return false;
            }

            return true;
        }

        @Override
        public int hashCode() {
            return handle != null ? handle.hashCode() : 0;
        }
        // #model
    }
    // #model

    // #model

    public static class Hashtag {
        public final String name;

        public Hashtag(String name) {
            this.name = name;
        }

        // ...
        // #model

        @Override
        public int hashCode() {
            return name.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null) return false;
            if (getClass() != obj.getClass()) return false;
            Hashtag other = (Hashtag) obj;
            return name.equals(other.name);
        }

        @Override
        public String toString() {
            return "Hashtag(" + name + ")";
        }
        // #model
    }
    // #model

    // #model

    public static class Tweet {
        public final Author author;
        public final long timestamp;
        public final String body;

        public Tweet(Author author, long timestamp, String body) {
            this.author = author;
            this.timestamp = timestamp;
            this.body = body;
        }

        public Set<Hashtag> hashtags() {
            return Arrays.asList(body.split(" ")).stream()
                    .filter(a -> a.startsWith("#"))
                    .map(a -> new Hashtag(a))
                    .collect(Collectors.toSet());
        }

        // ...
        // #model

        @Override
        public String toString() {
            return "Tweet(" + author + "," + timestamp + "," + body + ")";
        }

        // #model
    }
    // #model

    // #model

    public static final Hashtag AKKA = new Hashtag("#akka");
}
