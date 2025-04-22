package com.hm.oldiesbutgoodies.service;

import com.hm.oldiesbutgoodies.domain.OwnerType;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    String store(MultipartFile file);

    String store(MultipartFile file, OwnerType ownerType);
}
