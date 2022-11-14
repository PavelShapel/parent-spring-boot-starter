package com.pavelshapel.aws.spring.boot.starter.impl.service;

import com.amazonaws.services.s3.AmazonS3;
import com.pavelshapel.aws.spring.boot.starter.AbstractTest;
import com.pavelshapel.aws.spring.boot.starter.api.service.BucketHandler;
import com.pavelshapel.aws.spring.boot.starter.provider.OneStringProvider;
import com.pavelshapel.aws.spring.boot.starter.provider.ThreeStringProvider;
import com.pavelshapel.aws.spring.boot.starter.provider.TwoStringProvider;
import com.pavelshapel.test.spring.boot.starter.container.S3AwsExtension;
import lombok.AccessLevel;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.NullSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.singleton;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(S3AwsExtension.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
class S3BucketHandlerTest extends AbstractTest {
    private static final String SOURCE_TXT = "source.txt";
    @SpyBean
    AmazonS3 amazonS3;
    @Autowired
    BucketHandler bucketHandler;
    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        bucketHandler.getBucketNames().forEach(bucketHandler::deleteBucketIfExists);
    }

    @ParameterizedTest
    @ArgumentsSource(OneStringProvider.class)
    void getBucketNames_WithExistingBucket_ShouldReturnBuketNames(String bucketName) {
        bucketName = createBucket(bucketName);

        List<String> result = bucketHandler.getBucketNames();

        assertThat(result)
                .asList()
                .hasSize(1)
                .first()
                .asString()
                .isEqualTo(bucketName);
    }

    @ParameterizedTest
    @ArgumentsSource(OneStringProvider.class)
    void isBucketExists_WithNotExistBucketAsParameter_ShouldReturnFalse(String bucketName) {
        bucketName = createValidBucketName(bucketName);

        boolean result = bucketHandler.isBucketExists(bucketName);

        assertThat(result).isFalse();
    }

    @ParameterizedTest
    @ArgumentsSource(OneStringProvider.class)
    void isBucketExists_WithExistBucketAsParameter_ShouldReturnTrue(String bucketName) {
        bucketName = createBucket(bucketName);

        boolean result = bucketHandler.isBucketExists(bucketName);

        assertThat(result).isTrue();
    }

    @ParameterizedTest
    @NullSource
    void isBucketExists_WithNulAsParameter_ShouldThrowException(String bucketName) {
        assertThatThrownBy(() -> bucketHandler.isBucketExists(bucketName))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @ArgumentsSource(OneStringProvider.class)
    void createBucketIfNotExists_WithNotExistBucketAsParameter_ShouldInvokeCreateBucketMethod(String bucketName) {
        bucketName = createValidBucketName(bucketName);

        String result = bucketHandler.createBucketIfNotExists(bucketName);

        assertThat(result).isEqualTo(bucketName);
        verify(amazonS3).createBucket(bucketName);
    }

    @ParameterizedTest
    @ArgumentsSource(OneStringProvider.class)
    void createBucketIfNotExists_WithExistBucketAsParameter_ShouldNotInvokeCreateBucketMethod(String bucketName) {
        bucketName = createBucket(bucketName);
        reset(amazonS3);

        String result = bucketHandler.createBucketIfNotExists(bucketName);

        assertThat(result).isEqualTo(bucketName);
        verify(amazonS3, never()).createBucket(bucketName);
    }

    @ParameterizedTest
    @NullSource
    void createBucketIfNotExists_WithNullAsParameter_ShouldThrowException(String bucketName) {
        assertThatThrownBy(() -> bucketHandler.createBucketIfNotExists(bucketName))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @ArgumentsSource(OneStringProvider.class)
    void deleteBucketIfExists_WithNotExistBucketAsParameter_ShouldNotInvokeDeleteBucketMethod(String bucketName) {
        bucketName = createValidBucketName(bucketName);

        String result = bucketHandler.deleteBucketIfExists(bucketName);

        assertThat(result).isEqualTo(bucketName);
        verify(amazonS3, never()).deleteBucket(bucketName);
    }

    @ParameterizedTest
    @ArgumentsSource(OneStringProvider.class)
    void deleteBucketIfExists_WithExistBucketAsParameter_ShouldInvokeDeleteBucketMethod(String bucketName) {
        bucketName = createBucket(bucketName);

        String result = bucketHandler.deleteBucketIfExists(bucketName);

        assertThat(result).isEqualTo(bucketName);
        verify(amazonS3).deleteBucket(bucketName);
    }

    @ParameterizedTest
    @NullSource
    void deleteBucketIfExists_WithNullAsParameter_ShouldThrowException(String bucketName) {
        assertThatThrownBy(() -> bucketHandler.deleteBucketIfExists(bucketName))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @ArgumentsSource(ThreeStringProvider.class)
    void clearBucket_WithValidParameters_ShouldClearBucket(String bucketName, String key, String payload) {
        bucketName = createBucket(bucketName);
        bucketHandler.uploadObject(bucketName, key, payload);

        bucketHandler.clearBucket(bucketName);

        assertThat(bucketHandler.isObjectExist(bucketName, key)).isFalse();
    }

    @ParameterizedTest
    @NullSource
    void clearBucket_WithNullAsParameter_ShouldThrowException(String bucketName) {
        assertThatThrownBy(() -> bucketHandler.clearBucket(bucketName))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @ArgumentsSource(OneStringProvider.class)
    void clearBucket_WithNotExistBucketAsParameter_ShouldNotInvokeDeleteObject(String bucketName) {
        bucketName = createValidBucketName(bucketName);

        String result = bucketHandler.clearBucket(bucketName);

        assertThat(result).isEqualTo(bucketName);
        verify(amazonS3, never()).deleteObject(anyString(), anyString());
    }

    @ParameterizedTest
    @ArgumentsSource(OneStringProvider.class)
    void clearBucket_WithNotExistBucketAsParameter_ShouldNotInvokeDeleteVersion(String bucketName) {
        bucketName = createValidBucketName(bucketName);

        String result = bucketHandler.clearBucket(bucketName);

        assertThat(result).isEqualTo(bucketName);
        verify(amazonS3, never()).deleteVersion(anyString(), anyString(), anyString());
    }

    @ParameterizedTest
    @ArgumentsSource(ThreeStringProvider.class)
    void isObjectExist_WithExistObjectAsParameter_ShouldReturnTrue(String bucketName, String key, String payload) {
        bucketName = createBucket(bucketName);
        bucketHandler.uploadObject(bucketName, key, payload);

        boolean result = bucketHandler.isObjectExist(bucketName, key);

        assertThat(result).isTrue();
    }

    @ParameterizedTest
    @ArgumentsSource(TwoStringProvider.class)
    void isObjectExist_WithNotExistObjectAsParameter_ShouldReturnFalse(String bucketName, String key) {
        bucketName = createBucket(bucketName);

        boolean result = bucketHandler.isObjectExist(bucketName, key);

        assertThat(result).isFalse();
    }

    @ParameterizedTest
    @ArgumentsSource(OneStringProvider.class)
    void isObjectExist_WithNullBucketNameAsParameter_ShouldThrowException(String key) {
        assertThatThrownBy(() -> bucketHandler.isObjectExist(null, key))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @ArgumentsSource(OneStringProvider.class)
    void isObjectExist_WithNullKeyAsParameter_ShouldThrowException(String bucketName) {
        assertThatThrownBy(() -> bucketHandler.isObjectExist(bucketName, null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @ArgumentsSource(ThreeStringProvider.class)
    void uploadObject_WithStringPayloadAsParameter_ShouldUploadObject(String bucketName, String key, String payload) {
        bucketName = createBucket(bucketName);

        String result = bucketHandler.uploadObject(bucketName, key, payload);

        assertThat(result).isEqualTo(buildObjectPath(bucketName, key));
        assertThat(bucketHandler.isObjectExist(bucketName, key)).isTrue();
    }

    @ParameterizedTest
    @ArgumentsSource(TwoStringProvider.class)
    void uploadObject_WithNullBucketNameAsParameter_ShouldThrowException(String key, String payload) {
        assertThatThrownBy(() -> bucketHandler.uploadObject(null, key, payload))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @ArgumentsSource(TwoStringProvider.class)
    void uploadObject_WithNullKeyAsParameter_ShouldThrowException(String bucketName, String payload) {
        assertThatThrownBy(() -> bucketHandler.uploadObject(bucketName, null, payload))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @SneakyThrows
    @ParameterizedTest
    @ArgumentsSource(ThreeStringProvider.class)
    void uploadObject_WithFilePayloadAsParameter_ShouldUploadObject(String bucketName, String key, String payload) {
        bucketName = createBucket(bucketName);
        Path templatePath = tempDir.resolve(SOURCE_TXT);
        Files.write(templatePath, singleton(payload));

        String result = bucketHandler.uploadObject(bucketName, key, templatePath.toFile());

        assertThat(result).isEqualTo(buildObjectPath(bucketName, key));
        assertThat(bucketHandler.isObjectExist(bucketName, key)).isTrue();
    }

    @SneakyThrows
    @ParameterizedTest
    @ArgumentsSource(ThreeStringProvider.class)
    void uploadObject_WithInputStreamPayloadAsParameter_ShouldUploadObject(String bucketName, String key, String payload) {
        bucketName = createBucket(bucketName);
        Path templatePath = tempDir.resolve(SOURCE_TXT);
        Files.write(templatePath, singleton(payload));

        try (InputStream inputStream = Files.newInputStream(templatePath)) {
            String result = bucketHandler.uploadObject(bucketName, key, inputStream, null);

            assertThat(result).isEqualTo(buildObjectPath(bucketName, key));
            assertThat(bucketHandler.isObjectExist(bucketName, key)).isTrue();
        }
    }

    @SneakyThrows
    @ParameterizedTest
    @ArgumentsSource(ThreeStringProvider.class)
    void downloadObject_WithValidParameters_ShouldDownloadObject(String bucketName, String key, String payload) {
        bucketName = createBucket(bucketName);
        bucketHandler.uploadObject(bucketName, key, payload);

        try (InputStream result = bucketHandler.downloadObject(bucketName, key)) {
            assertThat(result).isNotNull();
        }
    }

    @ParameterizedTest
    @ArgumentsSource(ThreeStringProvider.class)
    void listAll_WithValidParameters_ShouldReturnKeys(String bucketName, String key, String payload) {
        bucketName = createBucket(bucketName);
        bucketHandler.uploadObject(bucketName, key, payload);

        List<String> result = bucketHandler.listAll(bucketName);

        assertThat(result)
                .hasSize(1)
                .asList()
                .first()
                .asString()
                .isEqualTo(key);
    }

    private String createBucket(String bucketName) {
        return bucketHandler.createBucketIfNotExists(createValidBucketName(bucketName));
    }

    private String createValidBucketName(String bucketName) {
        return Optional.ofNullable(bucketName)
                .map(String::toLowerCase)
                .map("com-pavelshapel-"::concat)
                .map(this::getVerifiedBucketName)
                .orElseThrow();
    }

    private String getVerifiedBucketName(String bucket) {
        return bucket.length() > 63 ? bucket.substring(0, 63) : bucket;
    }

    private String buildObjectPath(String bucketName, String key) {
        return String.format("s3://%s/%s", bucketName, key);
    }
}