package com.manoj.sample.kafkasample.producerconsumer;

import java.util.Properties;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

/**
 * Sample class to send a record to the kafka single node cluster using the
 * kafka producer library.
 * 
 * @author mmenon
 *
 */
public class SingleDataKafkaProducer {

    /**
     * Main method to start the execution of the class.
     * 
     * @param args
     */
    public static void main(String[] args) {

        // Properties to be used by the kafka producer.
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.ByteArraySerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.ByteArraySerializer");

        // Logic to send data to the kafka cluster.
        try (KafkaProducer<byte[], byte[]> producer = new KafkaProducer<>(props)) {

            String key = "Manoj";
            String value = "Menon";

            ProducerRecord<byte[], byte[]> record = new ProducerRecord<>("ada", key.getBytes(), value.getBytes());
            producer.send(record, new MyKafkaCallback(key, value));
        }
    }

}

/**
 * Callback class which will be executed by kafka when the corresponding record
 * is sent to the topic.
 * 
 * @author mmenon
 *
 */
class MyKafkaCallback implements Callback {

    /**
     * the key to be sent
     */
    private String key;
    /**
     * the value to be sent
     */
    private String value;

    public MyKafkaCallback(String key, String value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public void onCompletion(RecordMetadata metadata, Exception exception) {
        System.out.println("Executing the callback....");
        System.out.println("The record metadata is ..." + metadata.partition() + "-" + metadata.topic());
        System.out.println("The key:value = " + key + ":" + value);
    }

}
