package ru.fizteh.fivt.students.riazanovskiy.TwitterStream;

import com.bytebybyte.google.geocoding.service.response.LatLng;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import ru.fizteh.fivt.students.riazanovskiy.TwitterStream.GeocodeWrapper.Location;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;


@RunWith(PowerMockRunner.class)
@PrepareForTest(GeocodeWrapper.class)
public class GeocodeWrapperTest {
    private URL urlMock;

    @Before
    public void setUp() throws Exception {
        urlMock = PowerMockito.mock(URL.class);
        PowerMockito.whenNew(URL.class).withArguments("http://ipinfo.io/json/").thenReturn(urlMock);
    }

    @Test
    public void testGetCoordinatesByIp() throws Exception {
        String data = "{\n \"ip\": \"93.175.31.92\",\n" +
                "  \"hostname\": \"No Hostname\",\n" +
                "  \"city\": \"Moscow\",\n" +
                "  \"region\": \"Moscow\",\n" +
                "  \"country\": \"RU\",\n" +
                "  \"loc\": \"0.5, 0.25\",\n" +
                "  \"org\": \"AS5467 Non state educational institution Educational Scientific and Experimental Center" +
                " of Moscow Institute of Physics and Technology\",\n" +
                "  \"postal\": \"101194\"\n}";
        InputStream stream = new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8));
        PowerMockito.when(urlMock.openStream()).thenReturn(stream);

        Location location = GeocodeWrapper.getCoordinatesByIp();
        Assert.assertEquals(0.5, location.getLat(), 1.0E-08);
        Assert.assertEquals(0.25, location.getLng(), 1.0E-08);
    }

    @Test
    public void testIsNearby() throws Exception {
        Assert.assertTrue(GeocodeWrapper.isNearby(new Location(15.0, 25.0), new Location(15.0, 25.0)));
        Assert.assertFalse(GeocodeWrapper.isNearby(new Location(15.0, 25.0),
                new Location(15.0, 25.0 + 2 * GeocodeWrapper.NEARBY_THRESHOLD)));
    }

    @Test
    public void testGetCoordinatesByStringNearby1() throws IOException {
        String data = "{\n \"ip\": \"93.175.31.92\",\n" +
                "  \"hostname\": \"No Hostname\",\n" +
                "  \"city\": \"Moscow\",\n" +
                "  \"region\": \"Moscow\",\n" +
                "  \"country\": \"RU\",\n" +
                "  \"loc\": \"0.5, 0.25\",\n" +
                "  \"org\": \"AS5467 Non state educational institution Educational Scientific and Experimental Center" +
                " of Moscow Institute of Physics and Technology\",\n" +
                "  \"postal\": \"101194\"\n}";
        InputStream stream = new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8));
        PowerMockito.when(urlMock.openStream()).thenReturn(stream);

        LatLng location = GeocodeWrapper.getCoordinatesByString("nearby");
        Assert.assertEquals(0.5, location.getLat(), 1.0E-08);
        Assert.assertEquals(0.25, location.getLng(), 1.0E-08);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetCoordinatesByStringNearby2() throws IOException {
        String data = "{\n \"ip\": \"93.175.31.92\",\n" +
                "  \"hostname\": \"No Hostname\",\n" +
                "  \"city\": \"Moscow\",\n" +
                "  \"region\": \"Moscow\",\n" +
                "  \"country\": \"RU\",\n" +
                "  \"loc\": \"0.5, 0.25\",\n" +
                "  \"org\": \"AS5467 Non state educational institution Educational Scientific and Experimental Center" +
                " of Moscow Institute of Physics and Technology\",\n" +
                "  \"postal\": \"101194\"\n";
        InputStream stream = new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8));
        PowerMockito.when(urlMock.openStream()).thenReturn(stream);
        GeocodeWrapper.getCoordinatesByString("nearby");
    }

}
