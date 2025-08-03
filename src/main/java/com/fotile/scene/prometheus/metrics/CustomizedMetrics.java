package com.fotile.scene.prometheus.metrics;

public enum CustomizedMetrics {

    USER("spe.user"),

    ROCKET_MQ_PRODUCER_MESSAGE("rocketmq.producer.message"),

    ;

    private final String meterId;

    CustomizedMetrics(String meterId) {
        this.meterId = meterId;
    }

    public String getMeterId() {
        return meterId;
    }
}

