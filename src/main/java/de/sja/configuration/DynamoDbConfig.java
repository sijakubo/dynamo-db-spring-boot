package de.sja.configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DynamoDbConfig {

    @Value("${amazon.dynamodb.endpoint}")
    private String amazonDynamoDbEndpoint;

    @Value("${amazon.aws.accesskey}")
    private String amazonAwsAccessKey;

    @Value("${amazon.aws.secretkey}")
    private String amazonAwsSecretKey;

    @Value("${amazon.aws.region:eu-west-1")
    private String amazonAwsRegion;

    @Bean
    public AmazonDynamoDB createDynamoConnection() {
        return AmazonDynamoDBClient.builder()
            .withCredentials(
                new AWSStaticCredentialsProvider(new BasicAWSCredentials(amazonAwsAccessKey, amazonAwsRegion))
            )
            .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(
                amazonDynamoDbEndpoint, amazonAwsRegion
            ))
            .build();
    }

    @Bean
    public DynamoDBMapper createDynamoMapper(AmazonDynamoDB db) {
        return new DynamoDBMapper(db);
    }

}
