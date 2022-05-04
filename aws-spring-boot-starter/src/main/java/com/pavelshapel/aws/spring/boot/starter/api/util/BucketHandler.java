package com.pavelshapel.aws.spring.boot.starter.api.util;

import java.io.File;
import java.io.InputStream;
import java.util.List;

public interface BucketHandler {
    List<String> getBucketNames();

    boolean isBucketExists(String bucketName);

    String createBucketIfNotExists(String bucketName);

    String createBucket(String bucketName);

    String deleteBucketIfExists(String bucketName);

    String clearBucket(String bucketName);

    String deleteBucket(String bucketName);

    boolean isObjectExist(String bucketName, String key);

    String uploadObject(String bucketName, String key, String payload);

    String uploadObject(String bucketName, String key, File payload);

    InputStream downloadObject(String bucketName, String key);

    List<String> listAll(String bucketName);
}
