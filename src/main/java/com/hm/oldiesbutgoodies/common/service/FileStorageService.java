package com.hm.oldiesbutgoodies.common.service;

import com.hm.oldiesbutgoodies.common.domain.OwnerType;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    String store(MultipartFile file);

    String store(MultipartFile file, OwnerType ownerType);
}
