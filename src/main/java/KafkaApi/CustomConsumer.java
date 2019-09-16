package KafkaApi;


import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.MessageAndMetadata;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class CustomConsumer {
    private static final String topic="hzz";
    private static final Integer threads=2;
    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put("zookeeper.connect", "192.168.159.149:2181/kafka");

        properties.put("auto.offset.reset", "smallest");

        properties.put("group.id","hzz");

        ConsumerConfig consumerConfig = new ConsumerConfig(properties);

        ConsumerConnector consumerConnector = Consumer.createJavaConsumerConnector(consumerConfig);

        //指定消费的topic信息
        HashMap<String,Integer> hashMap = new HashMap<>();

        hashMap.put(topic, threads);

        Map<String, List<KafkaStream<byte[],byte[]>>> messageMap = consumerConnector.createMessageStreams(hashMap);

        //解析消息
        //获取相应topic对应的消息流
        List<KafkaStream<byte[], byte[]>> kafkaStreams = messageMap.get(topic);

        for(KafkaStream<byte[], byte[]> ka:kafkaStreams){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for(MessageAndMetadata<byte[], byte[]> mm:ka){
                        System.out.println(new String(mm.message()));
                    }
                }
            }).start();
        }

    }
}
