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


}
