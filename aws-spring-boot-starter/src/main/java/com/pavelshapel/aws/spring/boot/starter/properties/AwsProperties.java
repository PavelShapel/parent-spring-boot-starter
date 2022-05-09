package com.pavelshapel.aws.spring.boot.starter.properties;

import com.amazonaws.regions.Regions;
import com.pavelshapel.aws.spring.boot.starter.properties.nested.DynamoDbServiceProperties;
import com.pavelshapel.aws.spring.boot.starter.properties.nested.LambdaServiceProperties;
import com.pavelshapel.aws.spring.boot.starter.properties.nested.S3ServiceProperties;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;

import static com.pavelshapel.aws.spring.boot.starter.AwsStarterAutoConfiguration.TYPE;
import static com.pavelshapel.aws.spring.boot.starter.properties.AwsProperties.PREFIX;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@ConfigurationProperties(prefix = PREFIX)
public class AwsProperties {
    public static final String PREFIX = "spring." + TYPE;
    public static final String DEFAULT_REGION = Regions.EU_WEST_1.getName();

    String profile;
    String accessKey;
    String secretKey;
    String region = DEFAULT_REGION;
    DynamoDbServiceProperties dynamoDb;
    S3ServiceProperties s3;
    LambdaServiceProperties lambda;
}