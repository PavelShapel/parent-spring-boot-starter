package com.pavelshapel.aws.spring.boot.starter.util;

import java.io.InputStream;
import java.util.List;

public interface BucketHandler {
    List<String> getBucketNames();

    boolean isBucketExists(String bucketName);

    String createBucketIfNotExists(String bucketName);

    String createBucket(String bucketName);

    String deleteBucketIfExists(String bucketName);

    String deleteBucket(String bucketName);

    void save(String bucketName, String key, String payload);

    InputStream findByKey(String bucketName, String key);

    List<String> listAll(String bucketName);

    void deleteAll(String bucketName);
}
