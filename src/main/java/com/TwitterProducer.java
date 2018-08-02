package com;

import com.google.gson.Gson;
import com.serializer.CustomStatus;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.LinkedBlockingQueue;

public class TwitterProducer {
    public static void run() throws IOException {
        String topicName = "twitter-streaming";
        InputStream input = null;
        Producer<String, CustomStatus> producer = null;
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

            while (true) {
                Status status = queue.poll();
                CustomStatus customStatus = createCustomStatus(status);

                if (status == null) {
                    Thread.sleep(100);
                } else {
                    producer.send(new ProducerRecord<>(topicName, customStatus));
                    System.out.println("Tweet " + (++tweetCounter) + " sent, by @" + customStatus.getUser().getScreenName());
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

    public static CustomStatus createCustomStatus(Status status)
    {
        CustomStatus customStatus = new CustomStatus();

        if (status != null)
        {
            if (status.getURLEntities() != null)
                customStatus.setURLEntities(status.getURLEntities());

            if (status.getHashtagEntities() != null)
                customStatus.setHashtagEntities(status.getHashtagEntities());

            if (status.getMediaEntities() != null)
                customStatus.setMediaEntities(status.getMediaEntities());

            if (status.getSymbolEntities() != null)
                customStatus.setSymbolEntities(status.getSymbolEntities());

            if (status.getUserMentionEntities() != null)
                customStatus.setUserMentionEntities(status.getUserMentionEntities());

            //if (status.getAccessLevel() != null)
            customStatus.setAccessLevel(status.getAccessLevel());

            if (status.getRateLimitStatus() != null)
                customStatus.setRateLimitStatus(status.getRateLimitStatus());

            if (status.getText() != null)
                customStatus.setText(status.getText());

            if (status.getCreatedAt() != null)
                customStatus.setCreatedAt(status.getCreatedAt());

            if (status.getUser() != null)
                customStatus.setUser(status.getUser());

            //if (status.getId() != null)
            customStatus.setId(status.getId());

            //if (status.getDisplayTextRangeStart() != null)
            customStatus.setDisplayTextRangeStart(status.getDisplayTextRangeStart());

            //if (status.getDisplayTextRangeEnd() != null)
            customStatus.setDisplayTextRangeEnd(status.getDisplayTextRangeEnd());

            if (status.getSource() != null)
                customStatus.setSource(status.getSource());

            // if (status.isTruncated() != null)
            customStatus.setTruncated(status.isTruncated());

            ///if (status.getInReplyToStatusId() != null)
            customStatus.setInReplyToStatusId(status.getInReplyToStatusId());

            //if (status.getInReplyToUserId() != null)
            customStatus.setInReplyToUserId(status.getInReplyToUserId());

            if (status.getInReplyToScreenName() != null)
                customStatus.setInReplyToScreenName(status.getInReplyToScreenName());

            if (status.getGeoLocation() != null)
                customStatus.setGeoLocation(status.getGeoLocation());

            if (status.getPlace() != null)
                customStatus.setPlace(status.getPlace());

            //if (status.isFavorited() != null)
            customStatus.setFavorited(status.isFavorited());

            //if (status.isRetweeted() != null)
            customStatus.setRetweeted(status.isRetweeted());

            //if (status.getFavoriteCount() != null)
            customStatus.setFavoriteCount(status.getFavoriteCount());

            //if (status.isRetweet() != null)
            customStatus.setRetweet(status.isRetweet());

            if (status.getRetweetedStatus() != null)
                customStatus.setRetweetedStatus(status.getRetweetedStatus());

            if (status.getContributors() != null)
                customStatus.setContributors(status.getContributors());

            //if (status.getRetweetCount() != null)
            customStatus.setRetweetCount(status.getRetweetCount());

            //if (status.isRetweetedByMe() != null)
            customStatus.setRetweetedByMe(status.isRetweetedByMe());

            //if (status.getCurrentUserRetweetId() != null)
            customStatus.setCurrentUserRetweetId(status.getCurrentUserRetweetId());

            // if (status.isPossiblySensitive() != null)
            customStatus.setPossiblySensitive(status.isPossiblySensitive());

            if (status.getLang() != null)
                customStatus.setLang(status.getLang());

            if (status.getScopes() != null)
                customStatus.setScopes(status.getScopes());

            if (status.getWithheldInCountries() != null)
                customStatus.setWithheldInCountries(status.getWithheldInCountries());

            //if (status.getQuotedStatusId() != null)
            customStatus.setQuotedStatusId(status.getQuotedStatusId());

            if (status.getQuotedStatus() != null)
                customStatus.setQuotedStatus(status.getQuotedStatus());
        }

        return customStatus;
    }

    public static void main(String[] args) throws IOException {
        TwitterProducer.run();
    }
}
