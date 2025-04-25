package com.hm.oldiesbutgoodies.content.controller;

import com.hm.oldiesbutgoodies.auth.JwtProvider;
import com.hm.oldiesbutgoodies.content.dto.request.PostSummaryDto;
import com.hm.oldiesbutgoodies.content.dto.request.PostDto;
import com.hm.oldiesbutgoodies.common.dto.ResponseDto;
import com.hm.oldiesbutgoodies.content.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/v1/posts")
@RequiredArgsConstructor
@Slf4j
public class PostController {

    private final PostService postService;
    private final JwtProvider jwtProvider;

    @PostMapping(value = "/",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDto> createPost(@RequestHeader("Authorization") String accessToken,
                                                  @RequestPart("post") @Valid PostDto dto,
                                                  @RequestParam(value = "images", required = false)List<MultipartFile> images) {
        String email = jwtProvider.getUserEmail(accessToken);

        return ResponseEntity.ok(postService.createPost(email, dto, images));
    }

    //수정
    @PutMapping(value = "/{postId}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDto> updatePost(@RequestHeader("Authorization") String accessToken,
                                                  @PathVariable Long postId,
                                                  @RequestPart("post") @Valid PostDto dto,
                                                  @RequestParam(value = "images", required = false)List<MultipartFile> images) {
        String email = jwtProvider.getUserEmail(accessToken);

        return ResponseEntity.ok(postService.updatePost(email, postId, dto, images));
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
    public ResponseEntity<PostDto> getPostById(@RequestHeader("Authorization") String accessToken,
                                               @PathVariable Long postId) {
        String email = jwtProvider.getUserEmail(accessToken);

        return ResponseEntity.ok(postService.getPostById(email, postId));
    }


    // 전체 조회
    @GetMapping(value = "",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<PostSummaryDto>> listPosts(@RequestParam(required = false) String category,
                                                          @RequestParam(required = false) String keyword,
                                                          Pageable pageable) {


        Page<PostSummaryDto> page = postService.listPosts(category, keyword, pageable);

        return ResponseEntity.ok(page);
    }


}
