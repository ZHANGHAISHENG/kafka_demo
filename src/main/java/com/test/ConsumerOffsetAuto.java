package com.test;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Collections;
import java.util.Properties;

/**
 * 自动提交偏移量
 *
 *
 *消费者流量控制
 *   kafka支持动态控制消费流量，分别在future的poll(long)中使用pause(Collection) 和 resume(Collection) 来暂停消费指定分配的分区，重新开始消费指定暂停的分区。
 *
 *控制消费的位置
 *   kafka使用seek(TopicPartition, long)指定新的消费位置。用于查找服务器保留的最早和最新的offset的特殊的方法也可用（seekToBeginning(Collection) 和 seekToEnd(Collection)）。
 *
 */
public class ConsumerOffsetAuto extends Thread {
    private final KafkaConsumer<Integer, String> consumer;
    private final String topic;

    public ConsumerOffsetAuto(String topic) {
        Properties props = new Properties();
        //props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaProperties.KAFKA_SERVER_URL + ":" + KafkaProperties.KAFKA_SERVER_PORT);
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaProperties.CLUSTER_SERVER);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "DemoConsumer");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.IntegerDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        consumer = new KafkaConsumer<Integer, String>(props);
        this.topic = topic;
    }

    @Override
    public void run() {
        consumer.subscribe(Collections.singletonList(this.topic));
         /*
         consumer.subscribe(Collections.singletonList(this.topic), new ConsumerRebalanceListener() {
             public void onPartitionsRevoked(Collection<TopicPartition> collection) {
             }
             public void onPartitionsAssigned(Collection<TopicPartition> collection) {
             }
         });
         */
        while(true){
           ConsumerRecords<Integer, String> records = consumer.poll(1000);
           for (ConsumerRecord<Integer, String> record : records) {
               System.out.println("Received message: (" + record.key() + ", " + record.value() + ") at offset " + record.offset());
           }
        }
    }
}
