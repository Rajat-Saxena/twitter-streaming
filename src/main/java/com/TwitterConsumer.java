package com;

import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import twitter4j.Status;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;
import java.util.StringTokenizer;

public class TwitterConsumer
{
    static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    static HTable hTable;
    static Properties hbaseProperties;

    static String topicName;
    static InputStream input;
    static Consumer<Long, String> consumer;
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
            InputStream is = ClassLoader.getSystemResourceAsStream("consumer.properties");
            properties.load(is);

            consumer = new KafkaConsumer<>(properties);
            consumer.subscribe(Arrays.asList(topicName));

            while (true)
            {
                ConsumerRecords<Long, String> records = consumer.poll(100);
                records.forEach(record -> {
                    try {
                        processRecordValue(record.value());
                    } catch (IOException e) {
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

    public static void processRecordValue(String status) throws IOException
    {
        try
        {
            System.out.print("Tweet " + (++counter) + " received.");

            String[] tokens = status.split("<<>>");
            String text = tokens[0];
            String user = "@" + tokens[1];
            String createdAt = tokens[2];

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

            hTable.put(p);
            System.out.println("Written to HBase");
        }
        catch (Exception e)
        {
            System.out.println("Exception occurred.");
        }
    }

    public static void main(String[] args) throws IOException
    {
        init();
        run();
    }
}
