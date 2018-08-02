package com;

import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import twitter4j.TwitterObjectFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import com.serializer.CustomStatus;

public class TwitterConsumer
{
    static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    static HTable hTable;
    static Properties hbaseProperties;

    static String topicName;
    static InputStream input;
    static Consumer<Long, CustomStatus> consumer;
    static Properties properties;
    static int counter = 0;

    public static void init() throws IOException
    {
        topicName = "twitter-streaming";
        input = null;
        consumer = null;

        HBaseClient hBaseClient = new HBaseClient();
        hTable = hBaseClient.gethTable();
        hbaseProperties = hBaseClient.getProperties();
        System.out.println("HBase init done.");
    }

    public static void run() throws IOException
    {
        try {
            properties = new Properties();
            input = ClassLoader.getSystemResourceAsStream("consumer.properties");
            properties.load(input);

            consumer = new KafkaConsumer<>(properties);
            consumer.subscribe(Arrays.asList(topicName));

            while (true)
            {
                ConsumerRecords<Long, CustomStatus> records = consumer.poll(100);
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

    public static void processRecordValue(CustomStatus status)
    {
        try
        {
            /*String text = listStatus.get(0);
            String user = "@" + listStatus.get(1);
            String createdAt = listStatus.get(2);*/

            System.out.print("Tweet " + (++counter) + " received, by " + status.getUser().getScreenName() + " ");

            LocalDateTime now = LocalDateTime.now();
            String rowKey = dtf.format(now);

            Put p = new Put(Bytes.toBytes(rowKey));

            p.addColumn(Bytes.toBytes(hbaseProperties.getProperty("columnFamily")),
                    Bytes.toBytes(hbaseProperties.getProperty("colTweet")),
                    //Bytes.toBytes(text));
                    Bytes.toBytes(status.getText()));

            p.addColumn(Bytes.toBytes(hbaseProperties.getProperty("columnFamily")),
                    Bytes.toBytes(hbaseProperties.getProperty("colHandle")),
                    //Bytes.toBytes(user));
                    Bytes.toBytes(status.getUser().getScreenName()));

            p.addColumn(Bytes.toBytes(hbaseProperties.getProperty("columnFamily")),
                    Bytes.toBytes(hbaseProperties.getProperty("colTimestamp")),
                    //Bytes.toBytes(createdAt));
                    Bytes.toBytes(status.getCreatedAt().toString()));

            /*if (listStatus.size() > 3)
            {
                p.addColumn(Bytes.toBytes(hbaseProperties.getProperty("columnFamily")),
                        Bytes.toBytes(hbaseProperties.getProperty("colLat")),
                        Bytes.toBytes(listStatus.get(3)));

                p.addColumn(Bytes.toBytes(hbaseProperties.getProperty("columnFamily")),
                        Bytes.toBytes(hbaseProperties.getProperty("colLong")),
                        Bytes.toBytes(listStatus.get(4)));

                System.out.println("with geo-location.");
            }*/

            hTable.put(p);
        }
        catch (Exception e)
        {
            System.out.println("Exception occurred.");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException
    {
        init();
        run();
    }
}
