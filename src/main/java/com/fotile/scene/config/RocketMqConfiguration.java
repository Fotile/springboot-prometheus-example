package com.fotile.scene.config;

import io.micrometer.core.instrument.MeterRegistry;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.hook.SendMessageHook;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RocketMqConfiguration {

    @Bean
    public DefaultMQProducer topic1Producer(MeterRegistry registry,
                                            @Value("${application.rocketmq.producer.topic1}") String topic,
                                            @Value("${rocketmq.name-server}") String producerServer) throws MQClientException {

        SpeRocketMqProducer producer = new SpeRocketMqProducer(topic, null);
        SendMessageHook hook = new SpeRocketmqProducerHook(registry, topic);
        producer.registerSendMessageHook(hook);

        producer.setNamesrvAddr(producerServer);
        producer.start();
        return producer;
    }


}
