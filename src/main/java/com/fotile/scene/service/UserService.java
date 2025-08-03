package com.fotile.scene.service;

import com.fotile.scene.prometheus.metrics.CounterMeta;
import com.fotile.scene.prometheus.metrics.CustomizedMetrics;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
@Slf4j
@Service
public class UserService {


    @CounterMeta(
          value =  CustomizedMetrics.ADD_USER,
            tags = {
                  "status", "ok", "user_id","asd"
          }
    )
    public void addUser() {


        log.info("hahahh add user");
    }



}