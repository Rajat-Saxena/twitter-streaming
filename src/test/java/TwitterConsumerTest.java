import com.TwitterConsumer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

public class TwitterConsumerTest
{
    Properties properties;
    InputStream input;

    @Before
    public void setup() throws IOException
    {
        input = new FileInputStream("src/test/resources/consumerTest.properties");
        properties = new Properties();
        properties.load(input);
    }

    @Test
    public void processRecordValueTest() throws IOException
    {
        ArrayList<String> listGeo = new ArrayList<>();
        listGeo.add(properties.getProperty("text"));
        listGeo.add(properties.getProperty("user"));
        listGeo.add(properties.getProperty("createdAt"));
        listGeo.add(properties.getProperty("lat"));
        listGeo.add(properties.getProperty("lon"));

        //TwitterConsumer.processRecordValue(listGeo);

    }

    @After
    public void cleanup() throws IOException
    {
        input.close();
    }
}
