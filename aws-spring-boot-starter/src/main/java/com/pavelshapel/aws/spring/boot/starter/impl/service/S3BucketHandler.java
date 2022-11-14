package com.pavelshapel.aws.spring.boot.starter.impl.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.ListVersionsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.s3.model.VersionListing;
import com.pavelshapel.aop.spring.boot.starter.annotation.ExceptionWrapped;
import com.pavelshapel.aws.spring.boot.starter.api.service.BucketHandler;
import com.pavelshapel.aws.spring.boot.starter.properties.AwsProperties;
import com.pavelshapel.aws.spring.boot.starter.properties.nested.AbstractServiceProperties;
import com.pavelshapel.aws.spring.boot.starter.properties.nested.S3ServiceProperties;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@FieldDefaults(level = AccessLevel.PRIVATE)
@ExceptionWrapped
public class S3BucketHandler implements BucketHandler {
    @Autowired
    AmazonS3 amazonS3;
    @Autowired
    AwsProperties awsProperties;

    @PostConstruct
    private void postConstruct() {
        Optional.ofNullable(awsProperties)
                .map(AwsProperties::getS3)
                .filter(AbstractServiceProperties::getEnabled)
                .map(S3ServiceProperties::getObject)
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
    @ExceptionWrapped(prefix = "bucket name should be specified.")
    public boolean isBucketExists(String bucketName) {
        return amazonS3.doesBucketExistV2(bucketName);
    }

    @Override
    public String createBucketIfNotExists(String bucketName) {
        return Optional.of(isBucketExists(bucketName))
                .filter(Boolean.FALSE::equals)
                .map(unused -> createBucket(bucketName))
                .orElse(bucketName);
    }

    private String createBucket(String bucketName) {
        return Optional.ofNullable(bucketName)
                .map(amazonS3::createBucket)
                .map(Bucket::getName)
                .orElseThrow();
    }

    @Override
    public String deleteBucketIfExists(String bucketName) {
        return Optional.of(isBucketExists(bucketName))
                .filter(Boolean.TRUE::equals)
                .map(unused -> deleteBucket(bucketName))
                .orElse(bucketName);
    }

    private String deleteBucket(String bucketName) {
        Optional.ofNullable(bucketName)
                .map(this::clearBucket)
                .ifPresent(amazonS3::deleteBucket);
        return bucketName;
    }

    @Override
    public String clearBucket(String bucketName) {
        return Optional.of(isBucketExists(bucketName))
                .filter(Boolean.TRUE::equals)
                .map(unused -> bucketName)
                .map(this::deleteAllObjectVersions)
                .map(this::deleteAllObjects)
                .orElse(bucketName);
    }

    @Override
    @ExceptionWrapped(prefix = "bucket name and key should be specified.")
    public boolean isObjectExist(String bucketName, String key) {
        return amazonS3.doesObjectExist(bucketName, key);
    }

    @Override
    public String uploadObject(String bucketName, String key, String payload) {
        amazonS3.putObject(bucketName, key, payload);
        return buildObjectPath(bucketName, key);
    }

    @Override
    public String uploadObject(String bucketName, String key, File payload) {
        amazonS3.putObject(bucketName, key, payload);
        return buildObjectPath(bucketName, key);
    }

    @Override
    public String uploadObject(String bucketName, String key, InputStream inputStream, ObjectMetadata metadata) {
        PutObjectRequest request = new PutObjectRequest(bucketName, key, inputStream, metadata);
        return uploadObject(request);
    }

    @Override
    public String uploadObject(PutObjectRequest request) {
        amazonS3.putObject(request);
        return buildObjectPath(request.getBucketName(), request.getKey());
    }

    @Override
    public InputStream downloadObject(String bucketName, String key) {
        return Optional.of(amazonS3.getObject(bucketName, key))
                .map(S3Object::getObjectContent)
                .orElseThrow();
    }

    private String buildObjectPath(String bucketName, String key) {
        return String.format("s3://%s/%s", bucketName, key);
    }

    @Override
    public List<String> listAll(String bucketName) {
        return amazonS3.listObjects(bucketName)
                .getObjectSummaries().stream()
                .map(S3ObjectSummary::getKey)
                .collect(Collectors.toList());
    }

    private String deleteAllObjects(String bucketName) {
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
        return bucketName;
    }

    private String deleteAllObjectVersions(String bucketName) {
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
        return bucketName;
    }
}
