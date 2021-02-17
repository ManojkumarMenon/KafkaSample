package com.manoj.sample.kafkasample.streaming;

import java.util.List;

import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.processor.StateStore;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.QueryableStoreType;
import org.apache.kafka.streams.state.internals.StateStoreProvider;

public class StateStoreBuilder implements StateStoreProvider {

    @Override
    public <T> List<T> stores(String storeName, QueryableStoreType<T> queryableStoreType) {
        // TODO Auto-generated method stub
        return null;
    }

}

class MyKeyValueStore implements KeyValueStore<String, String> {

    @Override
    public String name() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void init(ProcessorContext context, StateStore root) {
        // TODO Auto-generated method stub

    }

    @Override
    public void flush() {
        // TODO Auto-generated method stub

    }

    @Override
    public void close() {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean persistent() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isOpen() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public String get(String key) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public KeyValueIterator<String, String> range(String from, String to) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public KeyValueIterator<String, String> all() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long approximateNumEntries() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void put(String key, String value) {
        // TODO Auto-generated method stub

    }

    @Override
    public String putIfAbsent(String key, String value) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void putAll(List<KeyValue<String, String>> entries) {
        // TODO Auto-generated method stub

    }

    @Override
    public String delete(String key) {
        // TODO Auto-generated method stub
        return null;
    }

}