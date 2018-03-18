package com.test;

public class KafkaConsumerProducerDemo {
    public static void main(String[] args) {
        boolean isAsync = true;
        Producer producerThread = new Producer(KafkaProperties.TOPIC, isAsync);
        producerThread.start();

        ConsumerPartition consumerThread = new ConsumerPartition(KafkaProperties.TOPIC);
        consumerThread.start();

    }
}