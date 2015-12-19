package ru.fizteh.fivt.students.riazanovskiy.TwitterStream;

import com.beust.jcommander.JCommander;
import com.bytebybyte.google.geocoding.service.response.LatLng;
import org.junit.Test;
import ru.fizteh.fivt.students.riazanovskiy.TwitterStream.GeocodeWrapper.Location;
import twitter4j.Status;
import twitter4j.TestUtils;
import twitter4j.TestUtils.MockedTwitterStream;
import twitter4j.TwitterStreamFactory;

import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.*;

/**
 * Created by daniel on 12/18/15.
 */
public class TweetPrinterTest {
    private static ArgumentParser buildArguments(String... args) {
        ArgumentParser argumentParser = new ArgumentParser();
        new JCommander(argumentParser, args);
        return argumentParser;
    }

    @Test
    public void testPrint_stream() throws Exception {
        ArgumentParser argumentParser = new ArgumentParser();
        new JCommander(argumentParser, "--stream");
        TweetPrinter printer = spy(new TweetPrinter(buildArguments("--stream"), null, null, null));
        doNothing().when(printer).printTweetsInStream();
        printer.print();
        verify(printer).printTweetsInStream();
        verify(printer, never()).printTweetsByQuery();
    }

    @Test
    public void testPrint_query() throws Exception {
        TweetPrinter printer = spy(new TweetPrinter(buildArguments("--query", "Et ab hædis me sequestra"), null, null,
                null));
        doNothing().when(printer).printTweetsByQuery();
        printer.print();
        verify(printer, never()).printTweetsInStream();
        verify(printer).printTweetsByQuery();
    }

    @Test
    public void testPrintTweetsInStream() throws Exception {
        TweetPrinter printer = spy(new TweetPrinter(buildArguments("--stream"), null,
                new MockedTwitterStream(new TwitterStreamFactory().getInstance()), null));
        doNothing().when(printer).printSingleTweet(any(Status.class), any(boolean.class));
        doNothing().when(printer).waitBetweenTweets();
        printer.printTweetsInStream();
        TimeUnit.SECONDS.sleep(3);
        verify(printer, times(514)).printSingleTweet(any(Status.class), any(boolean.class));
    }
}
