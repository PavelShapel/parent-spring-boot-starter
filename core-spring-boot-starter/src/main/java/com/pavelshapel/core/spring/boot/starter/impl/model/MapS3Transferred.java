package com.pavelshapel.core.spring.boot.starter.impl.model;

import com.pavelshapel.core.spring.boot.starter.api.model.S3Transferred;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MapS3Transferred extends HashMap<String, String> implements S3Transferred {
    public MapS3Transferred() {
    }

    public MapS3Transferred(Map<? extends String, ? extends String> map) {
        super(map);
    }

    @Override
    public String getSourceBucket() {
        return get(SOURCE_BUCKET_FIELD);
    }

    @Override
    public void setSourceBucket(String sourceBucket) {
        put(SOURCE_BUCKET_FIELD, sourceBucket);
    }

    @Override
    public String getSourceFile() {
        return get(SOURCE_FILE_FIELD);
    }

    @Override
    public void setSourceFile(String sourceFile) {
        put(SOURCE_FILE_FIELD, sourceFile);
    }

    @Override
    public String getTargetBucket() {
        return get(TARGET_BUCKET_FIELD);
    }

    @Override
    public void setTargetBucket(String targetBucket) {
        put(TARGET_BUCKET_FIELD, targetBucket);
    }

    @Override
    public String getTargetFile() {
        return get(TARGET_FILE_FIELD);
    }

    @Override
    public void setTargetFile(String targetFile) {
        put(TARGET_FILE_FIELD, targetFile);
    }
}
