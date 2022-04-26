package com.pavelshapel.aws.spring.boot.starter.util;

import java.io.File;
import java.io.InputStream;
import java.util.List;

public interface BucketHandler {
    List<String> getBucketNames();

    boolean isBucketExists(String bucketName);

    String createBucketIfNotExists(String bucketName);

    String createBucket(String bucketName);

    String deleteBucketIfExists(String bucketName);

    String deleteBucket(String bucketName);

    void upload(String bucketName, String key, String payload);

    void upload(String bucketName, String key, File payload);

    InputStream download(String bucketName, String key);

    List<String> listAll(String bucketName);

    void deleteAll(String bucketName);
}
