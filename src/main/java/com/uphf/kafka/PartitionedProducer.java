package main.java.com.uphf.kafka;

import java.util.Properties;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

public class PartitionedProducer {
    public interface IKafkaConstants {
        public static String KAFKA_BROKERS = "localhost:9093";
        
        public static Integer MESSAGE_COUNT=100;
        
        public static String CLIENT_ID="client1";
        
        public static String TOPIC_NAME="HT-topic";
        
        public static String GROUP_ID_CONFIG="consumerGroup10";
        
        public static Integer MAX_NO_MESSAGE_FOUND_COUNT=100;
        
        public static String OFFSET_RESET_EARLIER="earliest";
        
        public static Integer MAX_POLL_RECORDS=1;}

    public static Producer<String, String> createProducer() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, IKafkaConstants.KAFKA_BROKERS);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, IKafkaConstants.CLIENT_ID);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        return new KafkaProducer<>(props);
    }

    public static void main(String[] args) {
        Producer<String, String> producer = createProducer();
        try {
            for (int i = 0; i < 10; i++) {
                String key = "key" + i;
                String value = "value" + i;
                int partition = i % 3; // Distribue les messages entre partitions 0, 1, 2
                ProducerRecord<String, String> record = new ProducerRecord<>(IKafkaConstants.TOPIC_NAME, partition, key, value);
                producer.send(record);
                System.out.println("Message sent successfully to partition " + partition);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            producer.close();
        }
    }
}
