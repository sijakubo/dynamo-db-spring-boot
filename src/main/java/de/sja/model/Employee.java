package de.sja.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConvertedEnum;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;
import de.sja.model.converter.LocalDateConverter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Collections;
import javax.validation.constraints.Pattern;

@NoArgsConstructor
@Getter
@Setter
@DynamoDBTable(tableName = Employee.TABLE_NAME)
public class Employee implements DynamoDbStoreable {

    public static final String TABLE_NAME = "employee";

    @Pattern(regexp = ""
        + "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\""
        + "(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\["
        + "\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0"
        + "-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|"
        + "[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*"
        + "[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\"
        + "[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")
    @DynamoDBHashKey
    private String email;

    @DynamoDBAttribute
    private String firstname;

    @DynamoDBAttribute
    private String lastname;

    @DynamoDBTypeConverted(converter = LocalDateConverter.class)
    @DynamoDBAttribute
    private LocalDate birthDate;

    @DynamoDBTypeConvertedEnum
    private Role role;

    @DynamoDBAttribute
    private Avatar avatar;

    @Override
    public CreateTableRequest createTableRequest() {
        return new CreateTableRequest()
            .withTableName(TABLE_NAME)
            .withKeySchema(
                Collections.singletonList(
                    new KeySchemaElement()
                        .withAttributeName("email")
                        .withKeyType(KeyType.HASH)
                )
            )
            .withProvisionedThroughput(
                new ProvisionedThroughput()
                    .withReadCapacityUnits(10L)
                    .withWriteCapacityUnits(10L))
            .withAttributeDefinitions(
                new AttributeDefinition()
                    .withAttributeName("email")
                    .withAttributeType(ScalarAttributeType.S)
            );
    }
}
