package twitter4j;

import org.apache.commons.io.IOUtils;
import twitter4j.auth.Authorization;
import twitter4j.conf.Configuration;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.nio.charset.StandardCharsets.*;

/**
 * Dirty hack to access twitter4j package local classes.
 * <p>
 * Created by kormushin on 29.09.15.
 */
public class TestUtils {
    static final InputStream inputStream = new ByteArrayInputStream(fileToString("/stream-dump.json").getBytes(UTF_8));

    public static String fileToString(String resource) {
        try (InputStream inputStream = TestUtils.class.getResourceAsStream(resource)) {
            return IOUtils.toString(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Status> tweetsFromJson(String resource) {
        try {
            JSONObject json = new JSONObject(fileToString(resource));

            JSONArray array = json.getJSONArray("statuses");
            List<Status> tweets = new ArrayList<>(array.length());
            for (int i = 0; i < array.length(); i++) {
                JSONObject tweet = array.getJSONObject(i);
                tweets.add(new StatusJSONImpl(tweet));
            }

            return tweets;
        } catch (JSONException | TwitterException e) {
            throw new RuntimeException(e);
        }
    }

    public static class MockedTwitterStream extends TwitterStreamImpl {
        public MockedTwitterStream(TwitterStream twitterStream) {
            super(((TwitterBaseImpl) twitterStream).conf, ((TwitterBaseImpl) twitterStream).auth);
        }

        @Override
        StatusStream getSampleStream() throws TwitterException {
            ensureAuthorizationEnabled();
            try {
                Method getDispatcher = getClass().getSuperclass().getDeclaredMethod("getDispatcher");
                getDispatcher.setAccessible(true);
                return new StatusStreamImpl((Dispatcher) getDispatcher.invoke(this), new HttpResponse() {
                    @Override
                    public String getResponseHeader(String name) {
                        return null;
                    }

                    @Override
                    public Map<String, List<String>> getResponseHeaderFields() {
                        return null;
                    }

                    @Override
                    public void disconnect() {
                    }

                    @Override
                    public InputStream asStream() {
                        return inputStream;
                    }
                }, conf);
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException | IOException e) {
                throw new TwitterException(e);
            }
        }
    }
}
