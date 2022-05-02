package com.pavelshapel.core.spring.boot.starter.api.model;

public interface S3Transferred {
    String SOURCE_BUCKET_FIELD = "sourceBucket";
    String SOURCE_FILE_FIELD = "sourceFile";
    String TARGET_BUCKET_FIELD = "targetBucket";
    String TARGET_FILE_FIELD = "targetFile";

    String getSourceBucket();

    void setSourceBucket(String sourceBucket);

    String getSourceFile();

    void setSourceFile(String sourceFile);

    String getTargetBucket();

    void setTargetBucket(String targetBucket);

    String getTargetFile();

    void setTargetFile(String targetFile);
}