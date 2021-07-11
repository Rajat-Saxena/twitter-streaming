package com.twitter_streaming;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

public class TwitterConsumer {
    static DateTimeFormatter tweetDttmFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy h:mm:ss a");

    static String topicName;
    static Consumer consumer;
    static Connection mySqlConnection;
    static int tweetCounter = 0;
    static JsonParser parser;

    public static void init() throws IOException {
        consumer = Utils.getConsumer();
        topicName = Utils.getKafkaTopic();
        mySqlConnection = Utils.getSqlConnection();
        parser = new JsonParser();

        System.out.println("Initialization done.");
    }

    public static void run() throws SQLException {
        try {
            consumer.subscribe(Arrays.asList(topicName));

            while (true) {
                ConsumerRecords<Long, String> records = consumer.poll(Duration.ofSeconds(1));
                records.forEach(record -> {
                    try {
                        processRecordValue(record.value());
                        System.out.println("Processed tweet #" + (++tweetCounter));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            consumer.close();
            mySqlConnection.close();
        }
    }

    public static void processRecordValue(String jsonMessage) {

        try {
            JsonElement jsonTree = parser.parse(jsonMessage);
            JsonObject jsonObject = jsonTree.getAsJsonObject();
            String tweet_id = jsonObject.get("id").getAsString();
            String created_at_string = jsonObject.get("createdAt").getAsString();
            Timestamp created_at = Timestamp.valueOf(LocalDateTime.from(tweetDttmFormatter.parse(created_at_string)));
            String screenName = jsonObject.getAsJsonObject("user").get("screenName").getAsString();
            String tweet_text = jsonObject.get("text").getAsString();
            String place_name = jsonObject.getAsJsonObject("place").get("name").getAsString();
            String place_country_code = jsonObject.getAsJsonObject("place").get("countryCode").getAsString();
            String place_country = jsonObject.getAsJsonObject("place").get("country").getAsString();
            String place_place_type = jsonObject.getAsJsonObject("place").get("placeType").getAsString();
            String place_url = jsonObject.getAsJsonObject("place").get("url").getAsString();
            String place_full_name = jsonObject.getAsJsonObject("place").get("fullName").getAsString();
            String place_bounding_box_type = jsonObject.getAsJsonObject("place").get("boundingBoxType").getAsString();
            JsonArray place_bounding_box_coordinates = jsonObject.getAsJsonObject("place").get("boundingBoxCoordinates").getAsJsonArray();
            String place_id = jsonObject.getAsJsonObject("place").get("id").getAsString();

            Statement stmt = mySqlConnection.createStatement();
            String sql = "INSERT INTO twitter_streaming VALUES ("
                    + tweet_id + ","
                    + "'" + created_at + "',"
                    + "'" + screenName + "',"
                    + "'" + tweet_text + "',"
                    + "'" + place_name + "',"
                    + "'" + place_country_code + "',"
                    + "'" + place_country + "',"
                    + "'" + place_place_type + "',"
                    + "'" + place_url + "',"
                    + "'" + place_full_name + "',"
                    + "'" + place_bounding_box_type + "',"
                    + "'" + " " + "',"
                    + "'" + place_id + "'"
                    + ")";

            stmt.executeUpdate(sql);
            System.out.println("Tweet saved.");
        } catch (Exception e) {
            System.out.println("Exception occurred.");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException, SQLException {
        init();
        run();
    }
}
