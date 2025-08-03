package com.fotile.scene.prometheus.metrics;


import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Aspect
@Component
public class MeterAopConfiguration {

    private final MeterRegistry meterRegistry;

    private final ConcurrentMap<String, Counter> customerCounter;

    public MeterAopConfiguration(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        this.customerCounter = new ConcurrentHashMap<>();
    }


    @Around("@annotation(com.fotile.scene.prometheus.metrics.CounterMeta)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取方法上的注解
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        CounterMeta metricCount = method.getAnnotation(CounterMeta.class);
        // 原方法执行结果
        Object result;
        try {
            // 执行目标方法
            result = joinPoint.proceed();
        } finally {
            // 记录计数器
            recordMetrics(metricCount);
        }

        return result;
    }


    /**
     * 记录指标指标计数
     */
    private void recordMetrics(CounterMeta metricCount) {
        String[] tags = metricCount.tags();
        StringBuilder sb = new StringBuilder(metricCount.value().getMeterId());
        for (String tg : tags) {
            sb.append(":").append(tg);
        }
        String cacheKey = sb.toString();
        Counter theCounter = customerCounter.computeIfAbsent(
                cacheKey, k -> Counter.builder(metricCount.value().getMeterId())
                        .tags(tags)
                        .description(metricCount.description())
                        .register(meterRegistry)
        );
        theCounter.increment();

    }


}
