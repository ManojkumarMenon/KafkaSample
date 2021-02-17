package com.manoj.sample.kafkasample.producerconsumer;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.DescribeTopicsResult;
import org.apache.kafka.clients.admin.TopicDescription;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.TopicPartitionInfo;

public class KafkaAdminClient {

    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");

        try (AdminClient client = AdminClient.create(properties)) {

            DescribeTopicsResult result = client.describeTopics(Arrays.asList("stream-sink"));

            result.values().forEach((k, v) -> {
                try {
                    TopicDescription desc = v.get();

                    int size = desc.partitions().size();
                    System.out.println(String.format("#### Topic name: %s  and partition: %s", k, size));

                    List<TopicPartitionInfo> partitionInfo = desc.partitions();
                    partitionInfo.forEach(s -> {
                        System.out.println("Partion id:" + s.partition());
                        s.isr().forEach(i -> System.out.println(i.host()));

                        s.replicas().forEach(r -> {
                            System.out.println(r.host());
                        });
                    });
                } catch (Exception e) {
                    System.out.println("Caught exception with msg: " + e.getMessage());
                }
            });
        }
    }

}
