package com.manoj.sample.kafkasample.streaming;

import org.apache.kafka.streams.processor.Processor;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.processor.StateStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FirstProcessor implements Processor<String, String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(FirstProcessor.class);

    private ProcessorContext ctxt;
    private String taskId;

    private StateStore store;

    @Override
    public void init(ProcessorContext context) {
        this.ctxt = context;
        taskId = ctxt.taskId().toString();
        // store = ctxt.getStateStore("StreamApp");
    }

    @Override
    public void process(String key, String value) {
        LOGGER.info("In first processor with taskId:{} and key :{} and value: {}.", taskId, key, value);
        ctxt.forward(key, value);
    }

    @Override
    public void close() {
    }

}
