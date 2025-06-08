package com.hm.oldiesbutgoodies.post.repository;

import com.hm.oldiesbutgoodies.post.domain.Post;
import com.hm.oldiesbutgoodies.post.dto.request.SearchRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepositoryCustom {
    Page<Post> searchByCondition(SearchRequest request, Pageable pageable);
}
