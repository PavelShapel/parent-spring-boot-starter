package com.pavelshapel.aws.spring.boot.starter.util.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.pavelshapel.aop.spring.boot.starter.log.method.Loggable;
import com.pavelshapel.aws.spring.boot.starter.properties.AwsProperties;
import com.pavelshapel.aws.spring.boot.starter.properties.nested.S3NestedProperties;
import com.pavelshapel.aws.spring.boot.starter.util.BucketHandler;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Loggable
public class S3BucketHandler implements BucketHandler {
    public static final String BUCKET_EXIST_PATTERN = "bucket [%s] already exists";
    public static final String BUCKET_DOES_NOT_EXIST_PATTERN = "bucket [%s] does not exist";

    @Autowired
    private AmazonS3 amazonS3;
    @Autowired
    private AwsProperties awsProperties;

    @PostConstruct
    private void postConstruct() {
        Optional.ofNullable(awsProperties)
                .map(AwsProperties::getS3)
                .map(S3NestedProperties::getBucketName)
                .ifPresent(this::createBucketIfNotExists);
    }

    @Override
    public List<String> getBucketNames() {
        return Optional.of(amazonS3)
                .map(AmazonS3::listBuckets)
                .orElseGet(Collections::emptyList)
                .stream()
                .map(Bucket::getName)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isBucketExists(String bucketName) {
        return amazonS3.doesBucketExistV2(bucketName);
    }

    @Override
    public String createBucketIfNotExists(String bucketName) {
        return Optional.of(isBucketExists(bucketName))
                .filter(Boolean.FALSE::equals)
                .map(unused -> createBucket(bucketName))
                .orElse(String.format(BUCKET_EXIST_PATTERN, bucketName));
    }

    @Override
    public String createBucket(String bucketName) {
        return Optional.of(amazonS3.createBucket(bucketName))
                .map(Bucket::getName)
                .orElse(null);
    }

    @Override
    public String deleteBucketIfExists(String bucketName) {
        return Optional.of(isBucketExists(bucketName))
                .filter(Boolean.TRUE::equals)
                .map(unused -> deleteBucket(bucketName))
                .orElse(String.format(BUCKET_DOES_NOT_EXIST_PATTERN, bucketName));
    }

    @Override
    public String deleteBucket(String bucketName) {
        deleteAll(bucketName);
        amazonS3.deleteBucket(bucketName);
        return bucketName;
    }

    @Override
    public void upload(String bucketName, String key, String payload) {
        amazonS3.putObject(bucketName, key, payload);
    }

    @Override
    public void upload(String bucketName, String key, File payload) {
        amazonS3.putObject(bucketName, key, payload);
    }

    @Override
    public InputStream download(String bucketName, String key) {
        return Optional.of(amazonS3.getObject(bucketName, key))
                .map(S3Object::getObjectContent)
                .orElseThrow(() -> new IllegalArgumentException(String.format("bucketName [%s], key [%s]", bucketName, key)));
    }

    @Override
    public List<String> listAll(String bucketName) {
        return amazonS3.listObjects(bucketName)
                .getObjectSummaries().stream()
                .map(S3ObjectSummary::getKey)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteAll(String bucketName) {
        deleteAllObjects(bucketName);
        deleteAllObjectVersions(bucketName);
    }

    private void deleteAllObjects(String bucketName) {
        ObjectListing objectListing = amazonS3.listObjects(bucketName);
        while (true) {
            objectListing.getObjectSummaries()
                    .forEach(s3ObjectSummary -> amazonS3.deleteObject(bucketName, s3ObjectSummary.getKey()));
            if (objectListing.isTruncated()) {
                objectListing = amazonS3.listNextBatchOfObjects(objectListing);
            } else {
                break;
            }
        }
    }

    private void deleteAllObjectVersions(String bucketName) {
        VersionListing versionList = amazonS3.listVersions(new ListVersionsRequest().withBucketName(bucketName));
        while (true) {
            versionList.getVersionSummaries()
                    .forEach(s3VersionSummary -> amazonS3.deleteVersion(bucketName, s3VersionSummary.getKey(), s3VersionSummary.getVersionId()));
            if (versionList.isTruncated()) {
                versionList = amazonS3.listNextBatchOfVersions(versionList);
            } else {
                break;
            }
        }
    }
}
