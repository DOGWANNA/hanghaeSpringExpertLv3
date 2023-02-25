package com.sparta.hanghaespringexpertlv3.controller;

import com.sparta.hanghaespringexpertlv3.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class S3Controller {
    private final S3Service s3Service;
    @PostMapping("upload")
    public String execWrite(@RequestPart(value = "file", required = false) MultipartFile file) throws IOException {

        String imgPath = s3Service.upload(file);

        return imgPath;
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<byte[]> download(@PathVariable String fileName) throws IOException {
        return s3Service.getObject(fileName);
    }

//    @DeleteMapping("/resource")
//    public void remove(AwsS3 awsS3) {
//        s3Service.remove(awsS3);
//    }
}
