package com.fotile.scene.prometheus.metrics;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = ElementType.METHOD)
@Documented
public @interface PlatformMetricsTimer {

    CustomizedMetrics value();

    String[] tags();


    String description() default "";

}
