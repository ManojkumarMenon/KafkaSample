package com.manoj.sample.kafkasample.producerconsumer;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.LoggerFactory;

public class StreamDataKafkaProducer {
    public static final org.slf4j.Logger logger = LoggerFactory.getLogger(StreamDataKafkaProducer.class);

    public static void main(String[] args) {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.ByteArraySerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.ByteArraySerializer");

        Runnable runnable = () -> {
            try (KafkaProducer<byte[], byte[]> producer = new KafkaProducer<>(props)) {
                int i = 100;
                while (i < 150) {

                    ProducerRecord<byte[], byte[]> record = new ProducerRecord<>("ada", ("Manoj" + i).getBytes(), ("Menon" + i).getBytes());
                    producer.send(record, (m, e) -> {

                        logger.info("Executing the callback. Message published to:- {} at partition: {} ", m.topic(), m.partition());

                    });
                    i++;
                }
            }
        };
        Thread thread = new Thread(runnable, "MyThread");
        thread.start();

    }

}
