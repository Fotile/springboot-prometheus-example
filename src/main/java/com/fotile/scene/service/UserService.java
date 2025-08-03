package com.fotile.scene.service;

import com.fotile.scene.prometheus.metrics.CounterMeta;
import com.fotile.scene.prometheus.metrics.CustomizedMetrics;
import org.springframework.stereotype.Service;

@Service
public class UserService {


    @CounterMeta(
          value =  CustomizedMetrics.ADD_USER,
            tags = {
                  "name=add", "status=ok"
            }
    )
    public void addUser() {

    }



}