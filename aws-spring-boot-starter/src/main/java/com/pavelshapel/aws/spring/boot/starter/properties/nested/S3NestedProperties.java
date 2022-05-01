package com.pavelshapel.aws.spring.boot.starter.properties.nested;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import static com.pavelshapel.aws.spring.boot.starter.properties.AwsProperties.DEFAULT_REGION;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class S3NestedProperties extends AbstractNestedProperties {
    static final String S3_URL_PATTERN = "s3-website-%s.amazonaws.com";

    String endpoint = String.format(S3_URL_PATTERN, DEFAULT_REGION);
    String bucket;
}
