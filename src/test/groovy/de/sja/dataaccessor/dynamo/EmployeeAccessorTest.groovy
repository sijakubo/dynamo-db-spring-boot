package de.sja.dataaccessor.dynamo

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB
import de.sja.model.Employee
import de.sja.model.Role
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification

@SpringBootTest
@ActiveProfiles("dev")
class EmployeeAccessorTest extends Specification {

  @Autowired
  private EmployeeAccessor employeeAccessor

  @Autowired
  private AmazonDynamoDB db

  void setup() {
    if (!db.listTables().getTableNames().isEmpty()) {
      db.deleteTable(Employee.TABLE_NAME)
    }

    db.createTable(new Employee().createTableRequest())
  }

  def "should find Employees by Role"() {
    given:
      employeeAccessor.saveAll([
        new Employee(
          email: "dev#1@mail.de",
          role: Role.DEVELOPER
        ),
        new Employee(
          email: "dev#2@mail.de",
          role: Role.DEVELOPER
        ),
        new Employee(
          email: "cons#1@mail.de",
          role: Role.CONSULTANT
        )
      ])

    expect:
      expectedEmployess.sort() == employeeAccessor.findByRole(role).sort()

    where:
      role            || expectedEmployess
      Role.DEVELOPER  || ["dev#1@mail.de", "dev#2@mail.de"]
      Role.CONSULTANT || ["cons#1@mail.de"]
  }
}
