package ru.fizteh.fivt.students.riazanovskiy.TwitterStream;

import com.bytebybyte.google.geocoding.service.response.LatLng;
import twitter4j.*;

import java.util.concurrent.TimeUnit;

public class TweetPrinter {
    private static final int DELIMITER_LENGTH = 140;
    private final ArgumentParser parameters;
    private final LatLng currentLocation;

    public TweetPrinter(ArgumentParser parameters, LatLng currentLocation) {
        this.parameters = parameters;
        this.currentLocation = currentLocation;
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

        Twitter twitter = TwitterFactory.getSingleton();
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
        TwitterStream twitterStream = new TwitterStreamFactory().getInstance();

        twitterStream.addListener(new StatusAdapter() {
            @Override
            public void onStatus(Status status) {
                if (shouldShowTweet(status)) {
                    printSingleTweet(status, false);
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException ignored) {
                        Thread.currentThread().interrupt();
                    }
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

    static void printSingleTweet(Status status, boolean showTime) {
        String formattedTweet = new TweetFormatter(status, showTime).format();
        System.out.println(formattedTweet);
        System.out.println(new String(new char[DELIMITER_LENGTH]).replace('\0', '-'));
    }
}
