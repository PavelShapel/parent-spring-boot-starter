package com.pavelshapel.aws.spring.boot.starter.context;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import com.pavelshapel.core.spring.boot.starter.api.model.Entity;
import com.pavelshapel.core.spring.boot.starter.api.model.Named;
import com.pavelshapel.core.spring.boot.starter.api.model.ParentalEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@DynamoDBTable(tableName = "User")
public class User implements Entity<String>, ParentalEntity<String, User>, Named {
    @DynamoDBHashKey
    @DynamoDBAutoGeneratedKey
    String id;
    @DynamoDBTypeConverted(converter = UserParentalDynamoDBTypeConverter.class)
    @DynamoDBAttribute
    User parent;
    @DynamoDBAttribute
    String name;

    @Override
    public int compareTo(Entity<String> entity) {
        return getId().compareTo(entity.getId());
    }
}