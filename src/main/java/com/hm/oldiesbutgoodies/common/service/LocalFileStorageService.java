package com.hm.oldiesbutgoodies.common.service;

import com.hm.oldiesbutgoodies.common.domain.OwnerType;
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
    private String baseDir;

    @Override
    public String store(MultipartFile file) {
        return store(file, null);
    }

    @Override
    public String store(MultipartFile file, OwnerType ownerType) {
        Path dir = (ownerType == null)
                ? Paths.get(baseDir)
                : Paths.get(baseDir, ownerType.name().toLowerCase());
        try {
            Files.createDirectories(dir);
        } catch (IOException e) {
            throw new RuntimeException("디렉토리 생성 실패", e);
        }

        String filename = UUID.randomUUID() + "-" + file.getOriginalFilename();
        Path target = dir.resolve(filename);

        try (InputStream in = file.getInputStream()) {
            Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 실패", e);
        }

        String prefix = (ownerType == null) ? "" : ownerType.name().toLowerCase() + "/";
        return "/uploads/" + prefix + filename;
    }


}
