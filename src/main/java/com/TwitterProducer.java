package com;

import com.google.gson.Gson;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;
import java.util.concurrent.LinkedBlockingQueue;

public class TwitterProducer {
    public static void run() throws IOException
    {
        String topicName = "twitter-streaming";
        InputStream input = null;
        Producer<String, Status> producer = null;
        TwitterStream twitterStream = null;
        TwitterClient twitterClient = new TwitterClient();
        int tweetCounter = 0;
        ArrayList<String> list;

        try {
            Properties properties = new Properties();
            InputStream is = ClassLoader.getSystemResourceAsStream("producer.properties");
            properties.load(is);

            producer = new KafkaProducer<>(properties);

            ConfigurationBuilder cb = new ConfigurationBuilder();
            cb.setDebugEnabled(true);
            cb.setOAuthConsumerKey(twitterClient.getConsumerKey());
            cb.setOAuthConsumerSecret(twitterClient.getConsumerSecret());
            cb.setOAuthAccessToken(twitterClient.getAccessToken());
            cb.setOAuthAccessTokenSecret(twitterClient.getAccessTokenSecret());

            final LinkedBlockingQueue<Status> queue = new LinkedBlockingQueue<>(1000);
            twitterStream = new TwitterStreamFactory(cb.build()).getInstance();
            StatusListener listener = new StatusListener() {
                @Override
                public void onStatus(Status status) {
                    if (status.getGeoLocation() != null)
                        System.out.println(status.getGeoLocation().getLatitude() + "," +
                        status.getGeoLocation().getLongitude());
                    queue.offer(status);
                }

                @Override
                public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
                    System.out.println("onDeletionNotice");
                }

                @Override
                public void onTrackLimitationNotice(int i) {
                    System.out.println("onTrackLimitationNotice");
                }

                @Override
                public void onScrubGeo(long l, long l1) {
                    System.out.println("onScrubGeo");
                }

                @Override
                public void onStallWarning(StallWarning stallWarning) {
                    System.out.println("onStallWarning");
                }

                @Override
                public void onException(Exception e) {
                    e.printStackTrace();
                }
            };

            FilterQuery filterQuery = new FilterQuery();
            String filterKeywords[] = {"MUFC", "mufc"};
            String filterLang[] = {"en"};
            filterQuery.track(filterKeywords);
            filterQuery.language(filterLang);

            twitterStream.addListener(listener);
            twitterStream.filter(filterQuery);

            while (true)
            {
                Status status = queue.poll();

                if (status == null) {
                    Thread.sleep(100);
                }
                else
                {
                    /*list = new ArrayList<>();
                    list.add(status.getText());
                    list.add(status.getUser().getScreenName());
                    list.add(status.getCreatedAt().toString());

                    if (status.getGeoLocation() != null)
                    {
                        list.add(String.valueOf(status.getGeoLocation().getLatitude()));
                        list.add(String.valueOf(status.getGeoLocation().getLongitude()));
                    }*/

                    producer.send(new ProducerRecord<>(topicName, status));
                    System.out.println("Tweet " + (++tweetCounter) + " sent, by @" + status.getUser().getScreenName());
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally {
            producer.close();
            twitterStream.shutdown();
            input.close();
        }
    }

    public static void main(String[] args) throws IOException
    {
        TwitterProducer.run();
    }
}
