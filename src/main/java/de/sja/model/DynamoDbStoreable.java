package de.sja.model;

import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;

public interface DynamoDbStoreable {

    CreateTableRequest createTableRequest();
}
