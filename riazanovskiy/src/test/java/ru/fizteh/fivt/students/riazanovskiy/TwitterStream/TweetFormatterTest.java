package ru.fizteh.fivt.students.riazanovskiy.TwitterStream;

import org.junit.Before;
import org.junit.Test;
import twitter4j.Status;
import twitter4j.TestUtils;

import java.util.List;

public class TweetFormatterTest {
    List<Status> statuses;

    @Before
    public void setUp() throws Exception {
        statuses = TestUtils.tweetsFromJson("/formatted-tweet-test-input.json");
    }

    @Test
    public void testFormat() throws Exception {

    }
}
