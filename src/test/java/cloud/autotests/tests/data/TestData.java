package cloud.autotests.tests.data;

import com.github.javafaker.Faker;

public class TestData {

    Faker faker = new Faker();

    String password = faker.numerify("#*#*#*#*#");
    String firstName = faker.name().firstName();
    String lastName = faker.name().lastName();
    String email = faker.internet().emailAddress();
    String anotherEmail = faker.internet().emailAddress();
    String anotherName = faker.name().firstName();
    String anotherLastName = faker.name().lastName();

    public String getPassword() {
        return this.password;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public String getEmail() {
        return this.email;
    }

    public String getAnotherEmail() {
        return this.anotherEmail;
    }

    public String getAnotherName() {
        return this.anotherName;
    }

    public String getAnotherLastName() {
        return this.anotherLastName;
    }
}
