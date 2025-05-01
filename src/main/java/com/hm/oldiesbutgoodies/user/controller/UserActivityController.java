package com.hm.oldiesbutgoodies.user.controller;

import com.hm.oldiesbutgoodies.comment.dto.response.CommentSimpleDto;
import com.hm.oldiesbutgoodies.post.dto.response.PostSimpleDto;
import com.hm.oldiesbutgoodies.post.dto.response.PostSummaryDto;
import com.hm.oldiesbutgoodies.security.JwtProvider;
import com.hm.oldiesbutgoodies.user.service.UserActivityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/users/activity")
@RequiredArgsConstructor
@Slf4j
public class UserActivityController {

    private final UserActivityService userActivityService;
    private final JwtProvider jwtProvider;


    @GetMapping(value = "/posts",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<PostSimpleDto>> getMyPosts(@RequestHeader("Authorization") String accessToken,
                                                          Pageable pageable) {
        String email = jwtProvider.getUserEmail(accessToken);

        return ResponseEntity.ok(userActivityService.getMyPosts(email, pageable));
    }

    @GetMapping(value = "/comments",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<CommentSimpleDto>> getMyComments(@RequestHeader("Authorization") String accessToken,
                                                                Pageable pageable) {
        String email = jwtProvider.getUserEmail(accessToken);

        return ResponseEntity.ok(userActivityService.getMyComments(email, pageable));
    }


    @GetMapping(value = "/likes",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<PostSimpleDto>> getMyPostLikes(@RequestHeader("Authorization") String accessToken,
                             Pageable pageable) {
        String email = jwtProvider.getUserEmail(accessToken);

        return ResponseEntity.ok(userActivityService.getMyPostLikes(email, pageable));
    }

    @GetMapping(value = "/bookmarks",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<PostSimpleDto>> getMyPostBookmarks(@RequestHeader("Authorization") String accessToken,
                                             Pageable pageable) {
        String email = jwtProvider.getUserEmail(accessToken);

        return ResponseEntity.ok(userActivityService.getMyPostBookmarks(email, pageable));
    }

    //✅TODO: 추 후 장터 기능 추가

//    @GetMapping(value = "/bookmarks",
//            produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<Page<>> getMyProductBookmarks(@RequestHeader("Authorization") String accessToken,
//                                                 Pageable pageable) {
//        String email = jwtProvider.getUserEmail(accessToken);
//
//        return ResponseEntity.ok(userActivityService.getMyBookmarks(email, pageable));
//    }

//    @GetMapping(value = "/posts",
//            produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<Page<>> getMyProducts(@RequestHeader("Authorization") String accessToken,
//                                Pageable pageable) {
//        String email = jwtProvider.getUserEmail(accessToken);
//
//        return ResponseEntity.ok(userActivityService.getMyProducts(email, pageable));
//    }

//    @GetMapping(value = "/posts",
//            produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<> getMyOrders() {
//        String email = jwtProvider.getUserEmail(accessToken);
//
//        return ResponseEntity.ok(userActivityService.getMyOrders(email));
//    }

}
