package com.test;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;

import java.util.Arrays;
import java.util.Collections;
import java.util.Properties;

/**
 * 消费指定分区
 */
public class ConsumerPartition extends Thread {
    private final KafkaConsumer<Integer, String> consumer;
    private final String topic;

    public ConsumerPartition(String topic) {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaProperties.KAFKA_SERVER_URL + ":" + KafkaProperties.KAFKA_SERVER_PORT);
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
        //消费指定分区
        TopicPartition partition0 = new TopicPartition(this.topic, 0);
        TopicPartition partition1 = new TopicPartition(this.topic, 1);
        consumer.assign(Arrays.asList(partition0, partition1));
        //指定消费的偏移位置
        //consumer.seek(partition0, 5);
        //consumer.seek(partition1, 5);
        //consumer.seekToBeginning(Arrays.asList(partition0, partition1));
        //consumer.seekToEnd(Arrays.asList(partition0, partition1))
        int i = 1;
        while(true){
            //交替访问分区，控制每个的流量
            if(i % 5 == 0) {
                if(i % 10 == 0){
                    consumer.pause(Arrays.asList(partition0));
                    consumer.resume(Arrays.asList(partition1));
                }else{
                   consumer.pause(Arrays.asList(partition1));
                   consumer.resume(Arrays.asList(partition0));
                }
            }
           //查看分区当前偏移量位置
           // consumer.position(partition0)
           ConsumerRecords<Integer, String> records = consumer.poll(1000);
           for (ConsumerRecord<Integer, String> record : records) {
               System.out.println("Received message: (" + record.key() + ", " + record.value() + ") at offset " + record.offset() + " at partiction " + record.partition());
           }
           i++;
        }
    }
}
