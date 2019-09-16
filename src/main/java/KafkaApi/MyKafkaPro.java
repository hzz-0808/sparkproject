package KafkaApi;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

import static sun.misc.Version.println;

public class MyKafkaPro {
    public static void main(String[] args) {
        Properties properties = new Properties();
        //设置kafka服务器的IP，端口
        properties.put("bootstrap.servers", "192.168.159.149:9092");

        properties.put("ack", "all");

        properties.put("retries", 1);

        properties.put("batch.size", 16384);

        properties.put("buffer.memory",33554432);
        //指定消息发送的序列化方式
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        //创建一个生产者对象
//        KafkaProducer kafkaProducer = new KafkaProducer<String,String>(properties);
        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);
        for(int i=0;i<10;i++){
            System.out.println(i);
            producer.send(new ProducerRecord<String, String>("first","kafka"+1));
        }

        producer.close();
    }
}
