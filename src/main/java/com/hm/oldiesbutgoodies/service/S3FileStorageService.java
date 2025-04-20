package com.hm.oldiesbutgoodies.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

//public class S3FileStorageService implements FileStorageService {

//    private final S3Client s3;
//    @Value("${aws.s3.bucket}")
//    private String bucket;
//
//    @Override
//    public String store(MultipartFile file) {
//        String key = "posts/" + UUID.randomUUID() + "-" + file.getOriginalFilename();
//        try (InputStream in = file.getInputStream()) {
//            s3.putObject(PutObjectRequest.builder()
//                            .bucket(bucket)
//                            .key(key)
//                            .acl(ObjectCannedACL.PUBLIC_READ)
//                            .build(),
//                    RequestBody.fromInputStream(in, file.getSize()));
//        } catch (IOException e) {
//            throw new FileStorageException("S3 업로드 실패", e);
//        }
//        return s3.utilities()
//                .getUrl(b -> b.bucket(bucket).key(key))
//                .toExternalForm();
//    }
//}
