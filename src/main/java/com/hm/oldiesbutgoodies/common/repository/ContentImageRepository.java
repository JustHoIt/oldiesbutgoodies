package com.hm.oldiesbutgoodies.common.repository;

import com.hm.oldiesbutgoodies.common.domain.ContentImage;
import com.hm.oldiesbutgoodies.common.domain.OwnerType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContentImageRepository extends JpaRepository<ContentImage, Long> {
    List<ContentImage> findAllByOwnerTypeAndOwnerIdOrderByPositionAsc(OwnerType ownerType, Long postId);

    void deleteAllByOwnerTypeAndOwnerId(OwnerType ownerType, Long postId);
}
