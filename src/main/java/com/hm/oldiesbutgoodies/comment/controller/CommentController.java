package com.hm.oldiesbutgoodies.comment.controller;

import com.hm.oldiesbutgoodies.security.JwtProvider;
import com.hm.oldiesbutgoodies.common.domain.OwnerType;
import com.hm.oldiesbutgoodies.comment.dto.request.CommentDto;
import com.hm.oldiesbutgoodies.common.dto.ResponseDto;
import com.hm.oldiesbutgoodies.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/{resource}/{resourceId}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final JwtProvider jwtProvider;


    @PostMapping("")
    public ResponseEntity<ResponseDto> createComment(@RequestHeader("Authorization") String accessToken,
                                                     @PathVariable("resource") OwnerType ownerType, @PathVariable Long resourceId,
                                                     @RequestBody CommentDto commentDto) {

        String email = jwtProvider.getUserEmail(accessToken);

        return ResponseEntity.ok(commentService.createComment(email, ownerType, resourceId, commentDto));
    }

    @GetMapping("")
//    public ResponseEntity<List<CommentResponse>> listComments(
//            @PathVariable Long postId, @PathVariable String resourceId
//    ) {
//        List<CommentResponse> list = commentService.listComments(postId);
//        return ResponseEntity.ok(list);
//    }


    @PutMapping("/{commentId}")
    public ResponseEntity<ResponseDto> updateComment(@RequestHeader("Authorization") String accessToken,
                                                     @PathVariable("resource") OwnerType ownerType,
                                                     @PathVariable("resourceId") Long resourceId,
                                                     @PathVariable("commentId") Long commentId) {

        return ResponseEntity.ok(new ResponseDto());
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<ResponseDto> deleteComment(@RequestHeader("Authorization") String accessToken,
                                                     @PathVariable("resource") OwnerType ownerType,
                                                     @PathVariable("resourceId") Long resourceId,
                                                     @PathVariable("commentId") Long commentId) {
        String email = jwtProvider.getUserEmail(accessToken);

        return ResponseEntity.ok(commentService.deleteComment(email, ownerType, resourceId, commentId));
    }


}
