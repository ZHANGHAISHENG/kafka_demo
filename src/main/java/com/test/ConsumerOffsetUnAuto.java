package com.test;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;

import java.util.*;

/**
 * 手动提交偏移量
 */
public class ConsumerOffsetUnAuto extends Thread {
    private final KafkaConsumer<Integer, String> consumer;
    private final String topic;

    public ConsumerOffsetUnAuto(String topic) {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaProperties.KAFKA_SERVER_URL + ":" + KafkaProperties.KAFKA_SERVER_PORT);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "DemoConsumer");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false"); //设置为手动提交
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.IntegerDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        consumer = new KafkaConsumer<Integer, String>(props);
        this.topic = topic;
    }

    @Override
    /*public void run() {
        consumer.subscribe(Collections.singletonList(this.topic));
        final int minBatchSize = 10;
        List<ConsumerRecord<Integer, String>> buffer = new ArrayList<ConsumerRecord<Integer, String>>();
        while(true){
           ConsumerRecords<Integer, String> records = consumer.poll(1000);
           for (ConsumerRecord<Integer, String> record : records) {
               buffer.add(record);
               System.out.println("Received message: (" + record.key() + ", " + record.value() + ") at offset " + record.offset());
           }
            if (buffer.size() >= minBatchSize) {
                //insertIntoDb(buffer);
                consumer.commitSync();
                buffer.clear();
            }
        }
    }*/

    /**处理完每个分区后再提交**/
    public void run() {
        consumer.subscribe(Collections.singletonList(this.topic));
        while(true){
            ConsumerRecords<Integer, String> records = consumer.poll(1000);
            for (TopicPartition partition : records.partitions()) {
                List<ConsumerRecord<Integer, String>> partitionRecords = records.records(partition);
                for (ConsumerRecord<Integer, String> record : partitionRecords) {
                    System.out.println("Received message: (" + record.key() + ", " + record.value() + ") at offset " + record.offset());
                }
                long lastOffset = partitionRecords.get(partitionRecords.size() - 1).offset();
                //consumer.commitSync(Collections.singletonMap(partition, new OffsetAndMetadata(lastOffset + 1)));
                //已提交的offset应始终是你的程序将读取的下一条消息的offset。因此，调用commitSync（offsets）时，你应该加1个到最后处理的消息的offset
            }
        }
    }
}
