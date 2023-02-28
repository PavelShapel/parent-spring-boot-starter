package com.pavelshapel.aws.spring.boot.starter.context;

import com.pavelshapel.aws.spring.boot.starter.api.service.DynamoDbService;
import com.pavelshapel.aws.spring.boot.starter.converter.AbstractParentalDynamoDBTypeConverter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UserParentalDynamoDBTypeConverter extends AbstractParentalDynamoDBTypeConverter<User> {
    public UserParentalDynamoDBTypeConverter(DynamoDbService<User> dynamoDbService) {
        super(dynamoDbService);
    }
}
