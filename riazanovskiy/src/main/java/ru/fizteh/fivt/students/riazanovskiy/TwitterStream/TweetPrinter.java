package ru.fizteh.fivt.students.riazanovskiy.TwitterStream;

import com.bytebybyte.google.geocoding.service.response.LatLng;
import twitter4j.*;

import java.util.concurrent.TimeUnit;

public class TweetPrinter {
    private static final int DELIMITER_LENGTH = 140;
    private final ArgumentParser parameters;
    private final LatLng currentLocation;
    private final TwitterStream twitterStream;
    private final Twitter twitter;

    public TweetPrinter(ArgumentParser parameters, LatLng currentLocation,
                        TwitterStream twitterStream, Twitter twitter) {
        this.parameters = parameters;
        this.currentLocation = currentLocation;
        this.twitterStream = twitterStream;
        this.twitter = twitter;
    }

    void print() throws TwitterException {
        if (parameters.isStream()) {
            printTweetsInStream();
        } else {
            printTweetsByQuery();
        }
    }

     void printTweetsByQuery() throws TwitterException {
        if (parameters.getKeywords().isEmpty()) {
            System.err.println("Пустой запрос. Попробуйте --stream, если хотите видеть все твиты подряд");
            return;
        }

        Query query = new Query(parameters.getKeywords());
        query.setCount(parameters.getLimit());

        QueryResult result = twitter.search(query);
        if (result.getCount() == 0) {
            System.out.println("Нет твитов по запросу " + query);
            return;
        }

        result.getTweets().stream().filter(this::shouldShowTweet).forEach(status
                -> printSingleTweet(status, true));
    }

    boolean shouldShowTweet(Status status) {
        return (!status.isRetweet() || parameters.isShowRetweets())
                && ((currentLocation == null || (status.getGeoLocation() != null
                && GeocodeWrapper.isNearby(currentLocation,
                new GeocodeWrapper.Location(status.getGeoLocation().getLatitude(),
                        status.getGeoLocation().getLongitude())))));
    }

    void printTweetsInStream() {
        twitterStream.addListener(new StatusAdapter() {
            @Override
            public void onStatus(Status status) {
                if (shouldShowTweet(status)) {
                    printSingleTweet(status, false);
                    waitBetweenTweets();
                }
            }
        });

        if (parameters.getKeywords().isEmpty()) {
            twitterStream.sample();
        } else {
            FilterQuery query = new FilterQuery().track(parameters.getKeywords());
            twitterStream.filter(query);
        }
    }

    void waitBetweenTweets() {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }
    }

    void printSingleTweet(Status status, boolean showTime) {
        String formattedTweet = new TweetFormatter(status, showTime).format();
        System.out.println(formattedTweet);
        System.out.println(new String(new char[DELIMITER_LENGTH]).replace('\0', '-'));
    }
}
