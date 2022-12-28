package com.pavelshapel.aws.spring.boot.starter.api.service;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;

import java.io.File;
import java.io.InputStream;
import java.util.List;

public interface BucketHandler {
    List<String> getBucketNames();

    boolean isBucketExists(String bucketName);

    String createBucketIfNotExists(String bucketName);

    String deleteBucketIfExists(String bucketName);

    String clearBucket(String bucketName);

    boolean isObjectExist(String bucketName, String key);

    String uploadObject(String bucketName, String key, String payload);

    String uploadObject(String bucketName, String key, File payload);

    String uploadObject(String bucketName, String key, InputStream inputStream, ObjectMetadata metadata);

    String uploadObject(PutObjectRequest request);

    S3Object downloadObject(String bucketName, String key);

    List<String> listAll(String bucketName);
}
