package com.fotile.scene.prometheus.metrics;


import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Aspect
@Component
public class MeterAopConfiguration{

    private final MeterRegistry meterRegistry;

    private final ConcurrentMap<String, Counter> customerCounter;
    private final ConcurrentMap<String, Timer> customerTimer;


    public MeterAopConfiguration(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        this.customerCounter = new ConcurrentHashMap<>();
        this.customerTimer = new ConcurrentHashMap<>();
    }

    @Around("@annotation(com.fotile.scene.prometheus.metrics.PlatformMetricsTimer)")
    public Object aroundTimer(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取方法上的注解
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        PlatformMetricsTimer metricTime = method.getAnnotation(PlatformMetricsTimer.class);
        // 原方法执行结果
        Timer timer = recordTimerMetrics(metricTime);
        return timer.record(
                () -> {
                    try {
                        // 执行目标方法
                        return joinPoint.proceed();
                    } catch (Throwable e) {
                        // 记录计数器
                        throw new RuntimeException(e);
                    }
                }
        );
    }

    /**
     * 记录指标指标计数
     */
    private Timer recordTimerMetrics(PlatformMetricsTimer metricsTimer) {
        String[] tags = metricsTimer.tags();
        StringBuilder sb = new StringBuilder(metricsTimer.value().getMeterId());
        for (String tg : tags) {
            sb.append(":").append(tg);
        }
        String cacheKey = sb.toString();
        return customerTimer.computeIfAbsent(
                cacheKey, k -> Timer.builder(metricsTimer.value().getMeterId())
                        .tags(tags)
                        .description(metricsTimer.description())
                        .register(meterRegistry)
        );
    }


    @Around("@annotation(com.fotile.scene.prometheus.metrics.PlatformMetricsCounter)")
    public Object aroundCounter(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取方法上的注解
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        PlatformMetricsCounter metricCount = method.getAnnotation(PlatformMetricsCounter.class);
        // 原方法执行结果
        Object result;
        try {
            // 执行目标方法
            result = joinPoint.proceed();
        } finally {
            // 记录计数器
            recordCounterMetrics(metricCount);
        }

        return result;
    }


    /**
     * 记录指标指标计数
     */
    private void recordCounterMetrics(PlatformMetricsCounter metricCount) {
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
