package com.oceans7.dib.global.util;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.oceans7.dib.global.exception.ApplicationException;
import com.oceans7.dib.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Component
@RequiredArgsConstructor
public class AmazonS3ResourceStorage {
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private final AmazonS3Client amazonS3Client;

    public void store(String fullPath, MultipartFile multipartFile) {
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(MultipartUtil.getContentType(multipartFile));

            amazonS3Client.putObject(new PutObjectRequest(bucket, fullPath, multipartFile.getInputStream(), metadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApplicationException(ErrorCode.FILE_UPLOAD_FAIL);
        }
    }
}
