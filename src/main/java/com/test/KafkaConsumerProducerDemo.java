package com.test;

/**
 *
 * 单机模式：
 * 只需启动一台kafkaServer、并且使用自带的zookeeper
 *
 * 注意：集群模式：
 *  开始启动时：集群中的kafkaServer 开始启动时为奇数，消费者才能够才能消费
 *  启动后：随意关闭kafkaServer 与 zookeeper 都不会影响生产与消费
 */
public class KafkaConsumerProducerDemo {
    public static void main(String[] args) {
        boolean isAsync = true;
        Producer producerThread = new Producer(KafkaProperties.TOPIC, isAsync);
        producerThread.start();

        //ConsumerOffsetUnAuto consumerThread = new ConsumerOffsetUnAuto(KafkaProperties.TOPIC);
        //consumerThread.start();

    }
}