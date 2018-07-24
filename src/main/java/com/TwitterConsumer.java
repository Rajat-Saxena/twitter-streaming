package com;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;

public class TwitterConsumer {
    public static void run() throws IOException {
        String topicName = "twitter-streaming";
        InputStream input = null;
        Consumer<Long, String> consumer = null;
        Properties properties;

        try {
            properties = new Properties();
            input = new FileInputStream("src/main/resources/consumer.properties");
            properties.load(input);

            consumer = new KafkaConsumer<>(properties);
            consumer.subscribe(Arrays.asList(topicName));

            while (true) {
                ConsumerRecords<Long, String> records = consumer.poll(100);
                records.forEach(record -> processRecordValue(record.value()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            consumer.close();
            input.close();
        }
    }

    public static void processRecordValue(String value)
    {
        System.out.println(value);
    }

    public static void main(String[] args) throws IOException
    {
        run();
    }
}
