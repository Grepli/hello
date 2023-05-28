package com.mzl.network.controller;

import com.mzl.network.entity.NetworkResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NetWorkController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @PostMapping("/network")
    public NetworkResponse networkResponse(){
        logger.info("he");
        return NetworkResponse.builder()
                .port(1800)
                .success(true)
                .build();
    }
}
