package com.fotile.scene.service;

import com.fotile.scene.prometheus.metrics.PlatformMetricsCounter;
import com.fotile.scene.prometheus.metrics.CustomizedMetrics;
import com.fotile.scene.prometheus.metrics.PlatformMetricsTimer;
import com.fotile.scene.threadpool.ThreadPoolFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Slf4j
@Service
public class UserService {

    private final DefaultMQProducer defaultMQProducer;

    public UserService(DefaultMQProducer defaultMQProducer) {
        this.defaultMQProducer = defaultMQProducer;
    }


    @PlatformMetricsCounter(
            value = CustomizedMetrics.USER,
            tags = {
                    "action", "add", "status", "ok", "user_id", "asd"
            }
    )
    public void addUser() {
        Message message = new Message();
        message.setBody("ckasdvnkkasjdbvhaks".getBytes(StandardCharsets.UTF_8));
        message.setTopic("FFFFF");
        message.setTags("HHHH");
        try {
            defaultMQProducer.send(message);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        log.info("hahahh add user");
    }

    @PlatformMetricsTimer(
            value = CustomizedMetrics.USER,
            tags = {
                    "action", "update", "status", "ok", "user_id", "asd"
            }
    )
    public void updateUser() {
        log.info("UPDATE USER!");
        ThreadPoolFactory.EXECUTOR_SERVICE.submit(()->{

            try{
                Thread.sleep(5000);
            }catch (Exception e) {
                log.error("sleep error!", e);
            }
        });
    }

}