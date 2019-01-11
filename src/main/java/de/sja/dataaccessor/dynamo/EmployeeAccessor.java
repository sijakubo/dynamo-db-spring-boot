package de.sja.dataaccessor.dynamo;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.google.common.collect.ImmutableMap;
import de.sja.model.Employee;
import de.sja.model.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class EmployeeAccessor {

    private final AmazonDynamoDB db;
    private final DynamoDBMapper dbMapper;

    public void saveAll(Collection<Employee> employees) {
        dbMapper.batchSave(employees);
    }

    public void save(Employee employee) {
        dbMapper.save(employee);
    }

    public List<String> findByRole(Role role) {
        List<Map<String, AttributeValue>> result =
            db.scan(
                new ScanRequest(Employee.TABLE_NAME)
                    .withScanFilter(ImmutableMap.of("role",
                        new Condition()
                            .withComparisonOperator(ComparisonOperator.EQ)
                            .withAttributeValueList(new AttributeValue(role.name()))
                    ))
                    .withAttributesToGet("email")
            ).getItems();

        return result
            .stream()
            .map(stringAttributeValueMap -> stringAttributeValueMap.get("email").getS())
            .collect(Collectors.toList());
    }
}
