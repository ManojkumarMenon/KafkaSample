package com.manoj.sample.kafkasample.twitter;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.Hosts;
import com.twitter.hbc.core.HttpHosts;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;

public class TwitterProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(TwitterProducer.class);

    private static final String CONSUMER_KEY = "UqhiQ3nmK4LQZc0dJU6rjGzvd";
    private static final String CONSUMER_SECRET = "ozVudflCIVBy4bkjV1JNhb3eya8QTq46lYwFlt7ddSPJcl6rpO";
    private static final String TOKEN = "2400144943-TQL3UOO8KmkZ9w07fhuDniC90FCWn34pSjKKrmr";
    private static final String SECRETS = "zBurLHncVP26QB2UqTzAq6zO7vjdxQjBOGKKD8GlpsuBi";

    public static void main(String[] args) {

        // Create twitter client
        BlockingQueue<String> msgQueue = new LinkedBlockingQueue<>(100000);

        Client hosebirdClient = new TwitterProducer().createTwitterClient(msgQueue);
        // Kafka producer
        // Attempts to establish a connection.
        hosebirdClient.connect();

        // Create kafka producer
        KafkaProducer<String, String> producer = createKafkaProducer();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {

            LOGGER.info("Shutdown hook called....");
            LOGGER.info("Closing twitter client...");
            hosebirdClient.stop();
            producer.close();
            LOGGER.info("Done!!!");
        }));

        // on a different thread, or multiple different threads....
        while (!hosebirdClient.isDone()) {
            String msg;
            try {
                msg = msgQueue.poll(10, TimeUnit.SECONDS);
                if (msg != null) {
                    LOGGER.info(msg);
                    producer.send(new ProducerRecord<String, String>("twitter_feeds", String.valueOf(System.currentTimeMillis()), msg),
                            new Callback() {

                                @Override
                                public void onCompletion(RecordMetadata metadata, Exception exception) {
                                    if (exception != null) {
                                        LOGGER.error("Caught exception sending message to kafka producer!!!!");
                                    }
                                }
                            });

                }
            } catch (InterruptedException e) {
                LOGGER.error("Caught exception !!!");
            }

        }

        // Send data to producer

    }

    public Client createTwitterClient(BlockingQueue<String> msgQueue) {

        /**
         * Set up your blocking queues: Be sure to size these properly based on
         * expected TPS of your stream
         */

        /**
         * Declare the host you want to connect to, the endpoint, and
         * authentication (basic auth or oauth)
         */
        Hosts hosebirdHosts = new HttpHosts(Constants.STREAM_HOST);
        StatusesFilterEndpoint hosebirdEndpoint = new StatusesFilterEndpoint();

        // Optional: set up some followings and track terms
        List<String> terms = Lists.newArrayList("bitcoin");
        hosebirdEndpoint.trackTerms(terms);

        // These secrets should be read from a config file
        Authentication hosebirdAuth = new OAuth1(CONSUMER_KEY, CONSUMER_SECRET, TOKEN, SECRETS);

        ClientBuilder builder = new ClientBuilder()
                .name("Hosebird-Client-01") // optional: mainly for the logs
                .hosts(hosebirdHosts)
                .authentication(hosebirdAuth)
                .endpoint(hosebirdEndpoint)
                .processor(new StringDelimitedProcessor(msgQueue));
        // .eventMessageQueue(eventQueue); // optional: use this if you
        // want to process client events

        Client hosebirdClient = builder.build();
        return hosebirdClient;

    }

    private static KafkaProducer<String, String> createKafkaProducer() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");

        return new KafkaProducer<>(props);
    }
}
