package ru.fizteh.fivt.students.riazanovskiy.TwitterStream;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import com.bytebybyte.google.geocoding.service.response.LatLng;
import org.fusesource.jansi.AnsiConsole;
import twitter4j.*;

import java.util.Scanner;

class Main {
    private static final int MAXIMUM_TRIES = 3;

    public static void main(String[] args) {
        ArgumentParser argumentParser = new ArgumentParser();
        try {
            new JCommander(argumentParser, args);
        } catch (ParameterException ignored) {
            new JCommander(new ArgumentParser()).usage();
            return;
        }

        if (argumentParser.isHelp() || (argumentParser.getLimit() != null && argumentParser.isStream())) {
            new JCommander(new ArgumentParser()).usage();
            return;
        }

        if (argumentParser.getLimit() == null) {
            argumentParser.setLimit(Integer.MAX_VALUE);
        }

        LatLng currentLocation = null;
        if (!argumentParser.getLocation().isEmpty()) {
            currentLocation = GeocodeWrapper.getCoordinatesByString(argumentParser.getLocation());
        }

        AnsiConsole.systemInstall();

        for (int tries = 0; tries < MAXIMUM_TRIES; tries++) {
            try {
                new TweetPrinter(argumentParser, currentLocation,
                        new TwitterStreamFactory().getInstance(), TwitterFactory.getSingleton()).print();
                waitUntilEndOfInput();
                return;
            } catch (TwitterException ignored) {
                System.err.println("Trying to reconnect");
            }
        }
        System.err.println("Giving up after " + MAXIMUM_TRIES + " tries");
    }

    private static void waitUntilEndOfInput() {
        try (Scanner scanner = new Scanner(System.in)) {
            while (scanner.hasNext()) {
                scanner.next();
                Thread.yield();
            }
        }
    }
}
