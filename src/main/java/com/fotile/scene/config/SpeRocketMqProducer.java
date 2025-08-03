package com.fotile.scene.config;

import org.apache.rocketmq.client.hook.SendMessageHook;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.remoting.RPCHook;

public class SpeRocketMqProducer extends DefaultMQProducer {

    public SpeRocketMqProducer(final String producerGroup, RPCHook rpcHook) {

        super(null, producerGroup, rpcHook);
    }

    public void registerSendMessageHook(SendMessageHook hook) {
        super.defaultMQProducerImpl.registerSendMessageHook(hook);
    }

}
