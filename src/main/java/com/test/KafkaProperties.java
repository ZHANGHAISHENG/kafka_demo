package com.test;

public class KafkaProperties {
    public static final String KAFKA_SERVER_URL = "localhost";
    public static final int KAFKA_SERVER_PORT = 9092;
    public static final String CLUSTER_SERVER = "localhost:9092,localhost:9093,localhost:9094";
    public static final int KAFKA_PRODUCER_BUFFER_SIZE = 64 * 1024;
    public static final int CONNECTION_TIMEOUT = 100000;
    public static final String TOPIC = "topic1";
    private KafkaProperties() {}
}