package com.twitter_streaming;

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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

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
            String place_id = jsonObject.getAsJsonObject("place").get("id").getAsString();

            List<double[]> bboxCoordinates = new ArrayList<>();
            Iterator<JsonElement> elementIterator = jsonObject.getAsJsonObject("place").get("boundingBoxCoordinates").getAsJsonArray().get(0).getAsJsonArray().iterator();
            while (elementIterator.hasNext()) {
                JsonElement obj = elementIterator.next();
                double latitude = ((JsonObject) obj).get("latitude").getAsDouble();
                double longitude = ((JsonObject) obj).get("longitude").getAsDouble();
                double[] coord = new double[]{latitude, longitude};
                bboxCoordinates.add(coord);
            }
            double place_bounding_box_lat_0 = bboxCoordinates.get(0)[0];
            double place_bounding_box_lat_1 = bboxCoordinates.get(1)[0];
            double place_bounding_box_lat_2 = bboxCoordinates.get(2)[0];
            double place_bounding_box_lat_3 = bboxCoordinates.get(3)[0];
            double place_bounding_box_long_0 = bboxCoordinates.get(0)[1];
            double place_bounding_box_long_1 = bboxCoordinates.get(1)[1];
            double place_bounding_box_long_2 = bboxCoordinates.get(2)[1];
            double place_bounding_box_long_3 = bboxCoordinates.get(3)[1];

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
                    + place_bounding_box_lat_0 + ","
                    + place_bounding_box_long_0 + ","
                    + place_bounding_box_lat_1 + ","
                    + place_bounding_box_long_1 + ","
                    + place_bounding_box_lat_2 + ","
                    + place_bounding_box_long_2 + ","
                    + place_bounding_box_lat_3 + ","
                    + place_bounding_box_long_3 + ","
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
