package com.test;

public class KafkaProperties {
    public static final String KAFKA_SERVER_URL = "192.168.0.104";
    public static final int KAFKA_SERVER_PORT = 9092;
    //public static final String CLUSTER_SERVER = "192.168.0.104:9092,192.168.0.104:9093,192.168.0.104:9094";
    public static final String CLUSTER_SERVER = "192.168.0.104:9092"; //单机
    public static final int KAFKA_PRODUCER_BUFFER_SIZE = 64 * 1024;
    public static final int CONNECTION_TIMEOUT = 100000;
    public static final String TOPIC = "topic1";
    private KafkaProperties() {}
}