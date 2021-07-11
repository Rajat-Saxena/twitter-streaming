package com.twitter_streaming;

import com.google.gson.Gson;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import twitter4j.*;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;

public class TwitterProducer {

    static TwitterStream twitterStream;
    static Gson gson;
    static int tweetCounter;
    static Producer producer;
    static String topicName;

    public static void init() throws IOException {
        twitterStream = Utils.getTwitterStream();
        gson = new Gson();
        tweetCounter = 0;
        producer = Utils.getProducer();
        topicName = Utils.getKafkaTopic();
    }

    public static void run() {

        try {

            final LinkedBlockingQueue<Status> queue = new LinkedBlockingQueue<>(1000);
            StatusListener listener = new StatusListener() {
                @Override
                public void onStatus(Status status) {
                    if (status.getPlace() != null)
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
            double[][] locations = {{68.116667, 8.066667,}, {97.416667, 37.100000,}};
            filterQuery.locations(locations);
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
        }
    }


    public static void main(String[] args) throws IOException {
        init();
        run();
    }
}
