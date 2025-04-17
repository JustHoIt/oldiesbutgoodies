package com.hm.oldiesbutgoodies.controller;

import com.hm.oldiesbutgoodies.auth.JwtProvider;
import com.hm.oldiesbutgoodies.dto.request.PostDto;
import com.hm.oldiesbutgoodies.dto.response.ResponseDto;
import com.hm.oldiesbutgoodies.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Pageable;

@RestController("/v1/posts")
@RequiredArgsConstructor
@Slf4j
public class PostController {

    private final PostService postService;
    private final JwtProvider jwtProvider;

    @PostMapping(value = "/",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDto> createPost(@RequestHeader("Authorization") String accessToken,
                                                 @Valid @RequestBody PostDto dto) {
        String email = jwtProvider.getUserEmail(accessToken);

        return ResponseEntity.ok(postService.createPost(email, dto));
    }

    //수정
    @PutMapping(value = "/{postId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDto> updatePost(@RequestHeader("Authorization") String accessToken,,
                                                  @PathVariable Long postId,
                                                  @Valid @RequestBody PostDto dto) {
        String email = jwtProvider.getUserEmail(accessToken);

        return ResponseEntity.ok(postService.updatePost(email, postId, dto));
    }


    // 삭제(soft-deleted)
    @DeleteMapping(value = "/{postId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDto> deletePost(@RequestHeader("Authorization") String accessToken,
                                                  @PathVariable Long postId) {
        String email = jwtProvider.getUserEmail(accessToken);

        return ResponseEntity.ok(postService.deletePost(email, postId));
    }

    // 조회
    @GetMapping(value = "/{postId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getPostById(@RequestHeader("Authorization") String accessToken,
                                      @PathVariable Long postId) {
        String email = jwtProvider.getUserEmail(accessToken);

        return ResponseEntity.ok(postService.getPostById(email, postId));
    }


    // 전체 조회
    @GetMapping(value = "",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Pageable<>> listPosts(@RequestParam String type) {

        return ResponseEntity.ok(postService.);
    }


}
