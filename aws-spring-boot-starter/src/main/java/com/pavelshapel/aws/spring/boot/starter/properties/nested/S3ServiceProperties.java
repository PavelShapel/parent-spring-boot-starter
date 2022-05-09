package com.pavelshapel.aws.spring.boot.starter.properties.nested;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import static com.pavelshapel.aws.spring.boot.starter.annotation.ConditionalOnPropertyS3.S3;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class S3ServiceProperties extends AbstractServiceProperties {
    public S3ServiceProperties() {
        super(S3);
    }
}
