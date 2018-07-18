package com;

import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;

public class TwitterConsumer {
    public void run() throws IOException {
        String topicName = "dummyKafkaTopic";
        InputStream input = null;
        KafkaConsumer consumer = null;
        Properties properties;

        try {
            properties = new Properties();
            input = new FileInputStream("src\\main\\resources\\consumer.properties");
            properties.load(input);

            consumer = new KafkaConsumer<>(properties);
            consumer.subscribe(Arrays.asList(topicName));


            while (true) {
                ConsumerRecords records = consumer.poll(100);
                records.forEach(record -> System.out.println(record));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            consumer.close();
            input.close();
        }

    }
}
