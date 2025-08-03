package com.fotile.scene.config;

import com.fotile.scene.prometheus.metrics.CustomizedMetrics;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.apache.rocketmq.client.hook.SendMessageContext;
import org.apache.rocketmq.client.hook.SendMessageHook;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.client.trace.TraceContext;

import java.util.concurrent.TimeUnit;

public class SpeRocketmqProducerHook implements SendMessageHook {

    private final Counter sendTotal;
    private final Counter sendFailure;
    private final Timer sendLatency;

    public SpeRocketmqProducerHook(MeterRegistry meterRegistry, String topic) {

        this.sendTotal = Counter.builder(CustomizedMetrics.ROCKET_MQ_PRODUCER_MESSAGE.getMeterId())
                .tag("condition", "total")
                .tag("topic", topic)
                .register(meterRegistry);

        this.sendFailure = Counter.builder(CustomizedMetrics.ROCKET_MQ_PRODUCER_MESSAGE.getMeterId())
                .tag("condition", "failure")
                .tag("topic", topic)
                .register(meterRegistry);

        this.sendLatency = Timer.builder(CustomizedMetrics.ROCKET_MQ_PRODUCER_MESSAGE.getMeterId())
                .tag("condition", "latency")
                .tag("topic", topic)
                .register(meterRegistry);
    }

    @Override
    public String hookName() {
        return "SPE-ROCKETMQ-PRODUCER-HOOK";
    }

    @Override
    public void sendMessageBefore(SendMessageContext context) {
        // 记录开始时间（用于计算耗时）
        TraceContext traceContext = new TraceContext();
        traceContext.setTimeStamp(System.nanoTime());
        context.setMqTraceContext(traceContext);
    }

    @Override
    public void sendMessageAfter(SendMessageContext context) {
        TraceContext mqTraceContext = (TraceContext) context.getMqTraceContext();
        long cost = System.nanoTime() - mqTraceContext.getTimeStamp();
        this.sendLatency.record(cost, TimeUnit.NANOSECONDS);
        this.sendTotal.increment();
        SendResult sendResult = context.getSendResult();
        if (!SendStatus.SEND_OK.equals(sendResult.getSendStatus())) {
            this.sendFailure.increment();
        }
    }
}
