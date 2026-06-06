package com.dance.me.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "app")
public class AppProperties {

    private String name = "DanceWithMe";
    private String baseUrl = "http://localhost:3002";
    private String frontendUrl = "http://localhost:3000";
}
