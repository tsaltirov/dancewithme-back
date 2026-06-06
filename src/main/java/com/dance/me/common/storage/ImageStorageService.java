package com.dance.me.common.storage;

import org.springframework.web.multipart.MultipartFile;

public interface ImageStorageService {

    // Sube la imagen y devuelve la URL pública. Devuelve null si no hay proveedor configurado.
    String upload(MultipartFile file, String folder);

    void delete(String imageUrl);
}
