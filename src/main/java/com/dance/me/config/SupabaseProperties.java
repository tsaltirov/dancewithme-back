package com.dance.me.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "supabase")
public class SupabaseProperties {

    private boolean enabled = false;
    private String url;
    private String key;
    private String bucket;
}
