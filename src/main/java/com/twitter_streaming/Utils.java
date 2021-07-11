package com.twitter_streaming;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Base64;
import java.util.Properties;

public class Utils {

    public static String getKafkaTopic() {
        String topicName = "dHdpdHRlci1zdHJlYW1pbmc=";
        return new String(Base64.getDecoder().decode(topicName));
    }

    public static TwitterStream getTwitterStream() throws IOException {

        Properties twitterProperties = new Properties();
        InputStream input = ClassLoader.getSystemResourceAsStream("twitter.properties");
        twitterProperties.load(input);

        String consumerKey = new String(Base64.getDecoder().decode(twitterProperties.getProperty("consumerKey")));
        String consumerSecret = new String(Base64.getDecoder().decode(twitterProperties.getProperty("consumerSecret")));
        String accessToken = new String(Base64.getDecoder().decode(twitterProperties.getProperty("accessToken")));
        String accessTokenSecret = new String(Base64.getDecoder().decode(twitterProperties.getProperty("accessTokenSecret")));

        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true);
        cb.setOAuthConsumerKey(consumerKey);
        cb.setOAuthConsumerSecret(consumerSecret);
        cb.setOAuthAccessToken(accessToken);
        cb.setOAuthAccessTokenSecret(accessTokenSecret);
        cb.setJSONStoreEnabled(true);

        TwitterStream twitterStream = new TwitterStreamFactory(cb.build()).getInstance();

        return twitterStream;
    }

    public static Producer getProducer() throws IOException {
        Properties producerProperties = new Properties();
        InputStream is = ClassLoader.getSystemResourceAsStream("producer.properties");
        producerProperties.load(is);
        Producer<String, String> producer = new KafkaProducer<>(producerProperties);

        return producer;
    }

    public static Consumer getConsumer() throws IOException {
        Properties consumerProperties = new Properties();
        InputStream input = ClassLoader.getSystemResourceAsStream("consumer.properties");
        consumerProperties.load(input);

        Consumer<Long, String> consumer = new KafkaConsumer<>(consumerProperties);
        return consumer;
    }

    public static Connection getSqlConnection() {
        try {
            Properties sqlProperties = new Properties();
            InputStream input = ClassLoader.getSystemResourceAsStream("sql.properties");
            sqlProperties.load(input);

            String databaseUrl = new String(Base64.getDecoder().decode(sqlProperties.getProperty("database_url")));
            String databaseName = new String(Base64.getDecoder().decode(sqlProperties.getProperty("database_name")));
            String databaseUsername = new String(Base64.getDecoder().decode(sqlProperties.getProperty("databaseUsername")));
            String databasePassword = new String(Base64.getDecoder().decode(sqlProperties.getProperty("databasePassword")));

            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(databaseUrl + databaseName, databaseUsername, databasePassword);
            return con;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
}
