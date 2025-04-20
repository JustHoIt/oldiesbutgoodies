package com.hm.oldiesbutgoodies.service;

import com.hm.oldiesbutgoodies.domain.ContentType;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileStorageService {
    String store(MultipartFile file) throws IOException;

    String store(MultipartFile file, ContentType contentType) throws IOException;
}
