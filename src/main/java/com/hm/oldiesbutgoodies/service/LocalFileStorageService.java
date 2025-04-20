package com.hm.oldiesbutgoodies.service;

import com.hm.oldiesbutgoodies.domain.ContentType;
import com.hm.oldiesbutgoodies.exception.CustomException;
import com.hm.oldiesbutgoodies.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class LocalFileStorageService implements FileStorageService {

    @Value("${app.upload.dir}")
    private String uploadDir;

    @Override
    public String store(MultipartFile file) throws IOException {
        return store(file, ContentType.valueOf(""));
    }

    @Override
    public String store(MultipartFile file, ContentType contentType) throws IOException {
        String filename = UUID.randomUUID() + "-" + file.getOriginalFilename();
        String ownerType = contentType.toString();

        Path dir = ownerType.isBlank()
                ? Paths.get(uploadDir)
                : Paths.get(uploadDir, ownerType.toLowerCase());
        Files.createDirectories(dir);
        Path target = dir.resolve(filename);
        try (InputStream in = file.getInputStream()) {
            Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
        }
        return "/uploads/" + (ownerType.isBlank() ? "" : ownerType.toLowerCase() + "/") + filename;
    }

}
