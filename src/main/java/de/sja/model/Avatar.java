package de.sja.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@DynamoDBDocument
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Avatar {

    @NotBlank(message = "A Man needs a Name")
    @DynamoDBAttribute
    private String avatarS3Link;
}
