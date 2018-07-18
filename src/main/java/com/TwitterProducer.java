package com;

import com.google.common.collect.Lists;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TwitterProducer {
    public static void run() throws IOException
    {
        String topicName = "dummyKafkaTopic";
        InputStream input = null;
        Producer producer;

        try {
            Properties properties = new Properties();
            input = new FileInputStream("src\\main\\resources\\producer.properties");
            properties.load(input);

            producer = new KafkaProducer<>(properties);

            BlockingQueue<String> queue = new LinkedBlockingQueue<>(10000);
            StatusesFilterEndpoint endpoint = new StatusesFilterEndpoint();
            endpoint.trackTerms(Lists.newArrayList("twitterapi", "#mufc"));

            Client twitterClient = TwitterClient.run(endpoint, queue);
            twitterClient.connect();

            for (int tweets = 1; tweets < 1000; tweets++)
            {
                String message = queue.take();
                System.out.println(message);
                producer.send(new ProducerRecord<>(topicName, message ));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally {
            input.close();
        }
    }

    public static void main(String[] args) throws IOException
    {
        TwitterProducer.run();
    }
}
