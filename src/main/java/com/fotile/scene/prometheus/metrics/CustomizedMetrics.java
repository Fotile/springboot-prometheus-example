package com.fotile.scene.prometheus.metrics;

public enum CustomizedMetrics {

    USER("spe.user")


    ;

    private final String meterId;

    CustomizedMetrics(String meterId) {
        this.meterId = meterId;
    }

    public String getMeterId() {
        return meterId;
    }
}

