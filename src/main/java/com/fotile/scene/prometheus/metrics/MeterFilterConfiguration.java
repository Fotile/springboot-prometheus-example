package com.fotile.scene.prometheus.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.config.MeterFilter;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Predicate;

@EnableAspectJAutoProxy
@Configuration
public class MeterFilterConfiguration {

    private final MeterRegistry meterRegistry;

    private final ConcurrentMap<String, Counter> customerCounter;

    public MeterFilterConfiguration(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        this.customerCounter = new ConcurrentHashMap<>();
    }

    @Bean
    public MeterFilter excludeNonBusinessUris(@Value("${application.management.ignore-uris}") List<String> ignoreMeterUris) {
        if (CollectionUtils.isEmpty(ignoreMeterUris)) {
            throw new RuntimeException("build MeterFilter failed! the ignoreMeterUris is NULL or Empty");
        }
        // 忽略指定路径的信息统计
        Predicate<String> uriFilterPredicate = uri -> shouldIncludeUri(uri, ignoreMeterUris);
        return MeterFilter.deny(
                id -> uriFilterPredicate.test(id.getTag("uri"))
        );
    }

    private boolean shouldIncludeUri(String uri, List<String> ignoreMeterUris) {
        if (ObjectUtils.isEmpty(uri)) {
            return false;
        }
        for (String ignoreUri : ignoreMeterUris) {
            if (uri.startsWith(ignoreUri)) {
                return true;
            }
        }
        return false;
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
        StringBuilder sb = new StringBuilder(metricCount.value().getTag());
        for (String tg : tags) {
            sb.append(":").append(tg);
        }
        String cacheKey = sb.toString();
        Counter theCounter = customerCounter.computeIfAbsent(
                cacheKey, k -> Counter.builder(metricCount.value().name())
                        .tags(tags)
                        .description(metricCount.description())
                        .register(meterRegistry)
        );
        theCounter.increment();

    }


}
