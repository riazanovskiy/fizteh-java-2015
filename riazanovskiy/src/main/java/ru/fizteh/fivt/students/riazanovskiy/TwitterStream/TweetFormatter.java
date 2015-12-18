package ru.fizteh.fivt.students.riazanovskiy.TwitterStream;

import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.Ansi.Color;
import twitter4j.Status;

import static org.fusesource.jansi.Ansi.ansi;

public class TweetFormatter {
    private final Status status;
    private final boolean showTime;

    public TweetFormatter(Status status, boolean showTime) {
        this.status = status;
        this.showTime = showTime;
    }

    String format() {
        Ansi formattedTweet = ansi();
        if (showTime) {
            formattedTweet.a('[' + RecentDateFormatter.format(status.getCreatedAt()) + "] ");
        }

        formattedTweet.fg(Color.BLUE).a('@' + status.getUser().getScreenName()).fg(Color.DEFAULT);
        formattedTweet.a(": ");

        if (status.isRetweet()) {
            formattedTweet.a("ретвитнул ");
            formattedTweet.fg(Color.BLUE).a('@' + status.getRetweetedStatus().getUser().getScreenName());
            formattedTweet.fg(Color.DEFAULT).a(": " + status.getRetweetedStatus().getText());
        } else {
            formattedTweet.a(status.getText());
            if (status.getRetweetCount() > 0) {
                formattedTweet.a(" (").a(status.getRetweetCount()).a(" ");
                formattedTweet.a(RussianWordForms.getWordForm("ретвит", status.getRetweetCount())).a(")");
            }
        }
        return formattedTweet.toString();
    }
}
