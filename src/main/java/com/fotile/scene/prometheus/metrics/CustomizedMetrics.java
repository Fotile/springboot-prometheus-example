package com.fotile.scene.prometheus.metrics;

public enum CustomizedMetrics {

    ADD_USER("base.user.count")


    ;

    private final String tag;

    CustomizedMetrics(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }
}

