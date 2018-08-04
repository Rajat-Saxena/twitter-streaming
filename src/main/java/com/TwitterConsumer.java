package com;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;

public class TwitterConsumer {
    static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    static HTable hTable;
    static Properties hbaseProperties;

    static String topicName;
    static InputStream input;
    static Consumer<Long, String> consumer;
    static Properties properties;
    static int counter = 0;

    public static void init() throws IOException {
        topicName = "twitter-streaming";
        input = null;
        consumer = null;

        HBaseClient hBaseClient = new HBaseClient();
        hTable = hBaseClient.gethTable();
        hbaseProperties = hBaseClient.getProperties();
        System.out.println("HBase init done.");
    }

    public static void run() throws IOException {
        try {
            properties = new Properties();
            input = ClassLoader.getSystemResourceAsStream("consumer.properties");
            properties.load(input);

            consumer = new KafkaConsumer<>(properties);
            consumer.subscribe(Arrays.asList(topicName));

            while (true) {
                ConsumerRecords<Long, String> records = consumer.poll(100);
                records.forEach(record -> {
                    try {
                        processRecordValue(record.value());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            consumer.close();
            input.close();
            hTable.close();
        }
    }

    public static void processRecordValue(String jsonMessage) {
        try {
            JSONObject jsonObject = new JSONObject(jsonMessage);

            String text = jsonObject.get("text").toString();
            String user = "@" + jsonObject.getJSONObject("user").get("screenName").toString();
            String createdAt = jsonObject.get("createdAt").toString();
            String location = jsonObject.getJSONObject("user").get("location").toString();

            System.out.print("Tweet " + (++counter) + " received, by " + user + " ");

            LocalDateTime now = LocalDateTime.now();
            String rowKey = dtf.format(now);

            Put p = new Put(Bytes.toBytes(rowKey));

            p.addColumn(Bytes.toBytes(hbaseProperties.getProperty("columnFamily")),
                    Bytes.toBytes(hbaseProperties.getProperty("colTweet")),
                    Bytes.toBytes(text));

            p.addColumn(Bytes.toBytes(hbaseProperties.getProperty("columnFamily")),
                    Bytes.toBytes(hbaseProperties.getProperty("colHandle")),
                    Bytes.toBytes(user));

            p.addColumn(Bytes.toBytes(hbaseProperties.getProperty("columnFamily")),
                    Bytes.toBytes(hbaseProperties.getProperty("colTimestamp")),
                    Bytes.toBytes(createdAt));

            p.addColumn(Bytes.toBytes(hbaseProperties.getProperty("columnFamily")),
                    Bytes.toBytes(hbaseProperties.getProperty("colLocation")),
                    Bytes.toBytes(location));

            if (jsonObject.has("geoLocation")) {
                String latitude = jsonObject.getJSONObject("geoLocation").get("latitude").toString();
                p.addColumn(Bytes.toBytes(hbaseProperties.getProperty("columnFamily")),
                        Bytes.toBytes(hbaseProperties.getProperty("colLat")),
                        Bytes.toBytes(latitude));

                String longitude = jsonObject.getJSONObject("geoLocation").get("longitude").toString();
                p.addColumn(Bytes.toBytes(hbaseProperties.getProperty("columnFamily")),
                        Bytes.toBytes(hbaseProperties.getProperty("colLong")),
                        Bytes.toBytes(longitude));

                System.out.print("with geolocation ");
            }

            hTable.put(p);
            System.out.println("and written to HBase.");

        } catch (Exception e) {
            System.out.println("Exception occurred.");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        init();
        run();
    }
}
