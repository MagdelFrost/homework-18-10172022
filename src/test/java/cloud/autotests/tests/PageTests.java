package cloud.autotests.tests;

import cloud.autotests.tests.components.User;
import com.codeborne.selenide.WebDriverRunner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Cookie;

import static com.codeborne.selenide.Selenide.*;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;


public class PageTests extends TestBase {

    private final String authCookieName = "NOPCOMMERCE.AUTH";
    private final String verificationTokenName = "__RequestVerificationToken";
    private final String verificationTokenInputValue = "10QcxkN4-Gk5PEeZlTrtVTuN7xtnRi_RY4ssN4Kd1kn--wsjFIdx3MtZG3cs6EsIYcWSCd3dIikpNcaAeVkJyMaRx50q_u84GfmcWQFqcqw1";
    private final String verificationTokenHeaderValue = "at9THwDl4iOCtU40qL9aL87W4x6vnP7C7vDFXJ6VVruf0QlYjJGo4vKOOZ37as2KBsbYUCMAENIXnFqvW6QHp-85oL4JZadn5TQu5MPCDv41;";
    User user = new User();

    @AfterEach
    public void clearBrowser() {
        clearBrowserCookies();
        clearBrowserLocalStorage();
    }

    @Test
    @DisplayName("Регистрация через UI")
    void registerNewUserUITest() {

        step("Открываем страницу регистрации пользователя", () -> {
            open("/register");
        });

        step("Заполняем обязательные поля формы регистрации", () -> {
            user
                    .setFirstName(firstName)
                    .setLastName(lastName)
                    .setEmail(email)
                    .setPassword(password);
        });

        step("Отправляем заполненную форму на сервер", () -> {
            user.sendRegistrationForm();
        });

        step("Проверяем, что пользователь зарегистрирован", () -> {
            user.checkUserCreated(email);
        });
    }

    @Test
    @DisplayName("Регистрация через API")
    void registerNewUserAPITest() {
        String authCookieValue = step("Регистрируем пользователя через API ", () -> given()
                .when()
                .contentType("application/x-www-form-urlencoded; charset=utf-8")
                .formParam(verificationTokenName, verificationTokenInputValue)
                .formParam("FirstName", firstName)
                .formParam("LastName", lastName)
                .formParam("Email", email)
                .formParam("Password", password)
                .formParam("ConfirmPassword", password)
                .cookie(verificationTokenName, verificationTokenHeaderValue)
                .post("/register")
                .then()
                .extract().cookie(authCookieName));


        step("Подкладываем куки созданного пользователя", () -> {
            open("/Themes/DefaultClean/Content/images/logo.png");
            Cookie authCookie = new Cookie(authCookieName, authCookieValue);
            WebDriverRunner.getWebDriver().manage().addCookie(authCookie);
        });

        step("Открываем сайт", () -> {
            open("/registerresult/1");
        });

        step("Проверяем, что пользователь зарегистрирован", () -> {
            user.checkUserCreated(email);
        });
    }

    @Test
    @DisplayName("Редактирование профиля через UI")
    void userCanModifyProfileTest() {

        step("Открываем страницу регистрации пользователя", () -> {
            open("/register");
        });

        step("Заполняем обязательные поля формы регистрации", () -> {
            user
                    .setFirstName(firstName)
                    .setLastName(lastName)
                    .setEmail(email)
                    .setPassword(password);
        });

        step("Отправляем заполненную форму на сервер", () -> {
            user.sendRegistrationForm();
        });

        step("Открываем профиль", () -> {
            user.openProfile();
        });

        step("Заполняем профиль новыми занчениями", () -> {
            user
                    .setFirstName(anotherName)
                    .setLastName(anotherLastName)
                    .setEmail(anotherEmail);
        });

        step("Сохраняем профиль", () -> {
            user.saveProfile();
        });

        step("Повторно открываем профиль", () -> {
            user.openProfile();
        });

        step("Проверяем, что изменения сохранились", () -> {
            user.checkChangesSaved(anotherEmail, anotherName, anotherLastName);
        });
    }

    @Test
    @DisplayName("Редактирование профиля через API")
    void userCanModifyProfileAPITest() {
        String authCookieValue = step("Регистрируем пользователя через API ", () -> given()
                .when()
                .contentType("application/x-www-form-urlencoded; charset=utf-8")
                .formParam(verificationTokenName, verificationTokenInputValue)
                .formParam("FirstName", firstName)
                .formParam("LastName", lastName)
                .formParam("Email", email)
                .formParam("Password", password)
                .formParam("ConfirmPassword", password)
                .cookie(verificationTokenName, verificationTokenHeaderValue)
                .post("/register")
                .then()
                .extract().cookie(authCookieName));

        step("Подкладываем куки созданного пользователя", () -> {
            open("/Themes/DefaultClean/Content/images/logo.png");
            Cookie authCookie = new Cookie(authCookieName, authCookieValue);
            WebDriverRunner.getWebDriver().manage().addCookie(authCookie);
        });

        step("Открываем профиль", () -> {
            open("/customer/info");
        });

        step("Заполняем профиль новыми занчениями", () -> {
            user
                    .setFirstName(anotherName)
                    .setLastName(anotherLastName)
                    .setEmail(anotherEmail);
        });

        step("Сохраняем профиль", () -> {
            user.saveProfile();
        });

        step("Повторно открываем профиль", () -> {
            user.openProfile();
        });

        step("Проверяем, что изменения сохранились", () -> {
            user.checkChangesSaved(anotherEmail, anotherName, anotherLastName);
        });
    }
}