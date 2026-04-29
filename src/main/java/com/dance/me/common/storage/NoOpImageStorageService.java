package com.dance.me.common.storage;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

// Activo por defecto mientras Supabase no esté configurado.
// imageUrl se puede seguir guardando manualmente en los DTOs.
@Service
@ConditionalOnMissingBean(name = "supabaseImageStorageService")
public class NoOpImageStorageService implements ImageStorageService {

    @Override
    public String upload(MultipartFile file, String folder) {
        return null;
    }

    @Override
    public void delete(String imageUrl) {
        // no-op
    }
}
