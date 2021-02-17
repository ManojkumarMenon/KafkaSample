package com.manoj.sample.kafkasample.streaming;

import java.util.Properties;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainClass {
    private static final Logger LOGGER = LoggerFactory.getLogger(MainClass.class);
    private static KafkaStreams streams = null;

    public static void main(String[] args) {

        Runtime.getRuntime().addShutdownHook(new ShutdownThread(streams));

        Topology topology = new Topology();
        topology.addSource("source", "twitter_feeds");
        topology.addProcessor("FirstProcessor", () -> new FirstProcessor(), "source");
        topology.addProcessor("SecondProcessor", () -> new SecondProcessor(), "FirstProcessor");
        topology.addProcessor("ThirdProcessor", () -> new ThirdProcessor(), "SecondProcessor");
        topology.addProcessor("LastProcessor", () -> new LastProcessor(), "ThirdProcessor");
        topology.addSink("sink", "stream-sink", "ThirdProcessor");

        // topology.addStateStore(Stores., "FirstProcessor");

        try {
            streams = new KafkaStreams(topology, getProperties());
            streams.start();

        } catch (Exception e) {
            LOGGER.error("Caught exception!!!");
        }

    }

    private static Properties getProperties() {
        Properties props = new Properties();
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(StreamsConfig.NUM_STREAM_THREADS_CONFIG, 2);
        props.put(StreamsConfig.REPLICATION_FACTOR_CONFIG, 2);
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "StreamingApp8");
        return props;
    }

}

class ShutdownThread extends Thread {
    private static final Logger LOGGER = LoggerFactory.getLogger(MainClass.class);

    private KafkaStreams streams = null;

    public ShutdownThread(KafkaStreams streams) {

        this.streams = streams;
    }

    @Override
    public void run() {
        LOGGER.info("Closing stream ....");

        if (streams != null) {
            streams.cleanUp();
            streams.close();
        }
    }

}
