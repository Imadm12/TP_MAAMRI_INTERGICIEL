package main.java.com.uphf.kafka;

import java.util.Collections;
import java.util.Properties;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

public class PartitionedConsumer {
    public interface IKafkaConstants {
        public static String KAFKA_BROKERS = "localhost:9093";
        
        public static Integer MESSAGE_COUNT=100;
        
        public static String CLIENT_ID="client1";
        
        public static String TOPIC_NAME="HT-topic";
        
        public static String GROUP_ID_CONFIG="consumerGroup10";
        
        public static Integer MAX_NO_MESSAGE_FOUND_COUNT=100;
        
        public static String OFFSET_RESET_EARLIER="earliest";
        
        public static Integer MAX_POLL_RECORDS=1;}

    public static Consumer<String, String> createConsumer(int partition) {
        final Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, IKafkaConstants.KAFKA_BROKERS);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, IKafkaConstants.GROUP_ID_CONFIG);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, IKafkaConstants.MAX_POLL_RECORDS);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, IKafkaConstants.OFFSET_RESET_EARLIER);

        final Consumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.assign(Collections.singletonList(new org.apache.kafka.common.TopicPartition(IKafkaConstants.TOPIC_NAME, partition)));
        return consumer;
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Please provide the partition number as an argument.");
            System.exit(1);
        }

        int partition = Integer.parseInt(args[0]);
        Consumer<String, String> consumer = createConsumer(partition);
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(100);
            records.forEach(record -> {
                System.out.printf("Consumer Record:(%s, %s, %d, %d)\n",
                        record.key(), record.value(), record.partition(), record.offset());
            });
            consumer.commitAsync();
        }
    }
}
