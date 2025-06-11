package com.hm.oldiesbutgoodies.product.controller;

import com.hm.oldiesbutgoodies.common.dto.ResponseDto;
import com.hm.oldiesbutgoodies.post.dto.request.PostDto;
import com.hm.oldiesbutgoodies.post.dto.request.SearchRequest;
import com.hm.oldiesbutgoodies.post.dto.response.PostDetailDto;
import com.hm.oldiesbutgoodies.post.dto.response.PostSummaryDto;
import com.hm.oldiesbutgoodies.product.dto.request.ProductRegisterDto;
import com.hm.oldiesbutgoodies.product.service.ProductService;
import com.hm.oldiesbutgoodies.security.JwtProvider;
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
@RequestMapping("/v1/products")
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final ProductService productService;
    private final ProductInteractionService productInteractionService;
    private final JwtProvider jwtProvider;

    @PostMapping(value = "/",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDto> createProduct(@RequestHeader("Authorization") String accessToken,
                                                     @RequestPart("product") @Valid ProductRegisterDto dto,
                                                     @RequestParam(value = "images", required = false) List<MultipartFile> images) {
        String email = jwtProvider.getUserEmail(accessToken);

        return ResponseEntity.ok(productService.createProduct(email, dto, images));
    }


    //수정
    @PutMapping(value = "/{productId}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDto> updateProduct(@RequestHeader("Authorization") String accessToken,
                                                     @PathVariable Long productId,
                                                     @RequestPart("post") @Valid PostDto dto,
                                                     @RequestParam(value = "images", required = false) List<MultipartFile> images) {
        String email = jwtProvider.getUserEmail(accessToken);

        return ResponseEntity.ok(productService.updateProduct(email, productId, dto, images));
    }


    // 삭제(soft-deleted)
    @DeleteMapping(value = "/{productId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDto> deletePost(@RequestHeader("Authorization") String accessToken,
                                                  @PathVariable Long productId) {
        String email = jwtProvider.getUserEmail(accessToken);

        return ResponseEntity.ok(productService.deleteProduct(email, productId));
    }

    // 단건 조회(상세)
    @GetMapping(value = "/{productId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PostDetailDto> getProductById(@RequestHeader("Authorization") String accessToken,
                                                        @PathVariable Long productId) {
        String email = jwtProvider.getUserEmail(accessToken);
        PostDetailDto dto = productService.getProductById(email, productId);
        productService.incrementViewCount(productId);

        return ResponseEntity.ok(dto);
    }


    // 전체 조회
    @GetMapping(value = "",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<PostSummaryDto>> listProducts(@ModelAttribute SearchRequest searchRequest,
                                                             Pageable pageable) {
        Page<PostSummaryDto> page = productService.listProducts(searchRequest, pageable);

        return ResponseEntity.ok(page);
    }

    @PostMapping(value = "/{productId}/like",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDto> toggleLike(@RequestHeader("Authorization") String accessToken,
                                                  @PathVariable Long productId) {

        String email = jwtProvider.getUserEmail(accessToken);

        return ResponseEntity.ok(productInteractionService.toggleLike(email, productId));

    }

    @PostMapping(value = "/{productId}/bookmark",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDto> toggleBookmark(@RequestHeader("Authorization") String accessToken,
                                                      @PathVariable Long productId) {

        String email = jwtProvider.getUserEmail(accessToken);

        return ResponseEntity.ok(productInteractionService.toggleBookmark(email, productId));

    }

}
