package ru.fizteh.fivt.students.riazanovskiy.TwitterStream;

import org.fusesource.jansi.Ansi;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import twitter4j.Status;
import twitter4j.TestUtils;

import java.util.List;

import static org.mockito.Matchers.any;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(RecentDateFormatter.class)
public class TweetFormatterTest {
    List<Status> statuses;

    @Before
    public void setUp() throws Exception {
        statuses = TestUtils.tweetsFromJson("/formatted-tweet-test-input.json");
        PowerMockito.mockStatic(RecentDateFormatter.class);
        when(RecentDateFormatter.format(any())).thenReturn("SOME FUCKING DATE");
    }

    @Test
    public void testFormat() throws Exception {
        Ansi.setEnabled(false);
        Assert.assertEquals("@sean_cummings: Aggressive Ponytail #freebandnames",
                new TweetFormatter(statuses.get(0), false).format());
        Assert.assertEquals("[SOME FUCKING DATE] @sean_cummings: Aggressive Ponytail #freebandnames",
                new TweetFormatter(statuses.get(0), true).format());

        Assert.assertEquals("@twitterapi: Motherfucking snakes on this motherfucking plane! (666666 ретвитов)",
                            new TweetFormatter(statuses.get(1), false).format());
    }
}
