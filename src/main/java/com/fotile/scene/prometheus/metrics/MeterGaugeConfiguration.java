package com.fotile.scene.prometheus.metrics;

import com.fotile.scene.threadpool.ThreadPoolFactory;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;


@Configuration
public class MeterGaugeConfiguration {

    @Component
    public static class GaugeStation {

        private final MeterRegistry meterRegistry;

        public GaugeStation(MeterRegistry meterRegistry) {
            this.meterRegistry = meterRegistry;
        }

        @PostConstruct
        public void initGauge() {
            Gauge.builder("spe.execute.task",
                    ThreadPoolFactory.EXECUTOR_SERVICE::getTaskCount)
                    .tags("condition",CustomizedMetrics.USER.getMeterId())
                    .description("thread-pool-task info")
                    .register(meterRegistry);
        }


    }


}
