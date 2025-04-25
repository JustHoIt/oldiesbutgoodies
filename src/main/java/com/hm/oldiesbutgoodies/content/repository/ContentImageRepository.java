package com.hm.oldiesbutgoodies.content.repository;

import com.hm.oldiesbutgoodies.content.domain.ContentImage;
import com.hm.oldiesbutgoodies.content.domain.OwnerType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContentImageRepository extends JpaRepository<ContentImage, Long> {
    List<ContentImage> findAllByOwnerTypeAndOwnerIdOrderByPositionAsc(OwnerType ownerType, Long postId);

    void deleteAllByOwnerTypeAndOwnerId(OwnerType ownerType, Long postId);
}
