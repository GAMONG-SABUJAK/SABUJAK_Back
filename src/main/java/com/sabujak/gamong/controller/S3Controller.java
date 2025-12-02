package com.sabujak.gamong.controller;

import com.sabujak.gamong.dto.Request.ReqPresignedUrl;
import com.sabujak.gamong.dto.Response.PresignedUrlRes;
import com.sabujak.gamong.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class S3Controller {
    private final S3Service s3Service;

    @PostMapping("/s3/presigned-urls")
    public ResponseEntity<List<PresignedUrlRes>> getPresignedUrls(@RequestBody List<ReqPresignedUrl> requests) {
        List<PresignedUrlRes> presignedUrlResList = requests.stream()
                .map(s3Service::issuePresignedAndCdnUrl)
                .toList();
        return ResponseEntity.status(HttpStatus.OK).body(presignedUrlResList);
    }

    @DeleteMapping("/s3")
    public ResponseEntity<String> deleteFile(@RequestBody List<String> keys) {
        s3Service.deleteFiles(keys);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("삭제 성공");
    }

}

