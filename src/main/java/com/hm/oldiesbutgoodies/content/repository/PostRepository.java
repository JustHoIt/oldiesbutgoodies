package com.hm.oldiesbutgoodies.content.repository;

import com.hm.oldiesbutgoodies.content.domain.Category;
import com.hm.oldiesbutgoodies.content.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    Optional<Post> findByIdAndDeletedFalse(Long id);

    Page<Post> findAllByDeletedFalse(Pageable pageable);

    Page<Post> findAllByCategoryAndDeletedFalse(Category category, Pageable pageable);

}
