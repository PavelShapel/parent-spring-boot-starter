//package com.pavelshapel.aws.spring.boot.starter.impl.service;
//
//import com.pavelshapel.aws.spring.boot.starter.api.service.DbBackup;
//import com.pavelshapel.aws.spring.boot.starter.api.service.BucketHandler;
//import com.pavelshapel.aws.spring.boot.starter.api.service.DbHandler;
//import com.pavelshapel.aws.spring.boot.starter.properties.AwsProperties;
//import com.pavelshapel.aws.spring.boot.starter.properties.nested.AbstractServiceProperties;
//import com.pavelshapel.json.spring.boot.starter.converter.JsonConverter;
//import lombok.AccessLevel;
//import lombok.experimental.FieldDefaults;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.util.Pair;
//
//import java.util.Optional;
//
//@FieldDefaults(level = AccessLevel.PRIVATE)
//public class ScheduledDbBackup implements DbBackup {
//    @Autowired
//    AwsProperties awsProperties;
//    @Autowired
//    BucketHandler bucketHandler;
//    @Autowired
//    JsonConverter jsonConverter;
//    @Autowired
//    DbHandler dbHandler;
//
//    @Override
//    public String backup(String bucket, String object) {
//        return null;
//    }
//
//    private Pair<String, String> getBucketAndObject() {
//        return Optional.of(Pair.of(getBucket(), getObject()))
//                .filter(pair -> pair.getFirst().isPresent())
//                .filter(pair -> pair.getSecond().isPresent())
//                .map(pair -> Pair.of(pair.getFirst().get(), pair.getSecond().get()))
//                .orElseThrow(() -> new IllegalArgumentException(awsProperties.toString()));
//    }
//
//    private Optional<String> getBucket() {
//        return Optional.of(awsProperties)
//                .map(AwsProperties::getS3)
//                .map(AbstractServiceProperties::getObject)
//                .filter(bucketHandler::isBucketExists);
//    }
//
//    private Optional<String> getObject() {
//        return Optional.of(awsProperties)
//                .map(AwsProperties::getDynamoDb)
//                .map(AbstractServiceProperties::getObject)
//                .map(tableName -> String.format("%s.json", tableName));
//    }
//}
