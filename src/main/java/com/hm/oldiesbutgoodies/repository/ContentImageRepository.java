package com.hm.oldiesbutgoodies.repository;

import com.hm.oldiesbutgoodies.domain.ContentImage;
import com.hm.oldiesbutgoodies.domain.ContentType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContentImageRepository extends JpaRepository<ContentImage, Long> {
    List<ContentImage> findAllByContentTypeAndOwnerIdOrderByPositionAsc(ContentType contentType, Long postId);
}
