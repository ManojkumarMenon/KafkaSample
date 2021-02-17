package com.manoj.sample.kafkasample.elasticsearch;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.http.HttpHost;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ElasticSearchConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(ElasticSearchConsumer.class);

    public static void main(String[] args) {

        RestHighLevelClient client = createHttpClient();

        IndexRequest request = new IndexRequest("twitter", "tweets");

        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.ByteArrayDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.ByteArrayDeserializer");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "SampleJavaConsumerForTweets1");

        try (KafkaConsumer<byte[], byte[]> consumer = new KafkaConsumer<>(props);) {
            List<String> list = new ArrayList<>();
            list.add("twitter_feeds");
            consumer.subscribe(list);
            int counter = 0;
            while (counter < 100) {
                ConsumerRecords<byte[], byte[]> record = consumer.poll(Duration.ofMillis(1000));
                record.forEach(s -> {
                    ConsumerRecord<byte[], byte[]> rec = s;
                    String key = new String(rec.key());
                    String value = new String(rec.value());

                    // String data = new
                    // StringBuilder().append("{\"").append(key).append("\":\"").append(value).append("\"}").toString();
                    request.source(value, XContentType.JSON);

                    try {
                        IndexResponse response = client.index(request, RequestOptions.DEFAULT);
                        LOGGER.info("Response is{}", response);
                    } catch (IOException e) {
                        LOGGER.error("Caught exception!!!");
                    }

                });
                counter++;
            }
        }
    }

    public static RestHighLevelClient createHttpClient() {
        HttpHost host = new HttpHost("localhost", 9200, "http");
        RestClientBuilder clientBldr = RestClient.builder(host);
        return new RestHighLevelClient(clientBldr);
    }

}
