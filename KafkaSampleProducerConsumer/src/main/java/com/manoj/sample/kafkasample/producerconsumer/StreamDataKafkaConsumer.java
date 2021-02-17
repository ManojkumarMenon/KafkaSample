package com.manoj.sample.kafkasample.producerconsumer;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

public class StreamDataKafkaConsumer {

    public static void main(String[] args) {

        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.ByteArrayDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.ByteArrayDeserializer");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "SampleJavaConsumer");

        try (KafkaConsumer<byte[], byte[]> consumer = new KafkaConsumer<>(props);) {
            List<String> list = new ArrayList<>();
            list.add("ada");
            consumer.subscribe(list);
            while (true) {
                ConsumerRecords<byte[], byte[]> record = consumer.poll(Duration.ofMillis(1000));
                record.forEach(s -> {
                    ConsumerRecord<byte[], byte[]> rec = s;
                    String key = new String(rec.key());
                    String value = new String(rec.value());

                    System.out.println(key + ":" + value);
                });

            }
        }
    }

}
