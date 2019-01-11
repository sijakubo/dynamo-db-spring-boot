package de.sja.model

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper
import com.amazonaws.services.dynamodbv2.model.AttributeValue
import com.amazonaws.services.dynamodbv2.model.ScanRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification

import java.time.LocalDate

@SpringBootTest
@ActiveProfiles("dev")
class EmployeeTest extends Specification {

  @Autowired
  private AmazonDynamoDB db

  @Autowired
  private DynamoDBMapper dbMapper

  void setup() {
    if (!db.listTables().getTableNames().isEmpty()) {
      db.deleteTable(Employee.TABLE_NAME)
    }

    db.createTable(new Employee().createTableRequest())
  }

  def "should persist and find a saved Employee"() {
    given:
      dbMapper.save(
        new Employee(
          email: "sijakubo@testmail.de",
          firstname: "Simon",
          lastname: "Jakubowski",
          role: Role.DEVELOPER,
          birthDate: LocalDate.of(1985, 7, 1),
          avatar: new Avatar("https://s3/simon.png")
        )
      )

    when:
      Employee loadedEmployee = dbMapper.load(Employee.class, "sijakubo@testmail.de")

    then:
      loadedEmployee.getAvatar().getAvatarS3Link() == "https://s3/simon.png"
      loadedEmployee.getLastname() == "Jakubowski"
  }

  def "should find all employees firstnames"() {
    given:
      dbMapper.save(new Employee(email: "sijakubo@testmail.de", firstname: "Simon"))
      dbMapper.save(new Employee(email: "someOtherGuy@testmail.de", firstname: "Other"))
      dbMapper.save(new Employee(email: "anotherOne@testmail.de", firstname: "Some"))

    when:
      List<Map<String, AttributeValue>> result = db.scan(
        new ScanRequest(Employee.TABLE_NAME)
          .withAttributesToGet("firstname")
      ).getItems()

    then:
      result.size() == 3
      result.contains(["firstname": new AttributeValue("Simon")])
      result.contains(["firstname": new AttributeValue("Other")])
      result.contains(["firstname": new AttributeValue("Some")])
  }
}
