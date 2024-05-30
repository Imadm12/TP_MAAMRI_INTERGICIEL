package com.uphf.kafka;

import java.util.Properties;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

public class ProducerCreator {

    public static Producer<String, String> createProducer() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9093");  // Updated to SSL port
        props.put(ProducerConfig.CLIENT_ID_CONFIG, IKafkaConstants.CLIENT_ID);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());


        // SSL Configuration
        props.put("security.protocol", "SSL");
        props.put("ssl.truststore.location", "C:/Users/imadm/OneDrive/Bureau/test/kafka.server.truststore.jks");
        props.put("ssl.truststore.password", "123456");
        props.put("ssl.keystore.location", "C:/Users/imadm/OneDrive/Bureau/test/kafka.server.keystore.jks");
        props.put("ssl.keystore.password", "123456");
        props.put("ssl.key.password", "123456");


        return new KafkaProducer<>(props);
    }

    public static void main(String[] args) {
        Producer<String, String> producer = createProducer();
        ProducerRecord<String, String> record = new ProducerRecord<>(IKafkaConstants.TOPIC_NAME, "key", "value");
        try {
            producer.send(record);
            System.out.println("Message sent successfully");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            producer.close();
        }
    }
}
