package com.pavelshapel.aws.spring.boot.starter.properties;

import com.amazonaws.regions.Regions;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;

import static com.pavelshapel.aws.spring.boot.starter.AwsStarterAutoConfiguration.TYPE;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@ConfigurationProperties(prefix = "spring." + TYPE)
public class AwsProperties {
    static final String DEFAULT_REGION = Regions.EU_WEST_1.getName();
    static final String DYNAMO_DB_URL_PATTERN = "https://dynamodb.%s.amazonaws.com/";

    String profile;
    String accessKey;
    String secretKey;
    String region = DEFAULT_REGION;
    String endpoint = String.format(DYNAMO_DB_URL_PATTERN, DEFAULT_REGION);
}