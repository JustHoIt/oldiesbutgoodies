package com.hm.oldiesbutgoodies.product.controller;

import com.hm.oldiesbutgoodies.security.JwtProvider;
import com.hm.oldiesbutgoodies.common.dto.ResponseDto;
import com.hm.oldiesbutgoodies.product.dto.request.ProductRegisterDto;
import com.hm.oldiesbutgoodies.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private JwtProvider jwtProvider;


    @PostMapping(value = "/",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDto> createPost(@RequestHeader("Authorization") String accessToken,
                                                  @RequestPart("product") @Valid ProductRegisterDto dto,
                                                  @RequestParam(value = "images", required = false) List<MultipartFile> images) {
        String email = jwtProvider.getUserEmail(accessToken);

        return ResponseEntity.ok(productService.createProduct(email, dto, images));
    }
}
