package ru.fizteh.fivt.students.riazanovskiy.TwitterStream;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.security.Permission;
import java.util.function.Function;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Main.class)
public class MainTest {
    @Test
    public void testMainError() throws Exception {
        try (ByteArrayOutputStream myOut = new ByteArrayOutputStream()) {
            System.setOut(new PrintStream(myOut));
            Main.main(new String[]{"--interoveslocumpraesta"});
            assertThat(myOut.toString(), startsWith("Usage: "));
        }
    }

    @Test
    public void testMain() throws Exception {
        TweetPrinter mock = mock(TweetPrinter.class);
        PowerMockito.whenNew(TweetPrinter.class).withAnyArguments().thenReturn(mock);
        PowerMockito.spy(Main.class);
        PowerMockito.doNothing().when(Main.class, "waitUntilEndOfInput");
        Main.main(new String[]{"--stream"});
        verify(mock).print();
    }
}
