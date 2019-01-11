package de.sja.dataaccessor.dynamo

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB
import de.sja.model.Employee
import de.sja.model.Role
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification

import java.time.LocalDate

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

  def "should update a Employee"() {
    given:
      def employee = new Employee(email: "theEmp@mail.de", role: Role.DEVELOPER)
      employeeAccessor.save(employee)

    when:
      employee.setBirthDate(LocalDate.of(1990, 12, 1))
      employee.setRole(Role.CONSULTANT)
      employee.setFirstname("The")
      employee.setLastname("Emp")

      employeeAccessor.save(employee)

    then:
      def updatedEmployee = employeeAccessor.findByMail(employee.getEmail())
      updatedEmployee.getBirthDate() == LocalDate.of(1990, 12, 1)
      updatedEmployee.getRole() == Role.CONSULTANT
      updatedEmployee.getFirstname() == "The"
      updatedEmployee.getLastname() == "Emp"
  }
}
