package com.dance.me.common.storage;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;

@Service
@ConditionalOnProperty(name = "supabase.enabled", havingValue = "true")
public class SupabaseImageStorageService implements ImageStorageService {

    private static final Logger log = LoggerFactory.getLogger(SupabaseImageStorageService.class);

    @Value("${supabase.url}")
    private String supabaseUrl;

    @Value("${supabase.key}")
    private String serviceKey;

    @Value("${supabase.bucket}")
    private String bucket;

    private final RestClient restClient = RestClient.create();

    @Override
    public String upload(MultipartFile file, String folder) {
        try {
            String path = folder + "/" + UUID.randomUUID() + "_" + file.getOriginalFilename();

            restClient.post()
                    .uri(supabaseUrl + "/storage/v1/object/" + bucket + "/" + path)
                    .header("Authorization", "Bearer " + serviceKey)
                    .header("x-upsert", "true")
                    .contentType(MediaType.parseMediaType(file.getContentType()))
                    .body(file.getBytes())
                    .retrieve()
                    .toBodilessEntity();

            return supabaseUrl + "/storage/v1/object/public/" + bucket + "/" + path;
        } catch (Exception e) {
            log.error("Supabase upload failed: {}", e.getMessage(), e);
            return null;
        }
    }

    @Override
    public void delete(String imageUrl) {
        try {
            String prefix = "/storage/v1/object/public/" + bucket + "/";
            String path = imageUrl.substring(imageUrl.indexOf(prefix) + prefix.length());

            restClient.delete()
                    .uri(supabaseUrl + "/storage/v1/object/" + bucket + "/" + path)
                    .header("Authorization", "Bearer " + serviceKey)
                    .retrieve()
                    .toBodilessEntity();
        } catch (Exception e) {
            log.error("Supabase delete failed: {}", e.getMessage(), e);
        }
    }
}
