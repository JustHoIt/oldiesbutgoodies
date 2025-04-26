package com.hm.oldiesbutgoodies.product.service;

import com.hm.oldiesbutgoodies.common.dto.ResponseDto;
import com.hm.oldiesbutgoodies.common.exception.CustomException;
import com.hm.oldiesbutgoodies.common.exception.ErrorCode;
import com.hm.oldiesbutgoodies.common.service.FileStorageService;
import com.hm.oldiesbutgoodies.common.domain.ContentImage;
import com.hm.oldiesbutgoodies.common.domain.OwnerType;
import com.hm.oldiesbutgoodies.product.domain.Product;
import com.hm.oldiesbutgoodies.product.dto.request.ProductRegisterDto;
import com.hm.oldiesbutgoodies.common.repository.ContentImageRepository;
import com.hm.oldiesbutgoodies.product.repository.ProductRepository;
import com.hm.oldiesbutgoodies.user.domain.User;
import com.hm.oldiesbutgoodies.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ContentImageRepository contentImageRepository;
    private final FileStorageService storage;

    /* 상품 등록 */
    @Transactional
    public ResponseDto createProduct(String email, ProductRegisterDto dto, List<MultipartFile> images) {
        User user = findUserByEmail(email);
        Product product = Product.from(dto);
        product.setSeller(user);

        productRepository.save(product);

        attachImages(product.getId(), images);

        //임시
        List<String> urls = contentImageRepository
                .findAllByOwnerTypeAndOwnerIdOrderByPositionAsc(OwnerType.PRODUCT, product.getId())
                .stream().map(ContentImage::getUrl).toList();

        log.info("{}", urls);
        log.info("{} 님이 postId : {}, {} 라는 제목으로 상품을 작성했습니다.", user.getName(), product.getId(), dto.getTitle());

        return ResponseDto.setMessage("글이 성공적으로 작성됐습니다.");
    }


    private User findUserByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);

        if (user.isPresent()) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        if (!user.get().getIsSeller()) {
            throw new CustomException(ErrorCode.USER_NOT_AUTHOR);
        }

        return user.get();
    }


    /* 이미지 업로드 공통 로직 */
    private void attachImages(Long postId, List<MultipartFile> images) {
        if (images == null || images.isEmpty()) {
            return;
        }

        // 1) 기존 이미지 모두 삭제 (수정 시)
        contentImageRepository.deleteAllByOwnerTypeAndOwnerId(OwnerType.POST, postId);

        // 2) 새 이미지 저장
        int pos = 0;
        for (MultipartFile file : images) {
            String url = storage.store(file, OwnerType.valueOf(OwnerType.POST.name()));
            ContentImage ci = ContentImage.builder()
                    .ownerType(OwnerType.POST)
                    .ownerId(postId)
                    .url(url)
                    .position(pos++)
                    .build();
            contentImageRepository.save(ci);
        }
    }


}
