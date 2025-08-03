package com.fotile.scene.service;

import com.fotile.scene.prometheus.metrics.PlatformMetricsCounter;
import com.fotile.scene.prometheus.metrics.CustomizedMetrics;
import com.fotile.scene.prometheus.metrics.PlatformMetricsTimer;
import com.fotile.scene.threadpool.ThreadPoolFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService {


    @PlatformMetricsCounter(
            value = CustomizedMetrics.USER,
            tags = {
                    "action", "add", "status", "ok", "user_id", "asd"
            }
    )
    public void addUser() {


        log.info("hahahh add user");
    }

    @PlatformMetricsTimer(
            value = CustomizedMetrics.USER,
            tags = {
                    "action", "update", "status", "ok", "user_id", "asd"
            }
    )
    public void updateUser() {
        log.info("UPDATE USER!");
        ThreadPoolFactory.EXECUTOR_SERVICE.submit(()->{

            try{
                Thread.sleep(5000);
            }catch (Exception e) {
                log.error("sleep error!", e);
            }
        });
    }

}