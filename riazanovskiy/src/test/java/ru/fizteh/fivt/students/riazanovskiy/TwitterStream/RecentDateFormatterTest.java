package ru.fizteh.fivt.students.riazanovskiy.TwitterStream;

import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.Date;


public class RecentDateFormatterTest {

    @Test
    public void testFormat() throws Exception {
        LocalDateTime now = LocalDateTime.of(2012, Month.MARCH, 1, 16, 20);
        LocalDateTime then = LocalDateTime.of(2012, Month.FEBRUARY, 29, 4, 19);
        Assert.assertEquals("вчера", RecentDateFormatter.format(then, now));

        now = LocalDateTime.of(2013, Month.MARCH, 1, 16, 20);
        Assert.assertEquals("366 дней назад", RecentDateFormatter.format(then, now));

        now = LocalDateTime.of(2012, Month.MARCH, 1, 16, 20);
        then = LocalDateTime.of(2012, Month.FEBRUARY, 28, 4, 19);
        Assert.assertEquals("2 дня назад", RecentDateFormatter.format(then, now));

        then = LocalDateTime.of(2013, Month.MARCH, 1, 16, 20);
        Assert.assertEquals("только что", RecentDateFormatter.format(then, now));

        now = LocalDateTime.of(2016, Month.JANUARY, 1, 0, 10);
        then = LocalDateTime.of(2015, Month.DECEMBER, 31, 23, 50);
        Assert.assertEquals("20 минут назад", RecentDateFormatter.format(then, now));

        then = LocalDateTime.of(2015, Month.DECEMBER, 31, 23, 5);
        Assert.assertEquals("вчера", RecentDateFormatter.format(then, now));

        now = LocalDateTime.of(2016, Month.JANUARY, 1, 2, 10);
        then = LocalDateTime.of(2016, Month.JANUARY, 1, 1, 3);
        Assert.assertEquals("1 час назад", RecentDateFormatter.format(then, now));

        RecentDateFormatter.format(Date.from(then.atZone(ZoneId.systemDefault()).toInstant()));
    }
}
