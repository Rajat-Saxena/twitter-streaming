package com;

import com.google.gson.Gson;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.json.DataObjectFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.LinkedBlockingQueue;

public class TwitterProducer {
    public static void run() throws IOException {
        String topicName = "twitter-streaming";
        InputStream input = null;
        Producer<String, String> producer = null;
        TwitterStream twitterStream = null;
        TwitterClient twitterClient = new TwitterClient();
        Gson gson = new Gson();

        int tweetCounter = 0;

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
            cb.setJSONStoreEnabled(true);

            final LinkedBlockingQueue<Status> queue = new LinkedBlockingQueue<>(1000);
            twitterStream = new TwitterStreamFactory(cb.build()).getInstance();
            StatusListener listener = new StatusListener() {
                @Override
                public void onStatus(Status status) {
                    if (status.getUser().getLocation() != null)
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
            double[][] locations = { {68.116667, 8.066667,}, { 97.416667,37.100000,} };
            filterQuery.locations(locations);
            filterQuery.track(filterKeywords);
            //filterQuery.language(filterLang);

            twitterStream.addListener(listener);
            twitterStream.filter(filterQuery);

            while (true) {
                Status status = queue.poll();

                if (status == null) {
                    Thread.sleep(100);
                } else {
                    String jsonStatus = gson.toJson(status);

                    producer.send(new ProducerRecord<>(topicName, jsonStatus));
                    System.out.println("Tweet " + (++tweetCounter) + " sent, by @" + status.getUser().getScreenName());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            producer.close();
            twitterStream.shutdown();
            input.close();
        }
    }


    public static void main(String[] args) throws IOException {
        TwitterProducer.run();
    }
}
