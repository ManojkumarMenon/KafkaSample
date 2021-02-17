package com.manoj.sample.kafkasample.streaming;

import org.apache.kafka.streams.processor.Processor;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThirdProcessor implements Processor<String, String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ThirdProcessor.class);

    private ProcessorContext ctxt;
    private String taskId;

    @Override
    public void init(ProcessorContext context) {
        this.ctxt = context;
        taskId = ctxt.taskId().toString();
    }

    @Override
    public void process(String key, String value) {
        LOGGER.info("In third processor with taskId:{} and key :{} and value: {} ", taskId, key, value);
        ctxt.forward(key, value);
    }

    @Override
    public void close() {

    }

}
