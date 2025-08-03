package com.fotile.scene.prometheus.metrics;

public enum CustomizedMetrics {

    ADD_USER("spe.user.add")


    ;

    private final String meterId;

    CustomizedMetrics(String meterId) {
        this.meterId = meterId;
    }

    public String getMeterId() {
        return meterId;
    }
}

